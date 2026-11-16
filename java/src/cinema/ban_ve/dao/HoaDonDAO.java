package cinema.ban_ve.dao;

import cinema.ban_ve.model.HoaDon;
import cinema.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HoaDonDAO {
    private static final List<HoaDon> memoryStore = new ArrayList<>();

    static {
        memoryStore.add(new HoaDon(
                "HD20261116001",
                LocalDateTime.now(),
                160000.0,
                0.0,
                160000.0,
                "TIEN_MAT",
                "KH001",
                "NV_QUAY"
        ));
    }

    public boolean luuHoaDon(HoaDon hd, Connection conn) throws SQLException {
        if (hd == null) return false;

        if (conn != null) {
            String sql = "INSERT INTO tbl_hoa_don (id_hoa_don, ngay_lap, tong_tien_goc, discount, tong_thanh_toan, phuong_thuc_tt, id_kh, nhan_vien_lap) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, hd.getIdHoaDon());
                ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap() != null ? hd.getNgayLap() : LocalDateTime.now()));
                ps.setDouble(3, hd.getTongTienGoc());
                ps.setDouble(4, hd.getDiscount());
                ps.setDouble(5, hd.getTongThanhToan());
                ps.setString(6, hd.getPhuongThucTT());
                ps.setString(7, hd.getIdKH());
                ps.setString(8, hd.getNhanVienLap());
                return ps.executeUpdate() > 0;
            }
        }

        memoryStore.removeIf(h -> h.getIdHoaDon().equalsIgnoreCase(hd.getIdHoaDon()));
        memoryStore.add(hd);
        return true;
    }

    public boolean luuHoaDon(HoaDon hd) {
        Connection conn = DBConnection.getConnection();
        try {
            return luuHoaDon(hd, conn);
        } catch (SQLException e) {
            System.err.println("Lỗi lưu hóa đơn độc lập: " + e.getMessage());
            memoryStore.add(hd);
            return true;
        }
    }

    public Optional<HoaDon> timTheoMa(String idHoaDon) {
        if (idHoaDon == null) return Optional.empty();

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT id_hoa_don, ngay_lap, tong_tien_goc, discount, tong_thanh_toan, phuong_thuc_tt, id_kh, nhan_vien_lap " +
                         "FROM tbl_hoa_don WHERE id_hoa_don = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idHoaDon.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Timestamp ts = rs.getTimestamp("ngay_lap");
                        return Optional.of(new HoaDon(
                                rs.getString("id_hoa_don"),
                                ts != null ? ts.toLocalDateTime() : LocalDateTime.now(),
                                rs.getDouble("tong_tien_goc"),
                                rs.getDouble("discount"),
                                rs.getDouble("tong_thanh_toan"),
                                rs.getString("phuong_thuc_tt"),
                                rs.getString("id_kh"),
                                rs.getString("nhan_vien_lap")
                        ));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Lỗi timTheoMa HoaDon: " + e.getMessage());
            }
        }

        return memoryStore.stream()
                .filter(h -> idHoaDon.equalsIgnoreCase(h.getIdHoaDon()))
                .findFirst();
    }

    public List<HoaDon> layTatCa() {
        List<HoaDon> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT id_hoa_don, ngay_lap, tong_tien_goc, discount, tong_thanh_toan, phuong_thuc_tt, id_kh, nhan_vien_lap " +
                         "FROM tbl_hoa_don ORDER BY ngay_lap DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("ngay_lap");
                    list.add(new HoaDon(
                            rs.getString("id_hoa_don"),
                            ts != null ? ts.toLocalDateTime() : LocalDateTime.now(),
                            rs.getDouble("tong_tien_goc"),
                            rs.getDouble("discount"),
                            rs.getDouble("tong_thanh_toan"),
                            rs.getString("phuong_thuc_tt"),
                            rs.getString("id_kh"),
                            rs.getString("nhan_vien_lap")
                    ));
                }
                return list;
            } catch (SQLException e) {
                System.err.println("Lỗi layTatCa HoaDon: " + e.getMessage());
            }
        }
        return new ArrayList<>(memoryStore);
    }
}
