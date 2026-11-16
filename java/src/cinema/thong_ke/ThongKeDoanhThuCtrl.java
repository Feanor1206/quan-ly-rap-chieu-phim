package cinema.thong_ke;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class ThongKeDoanhThuCtrl {
    public static final String DUONG_DAN = "/thong-ke";
    private final ThongKeDAO dao;

    public ThongKeDoanhThuCtrl(ThongKeDAO dao) {
        this.dao = dao;
    }

    public void xuLy(HttpExchange yeuCau, TaiKhoan taiKhoan) throws IOException {
        try (yeuCau) {
            if (!DUONG_DAN.equals(yeuCau.getRequestURI().getPath())) {
                baoLoi(yeuCau, 404, "Không tìm thấy trang.");
                return;
            }
            if (!"GET".equals(yeuCau.getRequestMethod())) {
                yeuCau.getResponseHeaders().set("Allow", "GET");
                baoLoi(yeuCau, 405, "Phương thức không được hỗ trợ.");
                return;
            }
            boolean quanLy = taiKhoan != null && "QL".equals(taiKhoan.vaiTro());
            if (!quanLy && (taiKhoan == null || !taiKhoan.nhanVienHopLe())) {
                baoLoi(yeuCau, 403, "Bạn không có quyền xem doanh thu.");
                return;
            }

            try {
                var boLoc = docBoLoc(yeuCau.getRequestURI().getRawQuery());
                Long rapDuocXem = quanLy ? null : taiKhoan.rapPhimId();
                if (!coQuyenXem(boLoc, rapDuocXem)) {
                    baoLoi(yeuCau, 403, "Bạn chỉ được xem doanh thu rạp của mình.");
                    return;
                }
                var cacDong = dao.thongKe(boLoc, rapDuocXem);
                String html = boLoc.nhomId() == null
                        ? ThongKeDoanhThuFrm.hienThi(boLoc, cacDong)
                        : ChiTietThongKeFrm.hienThi(boLoc, cacDong);
                guiTrang(yeuCau, 200, html);
            } catch (IllegalArgumentException ex) {
                baoLoi(yeuCau, 400, ex.getMessage());
            } catch (SQLException ex) {
                System.getLogger(getClass().getName()).log(System.Logger.Level.ERROR,
                        "Không truy vấn được doanh thu", ex);
                baoLoi(yeuCau, 500, "Không đọc được dữ liệu doanh thu.");
            }
        }
    }

    private boolean coQuyenXem(BoLocThongKe boLoc, Long rapDuocXem) throws SQLException {
        if (rapDuocXem == null) {
            return true;
        }
        if (boLoc.nhom() == BoLocThongKe.Nhom.RAP && boLoc.nhomId() != null
                && !rapDuocXem.equals(boLoc.nhomId())) {
            return false;
        }
        return boLoc.suatChieuId() == null || dao.suatChieuThuocRap(boLoc.suatChieuId(), rapDuocXem);
    }

    private static BoLocThongKe docBoLoc(String truyVan) {
        var thamSo = new HashMap<String, String>();
        if (truyVan != null && !truyVan.isEmpty()) {
            for (String cap : truyVan.split("&")) {
                var tach = cap.split("=", 2);
                if (tach.length != 2 || thamSo.putIfAbsent(URLDecoder.decode(tach[0], UTF_8),
                        URLDecoder.decode(tach[1], UTF_8)) != null) {
                    throw new IllegalArgumentException("Bộ lọc không hợp lệ.");
                }
            }
        }

        var nhom = switch (thamSo.getOrDefault("nhom", "phim")) {
            case "phim" -> BoLocThongKe.Nhom.PHIM;
            case "rap" -> BoLocThongKe.Nhom.RAP;
            default -> throw new IllegalArgumentException("Chọn thống kê theo phim hoặc rạp.");
        };
        var tuNgay = thamSo.get("tuNgay");
        var denNgay = thamSo.get("denNgay");
        if ((tuNgay == null) != (denNgay == null)) {
            throw new IllegalArgumentException("Chọn đủ ngày bắt đầu và ngày kết thúc.");
        }
        var homNay = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        try {
            return new BoLocThongKe(tuNgay == null ? homNay.withDayOfMonth(1) : LocalDate.parse(tuNgay),
                    denNgay == null ? homNay : LocalDate.parse(denNgay), nhom,
                    docMa(thamSo.get("nhomId")), docMa(thamSo.get("suatChieuId")));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Ngày lọc không hợp lệ.");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Mã nhóm hoặc suất chiếu không hợp lệ.");
        }
    }

    private static Long docMa(String ma) {
        return ma == null ? null : Long.valueOf(ma);
    }

    private static void baoLoi(HttpExchange yeuCau, int trangThai, String thongBao) throws IOException {
        guiTrang(yeuCau, trangThai, ThongKeDoanhThuFrm.trang("Thống kê doanh thu",
                "<p role=\"alert\">" + ThongKeDoanhThuFrm.thoat(thongBao) + "</p>"
                        + "<a href=\"" + DUONG_DAN + "\">Về trang thống kê</a>"));
    }

    private static void guiTrang(HttpExchange yeuCau, int trangThai, String html) throws IOException {
        byte[] noiDung = html.getBytes(UTF_8);
        var tieuDe = yeuCau.getResponseHeaders();
        tieuDe.set("Content-Type", "text/html; charset=UTF-8");
        tieuDe.set("Cache-Control", "no-store");
        tieuDe.set("X-Content-Type-Options", "nosniff");
        tieuDe.set("Content-Security-Policy",
                "default-src 'none'; style-src 'unsafe-inline'; form-action 'self'; frame-ancestors 'none'; base-uri 'none'");
        yeuCau.sendResponseHeaders(trangThai, noiDung.length);
        yeuCau.getResponseBody().write(noiDung);
    }

    public record TaiKhoan(String vaiTro, Long rapPhimId) {
        boolean nhanVienHopLe() {
            return "NV".equals(vaiTro) && rapPhimId != null && rapPhimId > 0;
        }
    }
}
