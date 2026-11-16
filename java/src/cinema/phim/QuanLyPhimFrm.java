package cinema.phim;

import java.awt.*;
import javax.swing.*;

public class QuanLyPhimFrm extends JFrame {

    public QuanLyPhimFrm() {
        setTitle("Hệ Thống Quản Lý Rạp Chiếu Phim - Quản Lý Phim");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topMenuPanel.setBackground(new Color(230, 240, 250));

        JButton btnMenuPhim = new JButton("Quản lý phim");
        JButton btnMenuPhong = new JButton("Quản lý phòng chiếu");
        JButton btnMenuSuat = new JButton("Quản lý suất chiếu");
        JButton btnDangXuat = new JButton("Đăng xuất");

        topMenuPanel.add(btnMenuPhim);
        topMenuPanel.add(btnMenuPhong);
        topMenuPanel.add(btnMenuSuat);
        topMenuPanel.add(btnDangXuat);
        add(topMenuPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        JButton btnSuaThongTinPhim = new JButton("Sửa thông tin phim");
        btnSuaThongTinPhim.setFont(new Font("Arial", Font.BOLD, 15));
        btnSuaThongTinPhim.setPreferredSize(new Dimension(220, 45));
        centerPanel.add(btnSuaThongTinPhim);
        add(centerPanel, BorderLayout.CENTER);

        btnSuaThongTinPhim.addActionListener(e -> {
            new TimPhimFrm().setVisible(true);
            dispose();
        });

        btnDangXuat.addActionListener(e -> System.exit(0));
    }
}