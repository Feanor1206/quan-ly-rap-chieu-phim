package cinema.ban_ve;

import cinema.ban_ve.model.HoaDon;
import cinema.ban_ve.model.KhachHang;
import cinema.ban_ve.model.SuatChieu;
import cinema.ban_ve.model.VeXemPhim;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanVeFrm extends JFrame {
    private final BanVeCtrl ctrl;

    private SuatChieu suatChieuHienTai;
    private KhachHang khachHangHienTai;
    private List<VeXemPhim> dsVeCuaSuatChieu = new ArrayList<>();
    private final List<VeXemPhim> dsVeSelected = new ArrayList<>();

    public static final Color COLOR_TRONG = Color.WHITE;
    public static final Color COLOR_DANG_CHON = new Color(254, 240, 138);
    public static final Color COLOR_DA_BAN = new Color(239, 68, 68);

    private static final Color BORDER_REGULAR = new Color(156, 163, 175);
    private static final Color BORDER_VIP = new Color(234, 88, 12);
    private static final Color BORDER_COUPLE = new Color(219, 39, 119);

    private JComboBox<SuatChieuItem> cbSuatChieu;
    private JLabel lblThongTinSuat;
    private JPanel pnlMaTranGhe;
    private final Map<String, JButton> mapButtonGhe = new HashMap<>();

    private JTable tblVeChon;
    private DefaultTableModel modelTableVe;

    private JTextField txtSdtKHTT;
    private JButton btnTraCuuKHTT;
    private JLabel lblTenKHTT;
    private JLabel lblDiemKHTT;
    private JCheckBox chkDoiDiem;
    private JSpinner spnSoVeDoiDiem;

    private JLabel lblTongTienGoc;
    private JLabel lblGiamTru;
    private JLabel lblSoTienThucTra;
    private JComboBox<String> cbPhuongThucTT;
    private JButton btnXacNhanThanhToan;
    private JButton btnHuyChon;

    public BanVeFrm() {
        this.ctrl = new BanVeCtrl();
        khoiTaoGiaoDien();
        taiDanhSachSuatChieu();
    }

    public BanVeFrm(BanVeCtrl ctrl) {
        this.ctrl = ctrl;
        khoiTaoGiaoDien();
        taiDanhSachSuatChieu();
    }

    private void khoiTaoGiaoDien() {
        setTitle("3.3.2. Giao diện Chọn ghế và suất chiếu (BanVeFrm)");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(15, 23, 42));
        pnlHeader.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ BÁN VÉ RẠP CHIẾU PHIM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Thu ngân: NV01 - Nguyễn Thu Hà | Phiên làm việc: Hôm nay");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(203, 213, 225));
        pnlHeader.add(lblSub, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
        pnlMain.setBorder(new EmptyBorder(5, 12, 10, 12));

        JPanel pnlKhuVucA = taoKhuVucChonGheVaSuatChieu();
        JPanel pnlKhuVucB = taoKhuVucHoaDonVaKHTT();

        pnlMain.add(pnlKhuVucA, BorderLayout.CENTER);
        pnlMain.add(pnlKhuVucB, BorderLayout.EAST);

        add(pnlMain, BorderLayout.CENTER);
    }

    private JPanel taoKhuVucChonGheVaSuatChieu() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel pnlTopSuat = new JPanel(new BorderLayout(8, 8));
        pnlTopSuat.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "CHỌN SUẤT CHIẾU THEO THỜI GIAN THỰC",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 13)
        ));

        cbSuatChieu = new JComboBox<>();
        cbSuatChieu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbSuatChieu.addActionListener(e -> onChonSuatChieu());

        lblThongTinSuat = new JLabel("Đang tải suất chiếu...");
        lblThongTinSuat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblThongTinSuat.setForeground(new Color(51, 65, 85));
        lblThongTinSuat.setBorder(new EmptyBorder(4, 4, 4, 4));

        pnlTopSuat.add(cbSuatChieu, BorderLayout.NORTH);
        pnlTopSuat.add(lblThongTinSuat, BorderLayout.CENTER);
        panel.add(pnlTopSuat, BorderLayout.NORTH);

        JPanel pnlScreenAndSeats = new JPanel(new BorderLayout(8, 8));
        pnlScreenAndSeats.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "SƠ ĐỒ MA TRẬN GHẾ PHÒNG CHIẾU",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 13)
        ));

        JPanel pnlScreen = new JPanel();
        pnlScreen.setBackground(new Color(241, 245, 249));
        pnlScreen.setPreferredSize(new Dimension(400, 36));
        pnlScreen.setBorder(new LineBorder(new Color(100, 116, 139), 2, true));
        JLabel lblScreenText = new JLabel("════════════════ MÀN HÌNH CHIẾU (SCREEN) ════════════════");
        lblScreenText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblScreenText.setForeground(new Color(71, 85, 105));
        pnlScreen.add(lblScreenText);
        pnlScreenAndSeats.add(pnlScreen, BorderLayout.NORTH);

        pnlMaTranGhe = new JPanel();
        JScrollPane scrollGhe = new JScrollPane(pnlMaTranGhe);
        scrollGhe.setBorder(null);
        pnlScreenAndSeats.add(scrollGhe, BorderLayout.CENTER);

        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 6));
        pnlLegend.add(taoLegendItem(COLOR_TRONG, BORDER_REGULAR, "Ghế Regular (Trống - Trắng)"));
        pnlLegend.add(taoLegendItem(COLOR_TRONG, BORDER_VIP, "Ghế VIP (Viền Cam)"));
        pnlLegend.add(taoLegendItem(COLOR_TRONG, BORDER_COUPLE, "Ghế Couple (Viền Hồng)"));
        pnlLegend.add(taoLegendItem(COLOR_DANG_CHON, Color.BLACK, "Đang chọn (Vàng)"));
        pnlLegend.add(taoLegendItem(COLOR_DA_BAN, Color.BLACK, "Đã bán (Đỏ)"));
        pnlScreenAndSeats.add(pnlLegend, BorderLayout.SOUTH);

        panel.add(pnlScreenAndSeats, BorderLayout.CENTER);

        return panel;
    }

    private JPanel taoLegendItem(Color bg, Color border, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        JLabel box = new JLabel("    ");
        box.setOpaque(true);
        box.setBackground(bg);
        box.setBorder(new LineBorder(border, 2));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        p.add(box);
        p.add(lbl);
        return p;
    }

    private JPanel taoKhuVucHoaDonVaKHTT() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(380, 700));

        JPanel pnlBangVe = new JPanel(new BorderLayout(5, 5));
        pnlBangVe.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "BẢNG CHI TIẾT VÉ ĐÃ CHỌN",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 13)
        ));
        pnlBangVe.setPreferredSize(new Dimension(360, 160));

        modelTableVe = new DefaultTableModel(new String[]{"Ghế", "Loại", "Đơn giá (đ)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblVeChon = new JTable(modelTableVe);
        tblVeChon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblVeChon.setRowHeight(22);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        tblVeChon.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        tblVeChon.getColumnModel().getColumn(1).setCellRenderer(centerRender);

        JScrollPane scrollTable = new JScrollPane(tblVeChon);
        pnlBangVe.add(scrollTable, BorderLayout.CENTER);
        panel.add(pnlBangVe);

        panel.add(Box.createVerticalStrut(10));

        JPanel pnlKHTT = new JPanel();
        pnlKHTT.setLayout(new BoxLayout(pnlKHTT, BoxLayout.Y_AXIS));
        pnlKHTT.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "KHÁCH HÀNG THÂN THIẾT (KHTT)",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 13)
        ));

        JPanel pnlInputSdt = new JPanel(new BorderLayout(6, 4));
        txtSdtKHTT = new JTextField();
        txtSdtKHTT.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnTraCuuKHTT = new JButton("Tra cứu");
        btnTraCuuKHTT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTraCuuKHTT.addActionListener(e -> onTraCuuKHTT());

        pnlInputSdt.add(new JLabel("SĐT KHTT: "), BorderLayout.WEST);
        pnlInputSdt.add(txtSdtKHTT, BorderLayout.CENTER);
        pnlInputSdt.add(btnTraCuuKHTT, BorderLayout.EAST);
        pnlKHTT.add(pnlInputSdt);

        pnlKHTT.add(Box.createVerticalStrut(6));

        lblTenKHTT = new JLabel("Khách hàng: Khách vãng lai");
        lblTenKHTT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDiemKHTT = new JLabel("Điểm tích lũy: 0 điểm");
        lblDiemKHTT.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlKHTT.add(lblTenKHTT);
        pnlKHTT.add(lblDiemKHTT);

        pnlKHTT.add(Box.createVerticalStrut(8));

        JPanel pnlDoiDiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        chkDoiDiem = new JCheckBox("Đổi 500 điểm = 1 vé 2D chuẩn");
        chkDoiDiem.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkDoiDiem.setEnabled(false);
        chkDoiDiem.addActionListener(e -> onToggleDoiDiem());

        spnSoVeDoiDiem = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
        spnSoVeDoiDiem.setEnabled(false);
        spnSoVeDoiDiem.addChangeListener(e -> onCapNhatSoVeDoiDiem());

        pnlDoiDiem.add(chkDoiDiem);
        pnlDoiDiem.add(spnSoVeDoiDiem);
        pnlKHTT.add(pnlDoiDiem);

        panel.add(pnlKHTT);

        panel.add(Box.createVerticalStrut(10));

        JPanel pnlThanhToan = new JPanel();
        pnlThanhToan.setLayout(new BoxLayout(pnlThanhToan, BoxLayout.Y_AXIS));
        pnlThanhToan.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "BẢNG TỔNG THANH TOÁN",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 13)
        ));

        lblTongTienGoc = new JLabel("0 đ");
        lblTongTienGoc.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        lblGiamTru = new JLabel("0 đ");
        lblGiamTru.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblGiamTru.setForeground(new Color(220, 38, 38));

        lblSoTienThucTra = new JLabel("0 đ");
        lblSoTienThucTra.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSoTienThucTra.setForeground(new Color(16, 185, 129));

        cbPhuongThucTT = new JComboBox<>(new String[]{"TIỀN MẶT", "CHUYỂN KHOẢN (QR)", "QUẸT THẺ"});
        cbPhuongThucTT.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pnlThanhToan.add(taoRowThanhToan("Tổng tiền gốc:", lblTongTienGoc));
        pnlThanhToan.add(Box.createVerticalStrut(6));
        pnlThanhToan.add(taoRowThanhToan("Giảm trừ (Ưu đãi/Điểm):", lblGiamTru));
        pnlThanhToan.add(Box.createVerticalStrut(6));
        pnlThanhToan.add(new JSeparator());
        pnlThanhToan.add(Box.createVerticalStrut(6));
        pnlThanhToan.add(taoRowThanhToan("SỐ TIỀN THỰC TRẢ:", lblSoTienThucTra));
        pnlThanhToan.add(Box.createVerticalStrut(10));
        pnlThanhToan.add(taoRowThanhToan("Hình thức TT:", cbPhuongThucTT));

        panel.add(pnlThanhToan);

        panel.add(Box.createVerticalStrut(12));

        btnXacNhanThanhToan = new JButton("XÁC NHẬN THANH TOÁN & XUẤT VÉ");
        btnXacNhanThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXacNhanThanhToan.setBackground(new Color(16, 185, 129));
        btnXacNhanThanhToan.setForeground(Color.WHITE);
        btnXacNhanThanhToan.setFocusPainted(false);
        btnXacNhanThanhToan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnXacNhanThanhToan.addActionListener(e -> onXacNhanThanhToanVaXuatVe());

        btnHuyChon = new JButton("Hủy chọn lại");
        btnHuyChon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHuyChon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnHuyChon.addActionListener(e -> onHuyChonLai());

        panel.add(btnXacNhanThanhToan);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnHuyChon);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel taoRowThanhToan(String title, Component comp) {
        JPanel p = new JPanel(new BorderLayout(5, 2));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void taiDanhSachSuatChieu() {
        cbSuatChieu.removeAllItems();
        List<SuatChieu> ds = ctrl.layDanhSachSuatChieu();
        for (SuatChieu sc : ds) {
            cbSuatChieu.addItem(new SuatChieuItem(sc));
        }
        if (cbSuatChieu.getItemCount() > 0) {
            cbSuatChieu.setSelectedIndex(0);
        }
    }

    private void onChonSuatChieu() {
        SuatChieuItem item = (SuatChieuItem) cbSuatChieu.getSelectedItem();
        if (item == null) return;

        this.suatChieuHienTai = item.getSuatChieu();
        this.dsVeSelected.clear();

        lblThongTinSuat.setText(String.format(
                "<html><b>Phim:</b> %s (%s) | <b>Thời lượng:</b> %d phút | <b>Phòng:</b> %s | <b>Giờ:</b> %s - %s | <b>Giá vé chuẩn:</b> %,.0f đ</html>",
                suatChieuHienTai.getTenPhim(), suatChieuHienTai.getGioiHanTuoi(),
                suatChieuHienTai.getThoiLuong(), suatChieuHienTai.getTenPhong(),
                suatChieuHienTai.getGioBatDau(), suatChieuHienTai.getGioKetThuc(),
                suatChieuHienTai.getGiaVeCoBan()
        ));

        this.dsVeCuaSuatChieu = ctrl.laySoDoGhe(suatChieuHienTai.getIdLichChieu());

        hienThiSoDoGhe(this.dsVeCuaSuatChieu);
        capNhatBangVeVaTongTien();
    }

    public void hienThiSoDoGhe(List<VeXemPhim> dsVe) {
        pnlMaTranGhe.removeAll();
        mapButtonGhe.clear();

        if (suatChieuHienTai == null || dsVe == null) return;

        int soHang = suatChieuHienTai.getSoHang();
        int soCot = suatChieuHienTai.getSoCot();

        pnlMaTranGhe.setLayout(new GridLayout(soHang, soCot, 6, 6));
        pnlMaTranGhe.setBorder(new EmptyBorder(12, 12, 12, 12));

        Map<String, VeXemPhim> mapVe = new HashMap<>();
        for (VeXemPhim v : dsVe) {
            mapVe.put(v.getMaGhe(), v);
        }

        for (int r = 0; r < soHang; r++) {
            char hangChar = (char) ('A' + r);
            for (int c = 1; c <= soCot; c++) {
                String maGhe = String.format("%c%d", hangChar, c);
                VeXemPhim ve = mapVe.get(maGhe);

                JButton btnGhe = new JButton(maGhe);
                btnGhe.setFont(new Font("Segoe UI", Font.BOLD, 11));
                btnGhe.setMargin(new Insets(2, 2, 2, 2));
                btnGhe.setFocusPainted(false);

                if (ve != null) {

                    Color borderColor = "Couple".equalsIgnoreCase(ve.getLoaiGhe()) ? BORDER_COUPLE :
                                        "VIP".equalsIgnoreCase(ve.getLoaiGhe()) ? BORDER_VIP : BORDER_REGULAR;
                    btnGhe.setBorder(new LineBorder(borderColor, 2, true));

                    if (ve.isDaBan()) {

                        btnGhe.setBackground(COLOR_DA_BAN);
                        btnGhe.setForeground(Color.WHITE);
                        btnGhe.setEnabled(false);
                        btnGhe.setToolTipText("Ghế " + maGhe + " đã bán");
                    } else if (ve.isDangGiu()) {
                        btnGhe.setBackground(Color.GRAY);
                        btnGhe.setEnabled(false);
                        btnGhe.setToolTipText("Ghế " + maGhe + " đang được giữ");
                    } else {

                        btnGhe.setBackground(COLOR_TRONG);
                        btnGhe.setForeground(Color.BLACK);
                        btnGhe.setToolTipText(String.format("Ghế %s (%s) - %,.0f đ", maGhe, ve.getLoaiGhe(), ve.getGiaVe()));

                        btnGhe.addActionListener(e -> onToggleChonGhe(ve, btnGhe));
                    }
                }

                mapButtonGhe.put(maGhe, btnGhe);
                pnlMaTranGhe.add(btnGhe);
            }
        }

        pnlMaTranGhe.revalidate();
        pnlMaTranGhe.repaint();
    }

    private void onToggleChonGhe(VeXemPhim ve, JButton btnGhe) {
        if (dsVeSelected.contains(ve)) {

            dsVeSelected.remove(ve);
            btnGhe.setBackground(COLOR_TRONG);
            btnGhe.setForeground(Color.BLACK);
        } else {

            dsVeSelected.add(ve);
            btnGhe.setBackground(COLOR_DANG_CHON);
            btnGhe.setForeground(Color.BLACK);
        }
        capNhatBangVeVaTongTien();
    }

    private void onTraCuuKHTT() {
        String sdt = txtSdtKHTT.getText().trim();
        if (sdt.isEmpty()) {
            this.khachHangHienTai = null;
            lblTenKHTT.setText("Khách hàng: Khách vãng lai");
            lblDiemKHTT.setText("Điểm tích lũy: 0 điểm");
            chkDoiDiem.setSelected(false);
            chkDoiDiem.setEnabled(false);
            spnSoVeDoiDiem.setEnabled(false);
            capNhatBangVeVaTongTien();
            return;
        }

        KhachHang kh = ctrl.traCuuKHTT(sdt);
        if (kh != null) {
            this.khachHangHienTai = kh;
            lblTenKHTT.setText(String.format("<html>Khách hàng: <b>%s</b> (Hạng %s)</html>", kh.getHoTen(), kh.getLoaiKH()));
            lblDiemKHTT.setText(String.format("<html>Điểm tích lũy: <b style='color:#0284c7;'>%d điểm</b></html>", kh.getDiemTichLuy()));

            if (kh.coTheDoiVe()) {
                chkDoiDiem.setEnabled(true);
                int maxVeDoi = kh.getSoVeCoTheDoi();
                spnSoVeDoiDiem.setModel(new SpinnerNumberModel(1, 1, maxVeDoi, 1));
                JOptionPane.showMessageDialog(this,
                        String.format("Khách hàng %s có %d điểm (đủ điều kiện đổi %d vé 2D tiêu chuẩn)!",
                                kh.getHoTen(), kh.getDiemTichLuy(), maxVeDoi),
                        "Thông tin KHTT", JOptionPane.INFORMATION_MESSAGE);
            } else {
                chkDoiDiem.setSelected(false);
                chkDoiDiem.setEnabled(false);
                spnSoVeDoiDiem.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        String.format("Tìm thấy KHTT: %s (Điểm: %d - Chưa đủ 500 điểm để đổi vé).",
                                kh.getHoTen(), kh.getDiemTichLuy()),
                        "Thông tin KHTT", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            this.khachHangHienTai = null;
            lblTenKHTT.setText("<html><font color='red'>Không tìm thấy KHTT!</font></html>");
            lblDiemKHTT.setText("Điểm tích lũy: 0 điểm");
            chkDoiDiem.setSelected(false);
            chkDoiDiem.setEnabled(false);
            spnSoVeDoiDiem.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy khách hàng với số điện thoại này! Mặc định là khách vãng lai.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
        }

        capNhatBangVeVaTongTien();
    }

    private void onToggleDoiDiem() {
        spnSoVeDoiDiem.setEnabled(chkDoiDiem.isSelected());
        capNhatBangVeVaTongTien();
    }

    private void onCapNhatSoVeDoiDiem() {
        if (chkDoiDiem.isSelected()) {
            capNhatBangVeVaTongTien();
        }
    }

    private void capNhatBangVeVaTongTien() {

        modelTableVe.setRowCount(0);
        for (VeXemPhim ve : dsVeSelected) {
            modelTableVe.addRow(new Object[]{
                    ve.getMaGhe(),
                    ve.getLoaiGhe(),
                    String.format("%,.0f", ve.getGiaVe())
            });
        }

        double tongGoc = ctrl.tinhTongGiaVe(dsVeSelected);
        double discount = 0.0;

        if (chkDoiDiem.isSelected() && khachHangHienTai != null && !dsVeSelected.isEmpty()) {
            int soVeDoi = (Integer) spnSoVeDoiDiem.getValue();

            soVeDoi = Math.min(soVeDoi, dsVeSelected.size());
            double giaVeChuan = suatChieuHienTai != null ? suatChieuHienTai.getGiaVeCoBan() : 80000.0;
            discount = soVeDoi * giaVeChuan;
        }

        double soTienThucTra = ctrl.tinhTienDonHang(dsVeSelected, discount);

        lblTongTienGoc.setText(String.format("%,.0f đ", tongGoc));
        lblGiamTru.setText(String.format("- %,.0f đ", discount));
        lblSoTienThucTra.setText(String.format("%,.0f đ", soTienThucTra));
    }

    private void onXacNhanThanhToanVaXuatVe() {
        if (dsVeSelected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double tongGoc = ctrl.tinhTongGiaVe(dsVeSelected);
        double discount = 0.0;
        int soVeDoi = 0;
        int diemDoiVe = 0;

        if (chkDoiDiem.isSelected() && khachHangHienTai != null) {
            soVeDoi = Math.min((Integer) spnSoVeDoiDiem.getValue(), dsVeSelected.size());
            double giaVeChuan = suatChieuHienTai != null ? suatChieuHienTai.getGiaVeCoBan() : 80000.0;
            discount = soVeDoi * giaVeChuan;
            diemDoiVe = soVeDoi * 500;
        }

        double thucTra = ctrl.tinhTienDonHang(dsVeSelected, discount);

        int opt = JOptionPane.showConfirmDialog(this,
                String.format("Xác nhận thanh toán %,.0f đ cho %d vé đã chọn?\n(Giảm trừ: %,.0f đ%s)",
                        thucTra, dsVeSelected.size(), discount,
                        diemDoiVe > 0 ? " | Đổi: " + diemDoiVe + " điểm" : ""),
                "Xác nhận thanh toán", JOptionPane.YES_NO_OPTION);

        if (opt != JOptionPane.YES_OPTION) return;

        String idHD = "HD" + System.currentTimeMillis();
        String phuongThucTT = (String) cbPhuongThucTT.getSelectedItem();
        HoaDon hd = new HoaDon(idHD, LocalDateTime.now(), tongGoc, discount, thucTra,
                phuongThucTT, khachHangHienTai != null ? khachHangHienTai.getIdKH() : null, "NV_QUAY");
        hd.setDanhSachVe(new ArrayList<>(dsVeSelected));

        boolean result = ctrl.thanhToanVaXuatVe(hd, dsVeSelected, khachHangHienTai, diemDoiVe);

        if (result) {

            inHoaDonVaVe(hd, new ArrayList<>(dsVeSelected));
        } else {

            String msgLoi = ctrl.getThongBaoLoi() != null ? "\nChi tiết: " + ctrl.getThongBaoLoi() : "";
            JOptionPane.showMessageDialog(this,
                    "Giao dịch thất bại! Đã hoàn nguyên giao dịch." + msgLoi,
                    "Lỗi Giao Dịch ACID", JOptionPane.ERROR_MESSAGE);

            if (suatChieuHienTai != null) {
                onChonSuatChieu();
            }
        }
    }

    public void inHoaDonVaVe(HoaDon hd, List<VeXemPhim> dsVeIn) {
        String banIn = ctrl.inHoaDonVaVe(hd, dsVeIn, suatChieuHienTai, khachHangHienTai);

        JTextArea txtPrint = new JTextArea(banIn);
        txtPrint.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtPrint.setEditable(false);
        JScrollPane sp = new JScrollPane(txtPrint);
        sp.setPreferredSize(new Dimension(580, 520));

        JOptionPane.showMessageDialog(this, sp, "MẪU IN VÉ XEM PHIM VÀ HÓA ĐƠN THANH TOÁN", JOptionPane.INFORMATION_MESSAGE);

        dsVeSelected.clear();
        chkDoiDiem.setSelected(false);
        if (suatChieuHienTai != null) {
            this.dsVeCuaSuatChieu = ctrl.laySoDoGhe(suatChieuHienTai.getIdLichChieu());
            hienThiSoDoGhe(this.dsVeCuaSuatChieu);
        }
        if (khachHangHienTai != null) {
            lblDiemKHTT.setText(String.format("<html>Điểm tích lũy: <b style='color:#0284c7;'>%d điểm</b></html>", khachHangHienTai.getDiemTichLuy()));
            chkDoiDiem.setEnabled(khachHangHienTai.coTheDoiVe());
        }
        capNhatBangVeVaTongTien();
    }

    private void onHuyChonLai() {
        dsVeSelected.clear();
        chkDoiDiem.setSelected(false);
        if (suatChieuHienTai != null) {
            hienThiSoDoGhe(this.dsVeCuaSuatChieu);
        }
        capNhatBangVeVaTongTien();
    }

    private static class SuatChieuItem {
        private final SuatChieu suatChieu;

        public SuatChieuItem(SuatChieu suatChieu) {
            this.suatChieu = suatChieu;
        }

        public SuatChieu getSuatChieu() {
            return suatChieu;
        }

        @Override
        public String toString() {
            return suatChieu.toString();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            BanVeFrm frm = new BanVeFrm();
            frm.setVisible(true);
        });
    }
}
