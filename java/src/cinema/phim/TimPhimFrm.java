package cinema.phim;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TimPhimFrm extends JFrame {
    private JTextField txtTenPhim;
    private JButton btnTimKiem, btnQuayLai;
    private JTable tblDanhSachPhim;
    private DefaultTableModel tableModel;
    private PhimCtrl phimCtrl;
    private List<Phim> currentList;

    public TimPhimFrm() {
        phimCtrl = new PhimCtrl();
        currentList = new ArrayList<>();

        setTitle("Tìm Kiếm Phim Theo Tên");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        searchPanel.add(new JLabel("Tên phim:"));
        txtTenPhim = new JTextField(25);
        btnTimKiem = new JButton("Tìm kiếm");
        btnQuayLai = new JButton("Quay lại");

        searchPanel.add(txtTenPhim);
        searchPanel.add(btnTimKiem);
        searchPanel.add(btnQuayLai);
        add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Mã phim", "Tên phim", "Thể loại", "Thời lượng", "Đạo diễn", "Diễn viên", "Năm sản xuất"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDanhSachPhim = new JTable(tableModel);
        tblDanhSachPhim.setRowHeight(25);
        add(new JScrollPane(tblDanhSachPhim), BorderLayout.CENTER);

        JLabel lblHuongDan = new JLabel("  * Click chọn một bộ phim trong danh sách bên trên để tiến hành sửa thông tin.");
        lblHuongDan.setFont(new Font("Arial", Font.ITALIC, 12));
        add(lblHuongDan, BorderLayout.SOUTH);

        btnTimKiem.addActionListener(e -> xuLyTimKiem());
        txtTenPhim.addActionListener(e -> xuLyTimKiem());

        tblDanhSachPhim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblDanhSachPhim.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < currentList.size()) {
                    Phim selectedPhim = currentList.get(selectedRow);
                    new SuaPhimFrm(selectedPhim).setVisible(true);
                    dispose();
                }
            }
        });

        btnQuayLai.addActionListener(e -> {
            new QuanLyPhimFrm().setVisible(true);
            dispose();
        });
    }

    private void xuLyTimKiem() {
        String tuKhoa = txtTenPhim.getText();
        currentList = phimCtrl.timPhimTheoTen(tuKhoa);
        tableModel.setRowCount(0);

        if (currentList.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Không tìm thấy phim nào chứa từ khóa vừa nhập, vui lòng kiểm tra lại!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE
            );
            txtTenPhim.setText("");
            txtTenPhim.requestFocus();
            return;
        }

        for (Phim p : currentList) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getMaPhim(),
                p.getTenPhim(),
                p.getTheLoai(),
                p.getThoiLuong() + " phút",
                p.getDaoDien(),
                p.getDienVien(),
                p.getNamSanXuat()
            });
        }
    }
}