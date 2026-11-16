package cinema.phim;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SuaPhimFrm extends JFrame {
    private Phim phim;
    private PhimCtrl phimCtrl;

    private JTextField txtId, txtMaPhim, txtTenPhim, txtTheLoai, txtThoiLuong, txtDaoDien, txtDienVien, txtNamSanXuat;
    private JTextArea txtMoTa;
    private JButton btnCapNhat, btnHuy;
    private Border defaultBorder;

    public SuaPhimFrm(Phim phim) {
        this.phim = phim;
        this.phimCtrl = new PhimCtrl();

        setTitle("Sửa Thông Tin Phim");
        setSize(550, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(20);
        txtId.setEditable(false);
        txtId.setBackground(new Color(235, 235, 235));

        txtMaPhim = new JTextField(20);
        txtTenPhim = new JTextField(20);
        txtTheLoai = new JTextField(20);
        txtThoiLuong = new JTextField(20);
        txtDaoDien = new JTextField(20);
        txtDienVien = new JTextField(20);
        txtNamSanXuat = new JTextField(20);
        defaultBorder = txtThoiLuong.getBorder();

        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);

        addFormRow(formPanel, gbc, 0, "ID:", txtId);
        addFormRow(formPanel, gbc, 1, "Mã phim (*):", txtMaPhim);
        addFormRow(formPanel, gbc, 2, "Tên phim (*):", txtTenPhim);
        addFormRow(formPanel, gbc, 3, "Thể loại (*):", txtTheLoai);
        addFormRow(formPanel, gbc, 4, "Thời lượng (phút) (*):", txtThoiLuong);
        addFormRow(formPanel, gbc, 5, "Đạo diễn (*):", txtDaoDien);
        addFormRow(formPanel, gbc, 6, "Diễn viên (*):", txtDienVien);
        addFormRow(formPanel, gbc, 7, "Mô tả:", scrollMoTa);
        addFormRow(formPanel, gbc, 8, "Năm sản xuất (*):", txtNamSanXuat);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        btnCapNhat = new JButton("Cập nhật");
        btnHuy = new JButton("Hủy");
        btnCapNhat.setPreferredSize(new Dimension(110, 35));
        btnHuy.setPreferredSize(new Dimension(110, 35));

        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnHuy);
        add(buttonPanel, BorderLayout.SOUTH);

        hienThiThongTinCu();

        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        btnHuy.addActionListener(e -> xuLyHuy());
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void hienThiThongTinCu() {
        txtId.setText(String.valueOf(phim.getId()));
        txtMaPhim.setText(phim.getMaPhim());
        txtTenPhim.setText(phim.getTenPhim());
        txtTheLoai.setText(phim.getTheLoai());
        txtThoiLuong.setText(String.valueOf(phim.getThoiLuong()));
        txtDaoDien.setText(phim.getDaoDien());
        txtDienVien.setText(phim.getDienVien());
        txtMoTa.setText(phim.getMoTa());
        txtNamSanXuat.setText(String.valueOf(phim.getNamSanXuat()));
    }

    private void xuLyCapNhat() {
        txtThoiLuong.setBorder(defaultBorder);
        txtNamSanXuat.setBorder(defaultBorder);

        JTextField[] requiredFields = {txtMaPhim, txtTenPhim, txtTheLoai, txtThoiLuong, txtDaoDien, txtDienVien, txtNamSanXuat};
        for (JTextField field : requiredFields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng nhập đầy đủ các thông tin bắt buộc của phim!",
                    "Lỗi nhập liệu",
                    JOptionPane.ERROR_MESSAGE
                );
                field.requestFocus();
                return;
            }
        }

        int thoiLuong = 0;
        int namSanXuat = 0;
        boolean invalidFormat = false;
        Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);

        try {
            thoiLuong = Integer.parseInt(txtThoiLuong.getText().trim());
            if (thoiLuong <= 0) {
                txtThoiLuong.setBorder(redBorder);
                invalidFormat = true;
            }
        } catch (NumberFormatException ex) {
            txtThoiLuong.setBorder(redBorder);
            invalidFormat = true;
        }

        try {
            namSanXuat = Integer.parseInt(txtNamSanXuat.getText().trim());
            if (namSanXuat <= 0) {
                txtNamSanXuat.setBorder(redBorder);
                invalidFormat = true;
            }
        } catch (NumberFormatException ex) {
            txtNamSanXuat.setBorder(redBorder);
            invalidFormat = true;
        }

        if (invalidFormat) {
            JOptionPane.showMessageDialog(
                this,
                "Định dạng dữ liệu không hợp lệ! Thời lượng và Năm sản xuất phải là số nguyên dương.",
                "Lỗi định dạng",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        phim.setMaPhim(txtMaPhim.getText().trim());
        phim.setTenPhim(txtTenPhim.getText().trim());
        phim.setTheLoai(txtTheLoai.getText().trim());
        phim.setThoiLuong(thoiLuong);
        phim.setDaoDien(txtDaoDien.getText().trim());
        phim.setDienVien(txtDienVien.getText().trim());
        phim.setMoTa(txtMoTa.getText().trim());
        phim.setNamSanXuat(namSanXuat);

        if (phimCtrl.capNhatPhim(phim)) {
            JOptionPane.showMessageDialog(
                this,
                "Cập nhật thông tin phim thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE
            );
            new QuanLyPhimFrm().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Cập nhật thất bại! Vui lòng kiểm tra kết nối CSDL hoặc trùng mã phim.",
                "Lỗi hệ thống",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void xuLyHuy() {
        Object[] options = {"Đồng ý", "Không"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Bạn có chắc chắn muốn hủy các thay đổi hiện tại không?",
            "Xác nhận hủy",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]
        );

        if (choice == JOptionPane.YES_OPTION) {
            new TimPhimFrm().setVisible(true);
            dispose();
        }
    }
}