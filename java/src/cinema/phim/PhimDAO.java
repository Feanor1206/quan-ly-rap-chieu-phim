package cinema.phim;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhimDAO {

    public List<Phim> timPhimTheoTen(String tuKhoa) {
        List<Phim> dsPhim = new ArrayList<>();
        String sql = "SELECT * FROM Phim WHERE tenPhim LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + tuKhoa.trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Phim p = new Phim();
                    p.setId(rs.getInt("id"));
                    p.setMaPhim(rs.getString("maPhim"));
                    p.setTenPhim(rs.getString("tenPhim"));
                    p.setTheLoai(rs.getString("theLoai"));
                    p.setThoiLuong(rs.getInt("thoiLuong"));
                    p.setDaoDien(rs.getString("daoDien"));
                    p.setDienVien(rs.getString("dienVien"));
                    p.setMoTa(rs.getString("moTa"));
                    p.setNamSanXuat(rs.getInt("namSanXuat"));
                    dsPhim.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsPhim;
    }

    public boolean capNhatPhim(Phim p) {
        String sql = "UPDATE Phim SET maPhim = ?, tenPhim = ?, theLoai = ?, thoiLuong = ?, " +
                     "daoDien = ?, dienVien = ?, moTa = ?, namSanXuat = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getMaPhim());
            ps.setString(2, p.getTenPhim());
            ps.setString(3, p.getTheLoai());
            ps.setInt(4, p.getThoiLuong());
            ps.setString(5, p.getDaoDien());
            ps.setString(6, p.getDienVien());
            ps.setString(7, p.getMoTa());
            ps.setInt(8, p.getNamSanXuat());
            ps.setInt(9, p.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}