import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame {
    public StartMenu() {
        setTitle("Flapi Birb");
        setSize(360, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Set background image
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/assets/background.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // Panel untuk menampung elemen-elemen dengan GridBagLayout
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Judul di atas tombol
        JLabel titleLabel = new JLabel("Flappy Bird");
        try {
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("assets/PressStart2P.ttf")).deriveFont(30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
            titleLabel.setFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
            titleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // fallback
        }
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, gbc);

        // Memberi jarak antara judul dan tombol
        gbc.gridy = 1; // Pindahkan ke baris berikutnya
        panel.add(Box.createVerticalStrut(100), gbc);

        // Tombol Start Game
        JButton startButton = new JButton("Start");
        try {
            // Memuat font untuk tombol
            Font pixelFontButton = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("assets/PressStart2P.ttf")).deriveFont(16f);
            GraphicsEnvironment geButton = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geButton.registerFont(pixelFontButton);
            startButton.setFont(pixelFontButton);
        } catch (Exception e) {
            e.printStackTrace();
            startButton.setFont(new Font("Arial", Font.BOLD, 16)); // fallback
        }

        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(140, 40));
        startButton.setBackground(Color.green);
        startButton.setForeground(Color.WHITE);

        // Tombol Start Game ketika ditekan
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Menutup form StartMenu
                openGame(); // Membuka game FlappyBird
            }
        });

        gbc.gridy = 2; // Tempatkan tombol di bawah judul
        panel.add(startButton, gbc);

        // Menambahkan panel ke frame
        add(panel, BorderLayout.CENTER);
    }

    // Fungsi untuk membuka game FlappyBird
    private void openGame() {
        JFrame gameWindow = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();

        gameWindow.add(game);
        gameWindow.pack();
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);
    }
}
