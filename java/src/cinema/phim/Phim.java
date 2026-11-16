package cinema.phim;

public class Phim {
    private int id;
    private String maPhim;
    private String tenPhim;
    private String theLoai;
    private int thoiLuong;
    private String daoDien;
    private String dienVien;
    private String moTa;
    private int namSanXuat;

    public Phim() {
    }

    public Phim(int id, String maPhim, String tenPhim, String theLoai, int thoiLuong,
                String daoDien, String dienVien, String moTa, int namSanXuat) {
        this.id = id;
        this.maPhim = maPhim;
        this.tenPhim = tenPhim;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.daoDien = daoDien;
        this.dienVien = dienVien;
        this.moTa = moTa;
        this.namSanXuat = namSanXuat;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaPhim() { return maPhim; }
    public void setMaPhim(String maPhim) { this.maPhim = maPhim; }

    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }

    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }

    public int getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(int thoiLuong) { this.thoiLuong = thoiLuong; }

    public String getDaoDien() { return daoDien; }
    public void setDaoDien(String daoDien) { this.daoDien = daoDien; }

    public String getDienVien() { return dienVien; }
    public void setDienVien(String dienVien) { this.dienVien = dienVien; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public int getNamSanXuat() { return namSanXuat; }
    public void setNamSanXuat(int namSanXuat) { this.namSanXuat = namSanXuat; }
}