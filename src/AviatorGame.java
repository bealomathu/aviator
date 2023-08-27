import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AviatorGame extends JPanel implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLANE_WIDTH = 50;
    private static final int PLANE_HEIGHT = 30;
    private static final int OBSTACLE_WIDTH = 20;
    private static final int OBSTACLE_HEIGHT = 50;
    private static final int OBSTACLE_SPEED = 5;

    private boolean isGameOver = false;
    private Timer timer;
    private int planeY = HEIGHT / 2;
    private List<Point> obstacles = new ArrayList<>();
    private int score = 0;

    public AviatorGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (!isGameOver) {
                        planeY -= 20;
                    }
                }
            }
        });

        timer = new Timer(20, this);
        timer.start();

        generateObstacle();
    }

    private void generateObstacle() {
        int y = new Random().nextInt(HEIGHT - OBSTACLE_HEIGHT);
        obstacles.add(new Point(WIDTH, y));
    }

    private void moveObstacles() {
        for (int i = obstacles.size() - 1; i >= 0; i--) {
            Point obstacle = obstacles.get(i);
            obstacle.x -= OBSTACLE_SPEED;

            if (obstacle.x + OBSTACLE_WIDTH < 0) {
                obstacles.remove(i);
                generateObstacle();
                score++;
            }
        }
    }

    private boolean checkCollision() {
        Rectangle planeBounds = new Rectangle(50, planeY, PLANE_WIDTH, PLANE_HEIGHT);
        for (Point obstacle : obstacles) {
            Rectangle obstacleBounds = new Rectangle(obstacle.x, obstacle.y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
            if (planeBounds.intersects(obstacleBounds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            moveObstacles();
            isGameOver = checkCollision();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(50, planeY, PLANE_WIDTH, PLANE_HEIGHT);

        g.setColor(Color.RED);
        for (Point obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);
        }

        if (isGameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Score: " + score, 10, 30);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Aviator Game");
        AviatorGame game = new AviatorGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
