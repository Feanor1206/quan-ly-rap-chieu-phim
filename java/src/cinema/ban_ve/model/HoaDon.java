package cinema.ban_ve.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HoaDon {
    private String idHoaDon;
    private LocalDateTime ngayLap;
    private double tongTienGoc;
    private double discount;
    private double tongThanhToan;
    private String phuongThucTT;
    private String idKH;
    private String nhanVienLap;

    private List<VeXemPhim> danhSachVe = new ArrayList<>();
    private KhachHang khachHang;

    public HoaDon() {
        this.ngayLap = LocalDateTime.now();
        this.phuongThucTT = "TIEN_MAT";
        this.nhanVienLap = "NV_QUAY";
    }

    public HoaDon(String idHoaDon, String phuongThucTT, String idKH, String nhanVienLap) {
        this.idHoaDon = idHoaDon;
        this.ngayLap = LocalDateTime.now();
        this.phuongThucTT = phuongThucTT != null ? phuongThucTT : "TIEN_MAT";
        this.idKH = idKH;
        this.nhanVienLap = nhanVienLap != null ? nhanVienLap : "NV_QUAY";
    }

    public HoaDon(String idHoaDon, LocalDateTime ngayLap, double tongTienGoc, double discount,
                  double tongThanhToan, String phuongThucTT, String idKH, String nhanVienLap) {
        this.idHoaDon = idHoaDon;
        this.ngayLap = ngayLap;
        this.tongTienGoc = tongTienGoc;
        this.discount = discount;
        this.tongThanhToan = tongThanhToan;
        this.phuongThucTT = phuongThucTT;
        this.idKH = idKH;
        this.nhanVienLap = nhanVienLap;
    }

    public String getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTienGoc() {
        return tongTienGoc;
    }

    public void setTongTienGoc(double tongTienGoc) {
        this.tongTienGoc = tongTienGoc;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(double tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        this.phuongThucTT = phuongThucTT;
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public String getNhanVienLap() {
        return nhanVienLap;
    }

    public void setNhanVienLap(String nhanVienLap) {
        this.nhanVienLap = nhanVienLap;
    }

    public List<VeXemPhim> getDanhSachVe() {
        return danhSachVe;
    }

    public void setDanhSachVe(List<VeXemPhim> danhSachVe) {
        this.danhSachVe = danhSachVe;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
        if (khachHang != null) {
            this.idKH = khachHang.getIdKH();
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("Hóa đơn [%s] | %s | Số vé: %d | Gốc: %,.0f đ | Giảm: %,.0f đ | Thực trả: %,.0f đ",
                idHoaDon, ngayLap != null ? ngayLap.format(fmt) : "",
                danhSachVe != null ? danhSachVe.size() : 0,
                tongTienGoc, discount, tongThanhToan);
    }
}
