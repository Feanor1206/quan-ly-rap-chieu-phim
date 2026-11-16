package cinema.ban_ve.dao;

import cinema.ban_ve.model.SuatChieu;
import cinema.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SuatChieuDAO {
    private static final List<SuatChieu> memoryStore = new ArrayList<>();

    static {
        LocalDate homNay = LocalDate.now();
        memoryStore.add(new SuatChieu(
                "LC01", "Đào, Phở và Piano", "Phòng 01 (Dolby Atmos)",
                homNay, java.time.LocalTime.of(9, 30), java.time.LocalTime.of(11, 25),
                100, "T13", 80000.0, 5, 8
        ));
        memoryStore.add(new SuatChieu(
                "LC02", "Mai", "Phòng 02 (IMAX Laser)",
                homNay, java.time.LocalTime.of(14, 0), java.time.LocalTime.of(16, 30),
                131, "T18", 110000.0, 6, 10
        ));
        memoryStore.add(new SuatChieu(
                "LC03", "Lật Mặt 7: Một Điều Ước", "Phòng 01 (Dolby Atmos)",
                homNay, java.time.LocalTime.of(19, 0), java.time.LocalTime.of(21, 35),
                138, "T13", 95000.0, 5, 8
        ));
    }

    public List<SuatChieu> layDanhSachSuatChieu() {
        List<SuatChieu> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT lc.id_lich_chieu, p.ten_phim, pc.ten_phong, lc.ngay_chieu, " +
                         "lc.gio_bat_dau, lc.gio_ket_thuc, p.thoi_luong, p.gioi_han_tuoi, " +
                         "lc.gia_ve_co_ban, pc.so_hang, pc.so_cot " +
                         "FROM tbl_lich_chieu lc " +
                         "JOIN tbl_phim p ON lc.id_phim = p.id_phim " +
                         "JOIN tbl_phong_chieu pc ON lc.id_phong = pc.id_phong " +
                         "WHERE lc.ngay_chieu >= CURDATE() " +
                         "ORDER BY lc.ngay_chieu ASC, lc.gio_bat_dau ASC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Time start = rs.getTime("gio_bat_dau");
                    Time end = rs.getTime("gio_ket_thuc");
                    list.add(new SuatChieu(
                            rs.getString("id_lich_chieu"),
                            rs.getString("ten_phim"),
                            rs.getString("ten_phong"),
                            rs.getDate("ngay_chieu").toLocalDate(),
                            start != null ? start.toLocalTime() : null,
                            end != null ? end.toLocalTime() : null,
                            rs.getInt("thoi_luong"),
                            rs.getString("gioi_han_tuoi"),
                            rs.getDouble("gia_ve_co_ban"),
                            rs.getInt("so_hang"),
                            rs.getInt("so_cot")
                    ));
                }
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (SQLException e) {
                System.err.println("Lỗi layDanhSachSuatChieu: " + e.getMessage());
            }
        }
        return new ArrayList<>(memoryStore);
    }
}

