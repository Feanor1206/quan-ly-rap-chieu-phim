package cinema.ban_ve.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SuatChieu {
    private String idLichChieu;
    private String tenPhim;
    private String tenPhong;
    private LocalDate ngayChieu;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private int thoiLuong;
    private String gioiHanTuoi;
    private double giaVeCoBan;
    private int soHang;
    private int soCot;

    public SuatChieu() {
    }

    public SuatChieu(String idLichChieu, String tenPhim, String tenPhong, LocalDate ngayChieu,
                     LocalTime gioBatDau, LocalTime gioKetThuc, int thoiLuong,
                     String gioiHanTuoi, double giaVeCoBan, int soHang, int soCot) {
        this.idLichChieu = idLichChieu;
        this.tenPhim = tenPhim;
        this.tenPhong = tenPhong;
        this.ngayChieu = ngayChieu;
        this.gioBatDau = gioBatDau;
        this.gioKetThuc = gioKetThuc;
        this.thoiLuong = thoiLuong;
        this.gioiHanTuoi = gioiHanTuoi;
        this.giaVeCoBan = giaVeCoBan;
        this.soHang = soHang;
        this.soCot = soCot;
    }

    public String getIdLichChieu() {
        return idLichChieu;
    }

    public void setIdLichChieu(String idLichChieu) {
        this.idLichChieu = idLichChieu;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public LocalDate getNgayChieu() {
        return ngayChieu;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public LocalTime getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(LocalTime gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public LocalTime getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(LocalTime gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getGioiHanTuoi() {
        return gioiHanTuoi;
    }

    public void setGioiHanTuoi(String gioiHanTuoi) {
        this.gioiHanTuoi = gioiHanTuoi;
    }

    public double getGiaVeCoBan() {
        return giaVeCoBan;
    }

    public void setGiaVeCoBan(double giaVeCoBan) {
        this.giaVeCoBan = giaVeCoBan;
    }

    public int getSoHang() {
        return soHang;
    }

    public void setSoHang(int soHang) {
        this.soHang = soHang;
    }

    public int getSoCot() {
        return soCot;
    }

    public void setSoCot(int soCot) {
        this.soCot = soCot;
    }

    public double tinhGiaTheoLoaiGhe(String loaiGhe) {
        if ("VIP".equalsIgnoreCase(loaiGhe)) {
            return giaVeCoBan * 1.25;
        } else if ("Couple".equalsIgnoreCase(loaiGhe) || "SWEETBOX".equalsIgnoreCase(loaiGhe)) {
            return giaVeCoBan * 1.8;
        }
        return giaVeCoBan;
    }

    @Override
    public String toString() {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        return String.format("[%s] %s | %s - %s | %s",
                idLichChieu, tenPhim,
                gioBatDau != null ? gioBatDau.format(timeFmt) : "",
                gioKetThuc != null ? gioKetThuc.format(timeFmt) : "",
                tenPhong);
    }
}

