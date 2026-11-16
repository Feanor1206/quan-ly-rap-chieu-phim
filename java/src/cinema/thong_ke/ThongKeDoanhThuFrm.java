package cinema.thong_ke;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

class ThongKeDoanhThuFrm {
    static String hienThi(BoLocThongKe boLoc, List<ThongKeDAO.DongDoanhThu> cacDong) {
        boolean theoPhim = boLoc.nhom() == BoLocThongKe.Nhom.PHIM;
        String form = """
                <form action="%s" method="get">
                    <label>Từ ngày<input type="date" name="tuNgay" value="%s" required></label>
                    <label>Đến ngày<input type="date" name="denNgay" value="%s" required></label>
                    <label>Thống kê theo
                        <select name="nhom">
                            <option value="phim"%s>Phim</option>
                            <option value="rap"%s>Rạp</option>
                        </select>
                    </label>
                    <button type="submit">Xem doanh thu</button>
                </form>
                """.formatted(ThongKeDoanhThuCtrl.DUONG_DAN, boLoc.tuNgay(), boLoc.denNgay(),
                theoPhim ? " selected" : "", theoPhim ? "" : " selected");
        return trang("Thống kê doanh thu", form + bang(boLoc, theoPhim ? "Phim" : "Rạp", cacDong));
    }

    static String bang(BoLocThongKe boLoc, String cotDau, List<ThongKeDAO.DongDoanhThu> cacDong) {
        if (cacDong.isEmpty()) {
            return "<p>Không có giao dịch trong khoảng ngày này.</p>";
        }
        var bang = new StringBuilder("""
                <div class="bang" role="region" aria-label="Bảng doanh thu" tabindex="0">
                <table>
                    <thead><tr><th scope="col">%s</th><th scope="col">Bán vé (₫)</th>
                    <th scope="col">Hoàn vé (₫)</th><th scope="col">Doanh thu (₫)</th></tr></thead>
                    <tbody>
                """.formatted(cotDau));
        var dinhDang = NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN"));
        long tongBan = 0;
        long tongHoan = 0;
        for (var dong : cacDong) {
            String ten = thoat(dong.ten());
            if (boLoc.suatChieuId() != null) {
                ten = "#" + dong.id() + " · " + ten;
            } else {
                String url = boLoc.nhomId() == null
                        ? lienKet(boLoc, dong.id(), null)
                        : lienKet(boLoc, boLoc.nhomId(), dong.id());
                ten = "<a href=\"" + thoat(url) + "\">" + ten + "</a>";
            }
            themDong(bang, dinhDang, ten, dong.tienBan(), dong.tienHoan(), dong.doanhThu());
            tongBan += dong.tienBan();
            tongHoan += dong.tienHoan();
        }
        bang.append("</tbody><tfoot>");
        themDong(bang, dinhDang, "Tổng", tongBan, tongHoan, tongBan - tongHoan);
        return bang.append("</tfoot></table></div>").toString();
    }

    private static void themDong(StringBuilder bang, NumberFormat dinhDang, String ten,
                                long ban, long hoan, long doanhThu) {
        bang.append("<tr><th scope=\"row\">").append(ten).append("</th><td>")
                .append(dinhDang.format(ban)).append("</td><td>").append(dinhDang.format(hoan))
                .append("</td><td>").append(dinhDang.format(doanhThu)).append("</td></tr>");
    }

    static String lienKet(BoLocThongKe boLoc, Long nhomId, Long suatChieuId) {
        return ThongKeDoanhThuCtrl.DUONG_DAN + "?tuNgay=" + boLoc.tuNgay() + "&denNgay=" + boLoc.denNgay()
                + "&nhom=" + boLoc.nhom().name().toLowerCase(Locale.ROOT)
                + (nhomId == null ? "" : "&nhomId=" + nhomId)
                + (suatChieuId == null ? "" : "&suatChieuId=" + suatChieuId);
    }

    static String thoat(String chuoi) {
        return chuoi.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    static String trang(String tieuDe, String noiDung) {
        return """
                <!doctype html>
                <html lang="vi">
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>%s</title>
                    <style>
                        body { margin: 0; background: #f5f7fa; color: #182236; font: 1rem/1.5 Inter, system-ui, sans-serif; }
                        main { max-width: 64rem; margin: auto; padding: 2rem 1rem; }
                        h1 { margin: 0 0 1rem; font-size: 1.75rem; }
                        form { display: flex; flex-wrap: wrap; align-items: end; gap: .75rem; margin-bottom: 1rem; }
                        label { display: grid; gap: .25rem; font-weight: 600; }
                        input, select, button { font: inherit; padding: .45rem .65rem; border: 1px solid #687888; border-radius: .3rem; }
                        input, select { background: white; color: inherit; }
                        button { background: #174983; color: white; cursor: pointer; }
                        a { color: #174983; }
                        :focus-visible { outline: 2px solid #174983; outline-offset: 3px; }
                        .bang { overflow-x: auto; }
                        table { border-collapse: collapse; width: 100%%; background: white; }
                        td, th { border-bottom: 1px solid #d8e0e9; padding: .65rem; text-align: right; }
                        th:first-child { text-align: left; }
                        td { white-space: nowrap; font-variant-numeric: tabular-nums; }
                        thead, tfoot { background: #e8eef7; font-weight: 600; }
                    </style>
                </head>
                <body><main><h1>%s</h1>%s</main></body>
                </html>
                """.formatted(thoat(tieuDe), thoat(tieuDe), noiDung);
    }
}
