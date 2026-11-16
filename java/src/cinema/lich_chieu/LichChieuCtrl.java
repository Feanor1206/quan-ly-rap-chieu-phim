    }

    public boolean kiemTraTrungPhong(int maPhong, LocalDateTime batDau, LocalDateTime ketThuc) {
        if (batDau == null || ketThuc == null || !batDau.isBefore(ketThuc)) {
            throw new IllegalArgumentException("Khoảng giờ chiếu không hợp lệ.");
        }
        for (SuatChieu sc : suatChieuDAO.layTheoPhong(maPhong)) {
            if (trungThoiGian(batDau, ketThuc, sc.batDau, sc.ketThuc)) {
                return true;
            }
        }
        return false;
    }

    // Tạo lịch chiếu và lưu cả bảng giá theo từng loại ghế.
    public SuatChieu themLichChieu(Phim phim, PhongChieu phong,
                                   LocalDate ngayChieu, LocalTime gioBatDau,
                                   Map<LoaiGhe, BigDecimal> giaTheoLoaiGhe) {
        if (phim == null || phong == null || ngayChieu == null || gioBatDau == null
                || giaTheoLoaiGhe == null || giaTheoLoaiGhe.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin trước khi thêm lịch chiếu!");
        }
        if (phim.thoiLuongPhut <= 0) {
            throw new IllegalArgumentException("Thời lượng phim phải lớn hơn 0.");
        }
        for (Map.Entry<LoaiGhe, BigDecimal> item : giaTheoLoaiGhe.entrySet()) {
            if (item.getKey() == null || item.getValue() == null
                    || item.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Loại ghế và giá vé phải hợp lệ, giá vé > 0.");
            }
        }

        LocalDateTime batDau = LocalDateTime.of(ngayChieu, gioBatDau);
        LocalDateTime ketThuc = tinhGioKetThuc(batDau, phim.thoiLuongPhut);
        SuatChieu sc = new SuatChieu(phim, phong, batDau, ketThuc, giaTheoLoaiGhe);

        // DAO phải kiểm tra trùng và lưu như MỘT thao tác nguyên tử.
        if (!suatChieuDAO.luuNeuKhongTrung(sc)) {
            throw new IllegalStateException(
                    "Phòng chiếu đã có lịch tại thời điểm này. Vui lòng chọn phòng hoặc khung giờ khác!");
        }
        return sc;
    }

    public enum LoaiGhe { THUONG, VIP, DOI }

    public static class Phim {
        public final int maPhim;
        public final int thoiLuongPhut;

        public Phim(int maPhim, int thoiLuongPhut) {
            this.maPhim = maPhim;
            this.thoiLuongPhut = thoiLuongPhut;
        }
    }

    public static class PhongChieu {
        public final int maPhong;

        public PhongChieu(int maPhong) {
            this.maPhong = maPhong;
        }
    }

    public static class SuatChieu {
        public final Phim phim;
        public final PhongChieu phong;
        public final LocalDateTime batDau;
        public final LocalDateTime ketThuc;
        public final Map<LoaiGhe, BigDecimal> giaTheoLoaiGhe;

        public SuatChieu(Phim phim, PhongChieu phong, LocalDateTime batDau,
                         LocalDateTime ketThuc, Map<LoaiGhe, BigDecimal> giaTheoLoaiGhe) {
            this.phim = phim;
            this.phong = phong;
            this.batDau = batDau;
            this.ketThuc = ketThuc;
            EnumMap<LoaiGhe, BigDecimal> copy = new EnumMap<>(LoaiGhe.class);
            copy.putAll(giaTheoLoaiGhe);
            this.giaTheoLoaiGhe = Collections.unmodifiableMap(copy);
        }
    }

    // Khi kết nối MySQL, thay lớp Memory bằng SuatChieuDAO dùng JDBC.
    public interface SuatChieuDAO {
        List<SuatChieu> layTheoPhong(int maPhong);
        boolean luuNeuKhongTrung(SuatChieu sc);
    }

    // DAO tạm để chạy thử khi chưa có CSDL; KHÔNG lưu dữ liệu lâu dài.
    public static class SuatChieuDAOMemory implements SuatChieuDAO {
        private final List<SuatChieu> ds = new ArrayList<>();

        @Override
        public synchronized List<SuatChieu> layTheoPhong(int maPhong) {
            List<SuatChieu> ketQua = new ArrayList<>();
            for (SuatChieu sc : ds) {
                if (sc.phong.maPhong == maPhong) ketQua.add(sc);
            }
            return ketQua;
        }

        @Override
        public synchronized boolean luuNeuKhongTrung(SuatChieu moi) {
            for (SuatChieu cu : ds) {
                if (cu.phong.maPhong == moi.phong.maPhong
                        && moi.batDau.isBefore(cu.ketThuc)
                        && cu.batDau.isBefore(moi.ketThuc)) {
                    return false;
                }
            }
            ds.add(moi);
            return true;
        }
    }
}
