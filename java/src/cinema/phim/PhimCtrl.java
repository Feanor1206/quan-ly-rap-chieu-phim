package cinema.phim;

import java.util.List;

public class PhimCtrl {
    private PhimDAO phimDAO;

    public PhimCtrl() {
        this.phimDAO = new PhimDAO();
    }

    public List<Phim> timPhimTheoTen(String tuKhoa) {
        if (tuKhoa == null) {
            tuKhoa = "";
        }
        return phimDAO.timPhimTheoTen(tuKhoa.trim());
    }

    public boolean capNhatPhim(Phim p) {
        if (p == null || p.getId() <= 0) {
            return false;
        }
        if (isNullOrEmpty(p.getMaPhim()) || isNullOrEmpty(p.getTenPhim()) ||
            isNullOrEmpty(p.getTheLoai()) || isNullOrEmpty(p.getDaoDien()) ||
            isNullOrEmpty(p.getDienVien())) {
            return false;
        }
        if (p.getThoiLuong() <= 0 || p.getNamSanXuat() <= 0) {
            return false;
        }
        return phimDAO.capNhatPhim(p);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}