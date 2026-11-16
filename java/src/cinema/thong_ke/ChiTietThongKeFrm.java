package cinema.thong_ke;

import java.time.format.DateTimeFormatter;
import java.util.List;

class ChiTietThongKeFrm {
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/uuuu");

    static String hienThi(BoLocThongKe boLoc, List<ThongKeDAO.DongDoanhThu> cacDong) {
        boolean xemHoaDon = boLoc.suatChieuId() != null;
        String tieuDe = xemHoaDon ? "Hóa đơn của suất #" + boLoc.suatChieuId() : "Doanh thu theo suất chiếu";
        String urlQuayLai = ThongKeDoanhThuFrm.lienKet(boLoc, xemHoaDon ? boLoc.nhomId() : null, null);
        String tieuDeCot = xemHoaDon ? "Hóa đơn và ngày lập" : "Suất chiếu";
        String dieuHuong = """
                <p><a href="%s">%s</a></p>
                <p>Từ %s đến %s</p>
                """.formatted(ThongKeDoanhThuFrm.thoat(urlQuayLai),
                xemHoaDon ? "Về danh sách suất chiếu" : "Về bảng tổng hợp",
                NGAY.format(boLoc.tuNgay()), NGAY.format(boLoc.denNgay()));
        return ThongKeDoanhThuFrm.trang(tieuDe,
                dieuHuong + ThongKeDoanhThuFrm.bang(boLoc, tieuDeCot, cacDong));
    }
}
