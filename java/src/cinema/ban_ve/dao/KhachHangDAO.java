package cinema.ban_ve.dao;

import cinema.ban_ve.model.KhachHang;
import cinema.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KhachHangDAO {
    private static final List<KhachHang> memoryStore = new ArrayList<>();

    static {

        memoryStore.add(new KhachHang("KH001", "Nguyễn Văn An", "0901234567", 850, "VIP"));
        memoryStore.add(new KhachHang("KH002", "Trần Thị Bình", "0912345678", 1200, "Diamond"));
        memoryStore.add(new KhachHang("KH003", "Lê Hoàng Cường", "0988776655", 200, "Standard"));
    }

    public KhachHang traCuuKHTT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
            return null;
        }
        String cleanPhone = sdt.trim();

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT id_kh, ho_ten, so_dien_thoai, diem_tich_luy, loai_kh " +
                         "FROM tbl_khach_hang WHERE so_dien_thoai = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, cleanPhone);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new KhachHang(
                                rs.getString("id_kh"),
                                rs.getString("ho_ten"),
                                rs.getString("so_dien_thoai"),
                                rs.getInt("diem_tich_luy"),
                                rs.getString("loai_kh")
                        );
                    }
                }
            } catch (SQLException e) {
                System.err.println("Lỗi traCuuKHTT từ CSDL: " + e.getMessage());
            }
        }

        return memoryStore.stream()
                .filter(k -> cleanPhone.equals(k.getSoDienThoai()))
                .findFirst()
                .orElse(null);
    }

    public boolean capNhatDiemTichLuy(String idKH, int diemMoi, Connection conn) throws SQLException {
        if (idKH == null) return false;

        if (conn != null) {
            String sql = "UPDATE tbl_khach_hang SET diem_tich_luy = ? WHERE id_kh = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, diemMoi);
                ps.setString(2, idKH);
                return ps.executeUpdate() > 0;
            }
        }

        for (KhachHang kh : memoryStore) {
            if (idKH.equalsIgnoreCase(kh.getIdKH())) {
                kh.setDiemTichLuy(diemMoi);
                return true;
            }
        }
        return false;
    }

    public boolean luuKhachHang(KhachHang kh) {
        if (kh == null) return false;

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "INSERT INTO tbl_khach_hang (id_kh, ho_ten, so_dien_thoai, diem_tich_luy, loai_kh) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE ho_ten = VALUES(ho_ten), diem_tich_luy = VALUES(diem_tich_luy), loai_kh = VALUES(loai_kh)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, kh.getIdKH());
                ps.setString(2, kh.getHoTen());
                ps.setString(3, kh.getSoDienThoai());
                ps.setInt(4, kh.getDiemTichLuy());
                ps.setString(5, kh.getLoaiKH());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Lỗi luuKhachHang vào CSDL: " + e.getMessage());
            }
        }

        memoryStore.removeIf(k -> k.getIdKH().equalsIgnoreCase(kh.getIdKH()) ||
                                   k.getSoDienThoai().equals(kh.getSoDienThoai()));
        memoryStore.add(kh);
        return true;
    }

    public List<KhachHang> layTatCa() {
        return new ArrayList<>(memoryStore);
    }
}
