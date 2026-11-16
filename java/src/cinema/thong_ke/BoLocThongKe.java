package cinema.thong_ke;

import java.time.LocalDate;

public record BoLocThongKe(LocalDate tuNgay, LocalDate denNgay, Nhom nhom,
                           Long nhomId, Long suatChieuId) {
    public enum Nhom {
        PHIM, RAP
    }

    public BoLocThongKe {
        if (tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc.");
        }
        if (tuNgay.getYear() < 1 || !denNgay.isBefore(LocalDate.of(9999, 12, 31))) {
            throw new IllegalArgumentException("Ngày lọc không hợp lệ.");
        }
        if ((nhomId != null && nhomId <= 0)
                || (suatChieuId != null && (suatChieuId <= 0 || nhomId == null))) {
            throw new IllegalArgumentException("Mã nhóm hoặc suất chiếu không hợp lệ.");
        }
    }
}
