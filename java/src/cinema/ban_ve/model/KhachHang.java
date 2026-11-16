package cinema.ban_ve.model;

public class KhachHang {
    private String idKH;
    private String hoTen;
    private String soDienThoai;
    private int diemTichLuy;
    private String loaiKH;

    public KhachHang() {
        this.diemTichLuy = 0;
        this.loaiKH = "Standard";
    }

    public KhachHang(String idKH, String hoTen, String soDienThoai, int diemTichLuy, String loaiKH) {
        this.idKH = idKH;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.diemTichLuy = diemTichLuy;
        this.loaiKH = loaiKH != null ? loaiKH : "Standard";
    }

    public String getIdKH() {
        return idKH;
    }

    public void setIdKH(String idKH) {
        this.idKH = idKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String getLoaiKH() {
        return loaiKH;
    }

    public void setLoaiKH(String loaiKH) {
        this.loaiKH = loaiKH;
    }

    public boolean coTheDoiVe() {
        return this.diemTichLuy >= 500;
    }

    public int getSoVeCoTheDoi() {
        return this.diemTichLuy / 500;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | SĐT: %s | Điểm: %d | Hạng: %s",
                idKH, hoTen, soDienThoai, diemTichLuy, loaiKH);
    }
}
