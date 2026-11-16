package cinema.thong_ke;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ThongKeDAO {
    private static final String SQL_THONG_KE = """
            WITH GiaoDich(suatChieuId, hoaDonId, tienBan, tienHoan) AS (
                SELECT v.suatChieuId, v.hoaDonId, v.tienVeThucTra, 0
                FROM VeXemPhim v
                JOIN HoaDon h ON h.id = v.hoaDonId
                WHERE h.thoiDiemLap >= ? AND h.thoiDiemLap < ?
                UNION ALL
                SELECT v.suatChieuId, v.hoaDonId, 0, t.tienHoan
                FROM GiaoDichTraVe t
                JOIN VeXemPhim v ON v.id = t.veXemPhimId
                WHERE t.thoiDiemTra >= ? AND t.thoiDiemTra < ?
            )
            SELECT %s AS id, %s AS ten, SUM(g.tienBan) AS tienBan, SUM(g.tienHoan) AS tienHoan
            FROM GiaoDich g
            JOIN HoaDon h ON h.id = g.hoaDonId
            JOIN SuatChieu s ON s.id = g.suatChieuId
            JOIN Phim p ON p.id = s.phimId
            JOIN PhongChieu c ON c.id = s.phongChieuId
            JOIN RapPhim r ON r.id = c.rapPhimId
            WHERE r.id = COALESCE(?, r.id)
              AND %s = COALESCE(?, %s)
              AND s.id = COALESCE(?, s.id)
            GROUP BY 1, 2
            ORDER BY 2, 1
            """;

    private final DataSource nguonDuLieu;

    public ThongKeDAO(DataSource nguonDuLieu) {
        this.nguonDuLieu = nguonDuLieu;
    }

    public List<DongDoanhThu> thongKe(BoLocThongKe boLoc, Long rapDuocXem) throws SQLException {
        boolean theoPhim = boLoc.nhom() == BoLocThongKe.Nhom.PHIM;
        String cotNhom = theoPhim ? "p.id" : "r.id";
        String cotId = cotNhom;
        String cotTen = theoPhim ? "p.tenPhim" : "r.tenRap";
        if (boLoc.suatChieuId() != null) {
            cotId = "h.id";
            cotTen = "h.thoiDiemLap";
        } else if (boLoc.nhomId() != null) {
            cotId = "s.id";
            cotTen = "s.gioBatDau || ' · ' || p.tenPhim || ' · ' || r.tenRap || ' · ' || c.tenPhong";
        }

        String tuNgay = boLoc.tuNgay().toString();
        String sauDenNgay = boLoc.denNgay().plusDays(1).toString();
        String sql = SQL_THONG_KE.formatted(cotId, cotTen, cotNhom, cotNhom);
        var cacDong = new ArrayList<DongDoanhThu>();
        try (var ketNoi = nguonDuLieu.getConnection(); var truyVan = ketNoi.prepareStatement(sql)) {
            truyVan.setString(1, tuNgay);
            truyVan.setString(2, sauDenNgay);
            truyVan.setString(3, tuNgay);
            truyVan.setString(4, sauDenNgay);
            truyVan.setObject(5, rapDuocXem);
            truyVan.setObject(6, boLoc.nhomId());
            truyVan.setObject(7, boLoc.suatChieuId());
            try (var ketQua = truyVan.executeQuery()) {
                while (ketQua.next()) {
                    cacDong.add(new DongDoanhThu(ketQua.getLong("id"), ketQua.getString("ten"),
                            ketQua.getLong("tienBan"), ketQua.getLong("tienHoan")));
                }
            }
        }
        return cacDong;
    }

    public boolean suatChieuThuocRap(long suatChieuId, long rapPhimId) throws SQLException {
        String sql = """
                SELECT 1 FROM SuatChieu s
                JOIN PhongChieu c ON c.id = s.phongChieuId
                WHERE s.id = ? AND c.rapPhimId = ?
                """;
        try (var ketNoi = nguonDuLieu.getConnection(); var truyVan = ketNoi.prepareStatement(sql)) {
            truyVan.setLong(1, suatChieuId);
            truyVan.setLong(2, rapPhimId);
            try (var ketQua = truyVan.executeQuery()) {
                return ketQua.next();
            }
        }
    }

    public record DongDoanhThu(long id, String ten, long tienBan, long tienHoan) {
        public long doanhThu() {
            return tienBan - tienHoan;
        }
    }
}
