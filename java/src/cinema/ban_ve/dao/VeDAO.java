package cinema.ban_ve.dao;

import cinema.ban_ve.model.VeXemPhim;
import cinema.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VeDAO {

    private static final Map<String, List<VeXemPhim>> memorySeatsByLichChieu = new HashMap<>();

    static {

        khoiTaoGheMauChoLichChieu("LC01", 5, 8, 80000.0);

        capNhatGheMauDaBan("LC01", "A3", "HD20261116001");
        capNhatGheMauDaBan("LC01", "A4", "HD20261116001");

        khoiTaoGheMauChoLichChieu("LC02", 6, 10, 110000.0);
        capNhatGheMauDaBan("LC02", "C5", "HD20261116002");
        capNhatGheMauDaBan("LC02", "C6", "HD20261116002");

        khoiTaoGheMauChoLichChieu("LC03", 5, 8, 95000.0);
    }

    private static void khoiTaoGheMauChoLichChieu(String idLichChieu, int soHang, int soCot, double giaCoBan) {
        List<VeXemPhim> list = new ArrayList<>();
        for (int r = 0; r < soHang; r++) {
            char hang = (char) ('A' + r);
            String loaiGhe;
            double gia;
            if (hang == 'A' || hang == 'B') {
                loaiGhe = "Regular";
                gia = giaCoBan;
            } else if (hang == 'C' || hang == 'D') {
                loaiGhe = "VIP";
                gia = giaCoBan * 1.25;
            } else {
                loaiGhe = "Couple";
                gia = giaCoBan * 1.8;
            }

            for (int c = 1; c <= soCot; c++) {
                String maGhe = String.format("%c%d", hang, c);
                String idVe = String.format("VE-%s-%s", idLichChieu, maGhe);
                list.add(new VeXemPhim(idVe, idLichChieu, maGhe, loaiGhe, gia, "TRONG"));
            }
        }
        memorySeatsByLichChieu.put(idLichChieu, list);
    }

    private static void capNhatGheMauDaBan(String idLichChieu, String maGhe, String idHoaDon) {
        List<VeXemPhim> list = memorySeatsByLichChieu.get(idLichChieu);
        if (list != null) {
            for (VeXemPhim v : list) {
                if (v.getMaGhe().equalsIgnoreCase(maGhe)) {
                    v.setTrangThai("DA_BAN");
                    v.setIdHoaDon(idHoaDon);
                    v.setThoiGianBan(LocalDateTime.now());
                }
            }
        }
    }

    public List<VeXemPhim> layGheCuaLichChieu(String idLichChieu) {
        List<VeXemPhim> dsVe = new ArrayList<>();
        if (idLichChieu == null)
            return dsVe;

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT id_ve, id_lich_chieu, ma_ghe, loai_ghe, gia_ve, trang_thai, id_hoa_don, thoi_gian_ban "
                    +
                    "FROM tbl_ve WHERE id_lich_chieu = ? ORDER BY ma_ghe ASC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idLichChieu.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Timestamp ts = rs.getTimestamp("thoi_gian_ban");
                        dsVe.add(new VeXemPhim(
                                rs.getString("id_ve"),
                                rs.getString("id_lich_chieu"),
                                rs.getString("ma_ghe"),
                                rs.getString("loai_ghe"),
                                rs.getDouble("gia_ve"),
                                rs.getString("trang_thai"),
                                rs.getString("id_hoa_don"),
                                ts != null ? ts.toLocalDateTime() : null));
                    }
                }
                if (!dsVe.isEmpty()) {
                    return dsVe;
                }
            } catch (SQLException e) {
                System.err.println("Lỗi layGheCuaLichChieu từ CSDL: " + e.getMessage());
            }
        }

        List<VeXemPhim> memList = memorySeatsByLichChieu.get(idLichChieu);
        if (memList != null) {

            for (VeXemPhim v : memList) {
                dsVe.add(new VeXemPhim(
                        v.getIdVe(), v.getIdLichChieu(), v.getMaGhe(), v.getLoaiGhe(),
                        v.getGiaVe(), v.getTrangThai(), v.getIdHoaDon(), v.getThoiGianBan()));
            }
            return dsVe;
        }

        khoiTaoGheMauChoLichChieu(idLichChieu, 5, 8, 80000.0);
        return layGheCuaLichChieu(idLichChieu);
    }

    public boolean capNhatTrangThai(List<VeXemPhim> dsVeSelected, String idHoaDon, Connection conn)
            throws SQLException {
        if (dsVeSelected == null || dsVeSelected.isEmpty())
            return true;

        if (conn != null) {

            String sqlCheck = "SELECT ma_ghe FROM tbl_ve WHERE id_lich_chieu = ? AND ma_ghe = ? AND trang_thai = 'DA_BAN'";
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                for (VeXemPhim v : dsVeSelected) {
                    psCheck.setString(1, v.getIdLichChieu());
                    psCheck.setString(2, v.getMaGhe());
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next()) {
                            throw new SQLException(
                                    "Ghế " + v.getMaGhe() + " đã bị tài khoản khác thanh toán trước đó!");
                        }
                    }
                }
            }

            String sqlUpdate = "INSERT INTO tbl_ve (id_ve, id_lich_chieu, ma_ghe, loai_ghe, gia_ve, trang_thai, id_hoa_don, thoi_gian_ban) "
                    +
                    "VALUES (?, ?, ?, ?, ?, 'DA_BAN', ?, ?) " +
                    "ON DUPLICATE KEY UPDATE trang_thai = 'DA_BAN', id_hoa_don = VALUES(id_hoa_don), thoi_gian_ban = VALUES(thoi_gian_ban)";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                for (VeXemPhim v : dsVeSelected) {
                    ps.setString(1, v.getIdVe());
                    ps.setString(2, v.getIdLichChieu());
                    ps.setString(3, v.getMaGhe());
                    ps.setString(4, v.getLoaiGhe());
                    ps.setDouble(5, v.getGiaVe());
                    ps.setString(6, idHoaDon);
                    ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return true;
        }

        for (VeXemPhim v : dsVeSelected) {
            List<VeXemPhim> memList = memorySeatsByLichChieu.get(v.getIdLichChieu());
            if (memList != null) {
                for (VeXemPhim item : memList) {
                    if (item.getMaGhe().equalsIgnoreCase(v.getMaGhe())) {
                        if ("DA_BAN".equalsIgnoreCase(item.getTrangThai())) {
                            throw new SQLException(
                                    "Ghế " + v.getMaGhe() + " đã bị tài khoản khác thanh toán trước đó!");
                        }
                        item.setTrangThai("DA_BAN");
                        item.setIdHoaDon(idHoaDon);
                        item.setThoiGianBan(LocalDateTime.now());
                    }
                }
            }
        }
        return true;
    }

    public List<VeXemPhim> layVeTheoHoaDon(String idHoaDon) {
        List<VeXemPhim> list = new ArrayList<>();
        if (idHoaDon == null)
            return list;

        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            String sql = "SELECT id_ve, id_lich_chieu, ma_ghe, loai_ghe, gia_ve, trang_thai, id_hoa_don, thoi_gian_ban "
                    +
                    "FROM tbl_ve WHERE id_hoa_don = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idHoaDon);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Timestamp ts = rs.getTimestamp("thoi_gian_ban");
                        list.add(new VeXemPhim(
                                rs.getString("id_ve"),
                                rs.getString("id_lich_chieu"),
                                rs.getString("ma_ghe"),
                                rs.getString("loai_ghe"),
                                rs.getDouble("gia_ve"),
                                rs.getString("trang_thai"),
                                rs.getString("id_hoa_don"),
                                ts != null ? ts.toLocalDateTime() : null));
                    }
                }
                return list;
            } catch (SQLException e) {
                System.err.println("Lỗi layVeTheoHoaDon: " + e.getMessage());
            }
        }

        for (List<VeXemPhim> memList : memorySeatsByLichChieu.values()) {
            for (VeXemPhim v : memList) {
                if (idHoaDon.equalsIgnoreCase(v.getIdHoaDon())) {
                    list.add(v);
                }
            }
        }
        return list;
    }
}
