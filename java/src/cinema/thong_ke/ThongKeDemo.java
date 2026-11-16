package cinema.thong_ke;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class ThongKeDemo {
    public static void main(String[] args) throws IOException, SQLException {
        var dao = new ThongKeDAO(taoDuLieuMau(Path.of("out", "m4-demo.sqlite3")));
        String matKhauQuanLy = UUID.randomUUID().toString();
        String matKhauNhanVien = UUID.randomUUID().toString();
        var mayChu = taoMayChu(Integer.getInteger("m4.port", 8080), dao, matKhauQuanLy, matKhauNhanVien);
        mayChu.start();
        System.out.printf("http://127.0.0.1:%d%s?tuNgay=2026-12-15&denNgay=2026-12-17%n",
                mayChu.getAddress().getPort(), ThongKeDoanhThuCtrl.DUONG_DAN);
        System.out.printf("Quản lý: ql / %s%nNhân viên rạp 1: nv / %s%n", matKhauQuanLy, matKhauNhanVien);
    }

    static HttpServer taoMayChu(int cong, ThongKeDAO dao, String matKhauQuanLy, String matKhauNhanVien)
            throws IOException {
        var matKhau = Map.of("ql", matKhauQuanLy, "nv", matKhauNhanVien);
        var taiKhoan = Map.of("ql", new ThongKeDoanhThuCtrl.TaiKhoan("QL", null),
                "nv", new ThongKeDoanhThuCtrl.TaiKhoan("NV", 1L));
        var ctrl = new ThongKeDoanhThuCtrl(dao);
        var mayChu = HttpServer.create(new InetSocketAddress("127.0.0.1", cong), 0);
        var trang = mayChu.createContext(ThongKeDoanhThuCtrl.DUONG_DAN,
                yeuCau -> ctrl.xuLy(yeuCau, taiKhoan.get(yeuCau.getPrincipal().getUsername())));
        trang.setAuthenticator(new BasicAuthenticator("Thong ke doanh thu", UTF_8) {
            @Override
            public boolean checkCredentials(String ten, String matKhauNhap) {
                String matKhauDung = matKhau.get(ten);
                return matKhauDung != null && MessageDigest.isEqual(
                        matKhauDung.getBytes(UTF_8), matKhauNhap.getBytes(UTF_8));
            }
        });
        return mayChu;
    }

    static DataSource taoDuLieuMau(Path tep) throws IOException, SQLException {
        Files.createDirectories(tep.toAbsolutePath().getParent());
        var cauHinh = new SQLiteConfig();
        cauHinh.enforceForeignKeys(true);
        var nguonDuLieu = new SQLiteDataSource(cauHinh);
        nguonDuLieu.setUrl("jdbc:sqlite:" + tep.toAbsolutePath());

        try (var taiNguyen = ThongKeDemo.class.getResourceAsStream("/m4-demo.sql");
             var ketNoi = nguonDuLieu.getConnection();
             var lenh = ketNoi.createStatement()) {
            if (taiNguyen == null) {
                throw new IOException("Thiếu m4-demo.sql.");
            }
            ketNoi.setAutoCommit(false);
            for (String sql : new String(taiNguyen.readAllBytes(), UTF_8).split(";")) {
                if (!sql.isBlank()) {
                    lenh.executeUpdate(sql);
                }
            }
            ketNoi.commit();
        }
        return nguonDuLieu;
    }
}
