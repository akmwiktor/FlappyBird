
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener{

    private final int WIDHT = 800, HEIGHT = 800;
    public static FlappyBird flappyBird;
    private Renderer renderer;
    private Rectangle bird;
    private ArrayList<Rectangle> columns;
    private int yMotion, ticks, score;
    private boolean gameOver, started;
    private Random rand;

    public FlappyBird() {
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();

        jFrame.add(renderer);
        jFrame.setTitle("Flappy Bird!");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(WIDHT, HEIGHT);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setResizable(false);
        jFrame.setVisible(true);

        bird = new Rectangle(WIDHT / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    private void addColumn(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start) {
            columns.add(new Rectangle(WIDHT + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDHT + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    private void painColumn(Graphics g, Rectangle column) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    private void jump() {
        if (gameOver) {
            bird = new Rectangle(WIDHT / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }
        if (!started) {
            started = true;
        } else {
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int speed = 10;
        ticks++;
        if (started) {

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 1;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle column : columns) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 &&
                        bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                }
                if (column.intersects(bird)) {
                    gameOver = true;
                    if (bird.x <= column.x) {
                        bird.x = column.x - bird.width;
                    } else {
                        if (column.y != 0) {
                            bird.y = column.y - bird.height;
                        } else if (bird.y < column.height) {
                            bird.y = column.height;
                        }
                    }
                }
            }

            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameOver = true;
            }

            if (bird.y + yMotion >= HEIGHT - 120) {
                bird.y = HEIGHT - 120 - bird.height;
            }

        }

        renderer.repaint();
    }

    public void repaint(Graphics g2g) {
        BufferedImage bufferedImage = new BufferedImage(WIDHT, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        //********************************************
        g.setColor(Color.BLUE.darker());
        g.fillRect(0,0, WIDHT, HEIGHT);

        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT - 120, WIDHT,120);

        g.setColor(Color.GREEN.darker());
        g.fillRect(0, HEIGHT - 120, WIDHT,20);

        g.setColor(Color.RED);
        g.fillRect(bird.x,bird.y,bird.width,bird.height);

        for (Rectangle column : columns) {
            painColumn(g, column);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1, 100));

        if (!started) {
            g.drawString("Click to start!", 75, HEIGHT / 2 - 50);
        }

        if (gameOver) {
            g.drawString("Game over", 75, HEIGHT / 2 - 50);
        }

        if (!gameOver && started) {
            g.drawString(String.valueOf(score), WIDHT / 2 - 25, 100);
        }
        //*******************************************
        Graphics2D g2dComponent = (Graphics2D) g2g;
        g2dComponent.drawImage(bufferedImage, null, 0, 0);
    }

    public static void main(String[] args) {

        flappyBird = new FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }
    }
}
