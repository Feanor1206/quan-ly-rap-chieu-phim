package cinema.ban_ve;

import cinema.ban_ve.dao.HoaDonDAO;
import cinema.ban_ve.dao.KhachHangDAO;
import cinema.ban_ve.dao.SuatChieuDAO;
import cinema.ban_ve.dao.VeDAO;
import cinema.ban_ve.model.HoaDon;
import cinema.ban_ve.model.KhachHang;
import cinema.ban_ve.model.SuatChieu;
import cinema.ban_ve.model.VeXemPhim;
import cinema.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BanVeCtrl {
    private final VeDAO veDAO;
    private final HoaDonDAO hdDAO;
    private final KhachHangDAO khDAO;
    private final SuatChieuDAO suatChieuDAO;

    private String thongBaoLoi;

    public BanVeCtrl() {
        this.veDAO = new VeDAO();
        this.hdDAO = new HoaDonDAO();
        this.khDAO = new KhachHangDAO();
        this.suatChieuDAO = new SuatChieuDAO();
    }

    public BanVeCtrl(VeDAO veDAO, HoaDonDAO hdDAO, KhachHangDAO khDAO, SuatChieuDAO suatChieuDAO) {
        this.veDAO = veDAO;
        this.hdDAO = hdDAO;
        this.khDAO = khDAO;
        this.suatChieuDAO = suatChieuDAO;
    }

    public List<SuatChieu> layDanhSachSuatChieu() {
        return suatChieuDAO.layDanhSachSuatChieu();
    }

    public List<VeXemPhim> laySoDoGhe(String idLichChieu) {
        if (idLichChieu == null) return null;
        return veDAO.layGheCuaLichChieu(idLichChieu);
    }

    public KhachHang traCuuKHTT(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
            return null;
        }
        return khDAO.traCuuKHTT(sdt.trim());
    }

    public double tinhTienDonHang(List<VeXemPhim> dsVeSelected, double discount) {
        if (dsVeSelected == null || dsVeSelected.isEmpty()) {
            return 0.0;
        }
        double tongGiaVe = dsVeSelected.stream().mapToDouble(VeXemPhim::getGiaVe).sum();
        return Math.max(0, tongGiaVe - discount);
    }

    public double tinhTongGiaVe(List<VeXemPhim> dsVeSelected) {
        if (dsVeSelected == null || dsVeSelected.isEmpty()) return 0.0;
        return dsVeSelected.stream().mapToDouble(VeXemPhim::getGiaVe).sum();
    }

    public boolean thanhToanVaXuatVe(HoaDon hd, List<VeXemPhim> dsVeSelected, KhachHang kh, int diemDoiVe) {
        if (hd == null || dsVeSelected == null || dsVeSelected.isEmpty()) {
            this.thongBaoLoi = "Dữ liệu hóa đơn hoặc danh sách vé trống!";
            return false;
        }

        Connection conn = DBConnection.getConnection();
        try {

            if (conn != null) {
                conn.setAutoCommit(false);
            }

            hdDAO.luuHoaDon(hd, conn);

            veDAO.capNhatTrangThai(dsVeSelected, hd.getIdHoaDon(), conn);

            if (kh != null) {

                int diemCongMoi = (int) (hd.getTongThanhToan() / 10000);
                int diemMoi = Math.max(0, kh.getDiemTichLuy() - diemDoiVe + diemCongMoi);
                khDAO.capNhatDiemTichLuy(kh.getIdKH(), diemMoi, conn);
                kh.setDiemTichLuy(diemMoi);
            }

            if (conn != null) {
                conn.commit();
            }
            this.thongBaoLoi = null;
            return true;

        } catch (SQLException | RuntimeException ex) {

            this.thongBaoLoi = ex.getMessage();
            System.err.println("Giao dịch thất bại: " + ex.getMessage() + ". Đang rollback...");
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Lỗi khi rollback: " + rollbackEx.getMessage());
                }
            }
            return false;
        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public String inHoaDonVaVe(HoaDon hd, List<VeXemPhim> dsVe, SuatChieu suatChieu, KhachHang kh) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        sb.append("========================================================\n");
        sb.append("              RẠP CHIẾU PHIM VIỆT CINEMA                \n");
        sb.append("              HÓA ĐƠN THANH TOÁN TIỀN VÉ                \n");
        sb.append("========================================================\n");
        sb.append(String.format("Mã hóa đơn  : %s\n", hd.getIdHoaDon()));
        sb.append(String.format("Thời gian   : %s\n", hd.getNgayLap().format(fmt)));
        sb.append(String.format("Thu ngân    : %s\n", hd.getNhanVienLap()));
        if (kh != null) {
            sb.append(String.format("Khách hàng  : %s (%s - Hạng %s)\n",
                    kh.getHoTen(), kh.getSoDienThoai(), kh.getLoaiKH()));
            sb.append(String.format("Điểm KHTT   : %d điểm\n", kh.getDiemTichLuy()));
        } else {
            sb.append("Khách hàng  : Khách vãng lai\n");
        }
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("%-18s %-8s %-12s %12s\n", "Mã Vé", "Ghế", "Loại", "Đơn Giá"));
        sb.append("--------------------------------------------------------\n");
        for (VeXemPhim ve : dsVe) {
            sb.append(String.format("%-18s %-8s %-12s %,12.0f đ\n",
                    ve.getIdVe(), ve.getMaGhe(), ve.getLoaiGhe(), ve.getGiaVe()));
        }
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("Tổng tiền vé : %,38.0f đ\n", hd.getTongTienGoc()));
        sb.append(String.format("Giảm trừ     : %,38.0f đ\n", hd.getDiscount()));
        sb.append(String.format("THỰC TRẢ     : %,38.0f đ\n", hd.getTongThanhToan()));
        sb.append(String.format("Hình thức TT : %38s\n", hd.getPhuongThucTT()));
        sb.append("========================================================\n");
        sb.append("    Chúc quý khách có trải nghiệm xem phim vui vẻ!      \n");
        sb.append("========================================================\n\n");

        for (VeXemPhim ve : dsVe) {
            sb.append("********************************************************\n");
            sb.append("               VÉ XEM PHIM (CINEMA TICKET)              \n");
            sb.append("********************************************************\n");
            sb.append(String.format("Phim        : %s\n", suatChieu != null ? suatChieu.getTenPhim() : ""));
            sb.append(String.format("Phòng chiếu : %s\n", suatChieu != null ? suatChieu.getTenPhong() : ""));
            sb.append(String.format("Suất chiếu  : %s | Ngày: %s\n",
                    suatChieu != null ? suatChieu.getGioBatDau() : "",
                    suatChieu != null ? suatChieu.getNgayChieu() : ""));
            sb.append(String.format("Ghế         : %s (%s)\n", ve.getMaGhe(), ve.getLoaiGhe()));
            sb.append(String.format("Giá vé      : %,.0f đ\n", ve.getGiaVe()));
            sb.append(String.format("Mã vé       : %s\n", ve.getIdVe()));
            sb.append("********************************************************\n\n");
        }

        return sb.toString();
    }

    public String getThongBaoLoi() {
        return thongBaoLoi;
    }

    public VeDAO getVeDAO() {
        return veDAO;
    }

    public HoaDonDAO getHdDAO() {
        return hdDAO;
    }

    public KhachHangDAO getKhDAO() {
        return khDAO;
    }
}
