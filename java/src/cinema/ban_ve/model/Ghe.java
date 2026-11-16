package cinema.ban_ve.model;

public class Ghe {
    private String maGhe;
    private String hangGhe;
    private int soGhe;
    private String loaiGhe;

    public Ghe() {
        this.loaiGhe = "Regular";
    }

    public Ghe(String maGhe, String hangGhe, int soGhe, String loaiGhe) {
        this.maGhe = maGhe;
        this.hangGhe = hangGhe;
        this.soGhe = soGhe;
        this.loaiGhe = loaiGhe != null ? loaiGhe : "Regular";
    }

    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public String getHangGhe() {
        return hangGhe;
    }

    public void setHangGhe(String hangGhe) {
        this.hangGhe = hangGhe;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(String loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", maGhe, loaiGhe);
    }
}

