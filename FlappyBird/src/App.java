import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Menampilkan Start Menu terlebih dahulu
                new StartMenu().setVisible(true);
            }
        });
    }
}
