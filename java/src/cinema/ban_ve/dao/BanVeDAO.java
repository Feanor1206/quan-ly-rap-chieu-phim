package cinema.ban_ve.dao;

import cinema.ban_ve.model.HoaDon;
import cinema.ban_ve.model.KhachHang;
import cinema.ban_ve.model.VeXemPhim;

import java.util.List;

public class BanVeDAO {
    private final VeDAO veDAO;
    private final HoaDonDAO hoaDonDAO;
    private final KhachHangDAO khachHangDAO;
    private final SuatChieuDAO suatChieuDAO;

    public BanVeDAO() {
        this.veDAO = new VeDAO();
        this.hoaDonDAO = new HoaDonDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.suatChieuDAO = new SuatChieuDAO();
    }

    public VeDAO getVeDAO() {
        return veDAO;
    }

    public HoaDonDAO getHoaDonDAO() {
        return hoaDonDAO;
    }

    public KhachHangDAO getKhachHangDAO() {
        return khachHangDAO;
    }

    public SuatChieuDAO getSuatChieuDAO() {
        return suatChieuDAO;
    }
}
