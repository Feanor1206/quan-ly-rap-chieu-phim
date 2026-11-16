package cinema.phim;

import javax.swing.SwingUtilities;

public class MainPhim {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuanLyPhimFrm().setVisible(true);
        });
    }
}