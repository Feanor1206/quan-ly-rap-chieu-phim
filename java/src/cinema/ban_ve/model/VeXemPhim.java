package cinema.ban_ve.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VeXemPhim {
    private String idVe;
    private String idLichChieu;
    private String maGhe;
    private String loaiGhe;
    private double giaVe;
    private String trangThai;
    private String idHoaDon;
    private LocalDateTime thoiGianBan;

    public VeXemPhim() {
        this.trangThai = "TRONG";
        this.loaiGhe = "Regular";
    }

    public VeXemPhim(String idVe, String idLichChieu, String maGhe, String loaiGhe, double giaVe, String trangThai) {
        this.idVe = idVe;
        this.idLichChieu = idLichChieu;
        this.maGhe = maGhe;
        this.loaiGhe = loaiGhe != null ? loaiGhe : "Regular";
        this.giaVe = giaVe;
        this.trangThai = trangThai != null ? trangThai : "TRONG";
    }

    public VeXemPhim(String idVe, String idLichChieu, String maGhe, String loaiGhe, double giaVe,
                     String trangThai, String idHoaDon, LocalDateTime thoiGianBan) {
        this.idVe = idVe;
        this.idLichChieu = idLichChieu;
        this.maGhe = maGhe;
        this.loaiGhe = loaiGhe != null ? loaiGhe : "Regular";
        this.giaVe = giaVe;
        this.trangThai = trangThai != null ? trangThai : "TRONG";
        this.idHoaDon = idHoaDon;
        this.thoiGianBan = thoiGianBan;
    }

    public String getIdVe() {
        return idVe;
    }

    public void setIdVe(String idVe) {
        this.idVe = idVe;
    }

    public String getIdLichChieu() {
        return idLichChieu;
    }

    public void setIdLichChieu(String idLichChieu) {
        this.idLichChieu = idLichChieu;
    }

    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public String getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public double getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(double giaVe) {
        this.giaVe = giaVe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public LocalDateTime getThoiGianBan() {
        return thoiGianBan;
    }

    public void setThoiGianBan(LocalDateTime thoiGianBan) {
        this.thoiGianBan = thoiGianBan;
    }

    public boolean isTrong() {
        return "TRONG".equalsIgnoreCase(trangThai);
    }

    public boolean isDaBan() {
        return "DA_BAN".equalsIgnoreCase(trangThai);
    }

    public boolean isDangGiu() {
        return "DANG_GIU".equalsIgnoreCase(trangThai);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Vé [%s] | Suất: %s | Ghế: %s (%s) | Giá: %,.0f đ | Trạng thái: %s",
                idVe, idLichChieu, maGhe, loaiGhe, giaVe, trangThai);
    }
}
