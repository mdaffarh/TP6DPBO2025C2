import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int frameWidth = 360;
    int frameHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

//    game over and score
    boolean isGameOver = false;
    int score = 0;
    JLabel scoreLabel;

//    player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;

    int playerWidth = 34;
    int playerHeight = 24;

    Player player;

//    pipes
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;

    int pipeWidth = 64;
    int pipeHeight = 512;

    ArrayList<Pipe> pipes;

//    timer
    Timer gameLoop;
    Timer pipeCooldown;

//    gravity
    int gravity = 1;


    public FlappyBird(){
        setPreferredSize(new Dimension(360, 640));
        setFocusable(true);
        addKeyListener(this);
//        score label
        setLayout(null);
        scoreLabel = new JLabel("0", SwingConstants.CENTER);
        scoreLabel.setBounds(0, 50, frameWidth, 70);
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel);

//        add font
        try {
            Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("assets/PressStart2P.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
            scoreLabel.setFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 16)); // fallback
        }
        showScoreLabelDuringGame();

//        setBackground(Color.blue);

//        load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        pipeCooldown = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pipe");
                placePipes();
            }
        });
        pipeCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes(){
        int randomPosY = (int) (pipeStartPosY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = frameHeight / 4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY + openingSpace + pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

//    menampilkan player
    public void draw(Graphics g){
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);
        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(),null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
    }

    public void move(){
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for (Pipe pipe : pipes) {
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            // tambah skor jika player melewati pipa atas dan belum tercatat
            if (!pipe.isPassed() && pipe.getImage() == upperPipeImage && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                pipe.setPassed(true);
                score++;
                scoreLabel.setText(String.valueOf(score));
            }
        }


//        menambahkan skor
//        buat arraylist untuk pipa yang akan dihapus/terlewati
        ArrayList<Pipe> pipesToRemove = new ArrayList<>();
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            // hapus pipa yang keluar layar
            if (pipe.getPosX() + pipe.getWidth() < 0) {
                pipesToRemove.add(pipe);
            }
        }
        pipes.removeAll(pipesToRemove);

    }

//    check nubruk
    public boolean checkCollision() {
//        buat rectangle untuk player
        Rectangle playerRect = new Rectangle(player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight());
//        buat rectangle untuk tiap pipa
        for (Pipe pipe : pipes) {
            Rectangle pipeRect = new Rectangle(pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight());
//            cek apakah player nubruk pipa, jika iya return true
            if (playerRect.intersects(pipeRect)) {
                return true;
            }
        }

//        kondisi keluar layar/jatuh
        if (player.getPosY() + player.getHeight() >= frameHeight) {
            return true;
        }
        return false;
    }

    public void showScoreLabelDuringGame() {
        scoreLabel.setBounds(0, 50, frameWidth, 50); // Kiri atas
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(16f));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setText(String.valueOf(score));
    }

    public void showScoreLabelGameOver() {
        scoreLabel.setBounds(0, frameHeight / 2 - 60, frameWidth, 100); // Di tengah
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(18f));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setText("<html><center style='background-color:#d8ac55; padding: 10px 5px;'>Game Over!<br>Press R to Restart<br>Score: " + score + "</center></html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        move();
//        repaint();
//        belum game over jalankan move()
        if (!isGameOver) {
            move();
//            jika nabrak set game over dan stop
            if (checkCollision()) {
                isGameOver = true;
                gameLoop.stop();
                pipeCooldown.stop();
                showScoreLabelGameOver();
            }
            repaint();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

//    untuk restart game
    public void restartGame() {
//        kembalikan state dan skor ke awal
        isGameOver = false;
        score = 0;
        scoreLabel.setText("0");
        showScoreLabelDuringGame();

        player.setPosY(playerStartPosY);
        player.setVelocityY(0);

        pipes.clear();

        gameLoop.start();
        pipeCooldown.start();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !isGameOver) {
            player.setVelocityY(-10);
        }

//        restart
        if (e.getKeyCode() == KeyEvent.VK_R && isGameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
