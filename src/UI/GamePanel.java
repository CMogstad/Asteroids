package UI;

import Entities.Asteroid;
import Entities.Bullet;
import Entities.Spaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    public final int SCREEN_WIDTH = 600;
    public final int SCREEN_HEIGHT = 600;
    public final int UNIT_SIZE_ASTEROID = 40;
    public final int UNIT_SIZE_BULLET = 10;
    static final int DELAY = 0;

    MyKeyAdapter myKeyAdapter = new MyKeyAdapter();
    Timer timerGame;
    Random random;

    boolean running = false;
    int score;
    int lives;

    Spaceship spaceship;

    ArrayList<Asteroid> asteroids = new ArrayList<>();
    int[] xAsteroid = new int[UNIT_SIZE_ASTEROID];
    int[] yAsteroid = new int[UNIT_SIZE_ASTEROID];

    ArrayList<Bullet> bullets = new ArrayList<>();

    int speedSpaceship = 10;
    float timeUntilNextAsteroid = 0.0f;
    float timeUntilNextBullet = 0.0f;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setLayout(null);
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(myKeyAdapter);

        spaceship = new Spaceship();
        this.add(spaceship);

        startGame();
    }

    public void startGame() {
        running = true;
        setStartPositionSpaceship();
        lives = 3;
        score = 0;
        timerGame = new Timer(DELAY, this);
        timerGame.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (running) {
            for (Asteroid asteroid : asteroids) {
                graphics.setColor(Color.lightGray);
                graphics.fillOval(asteroid.getX(), asteroid.getY(), UNIT_SIZE_ASTEROID, UNIT_SIZE_ASTEROID);
            }

            for (Bullet bullet : bullets) {
                graphics.setColor(Color.red);
                graphics.fillOval(bullet.getX(), bullet.getY(), UNIT_SIZE_BULLET, UNIT_SIZE_BULLET);
            }

            drawCurrentLives(graphics);
            drawCurrentScore(graphics);

        } else {
            gameOver(graphics);
        }
    }

    private void drawCurrentScore(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Cambria", Font.BOLD, 24));
        graphics.drawString("Score: " + score, 20, 80);
    }

    private void drawCurrentLives(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Cambria", Font.BOLD, 24));
        graphics.drawString("Lives: " + lives, 20, 40);
    }

    private void setStartPositionSpaceship() {
        spaceship.setVisible(true);
        spaceship.setBounds(SCREEN_WIDTH / 2 - spaceship.getWidthSpaceship() / 2, SCREEN_HEIGHT - 150, 100, 100);
    }

    public void moveAsteroids() {
        boolean onScreen;

        for (int i = asteroids.size() - 1; i >= 0; i--) {
            onScreen = asteroids.get(i).moveOnePosition(SCREEN_HEIGHT);

            if (!onScreen) {
                asteroids.remove(asteroids.get(i));
                lives--;

                if (lives == 0) {
                    running = false;
                    break;
                }
            }
        }
    }

    public void moveBullets() {
        boolean onScreen;

        for (int i = bullets.size() - 1; i >= 0; i--) {
            onScreen = bullets.get(i).moveOnePosition();
            if (!onScreen) {
                bullets.remove(bullets.get(i));
            }
        }
    }

    public void createAsteroid() {
        Asteroid asteroid = new Asteroid(SCREEN_WIDTH, UNIT_SIZE_ASTEROID);
        asteroids.add(asteroid);
    }

    public void shoot(int xSpaceship, int ySpaceship) {
        Bullet bullet = new Bullet(xSpaceship, ySpaceship);
        bullets.add(bullet);
    }

    public void getAllXAsteroid(Asteroid asteroid) {
        for (int i = 0; i < UNIT_SIZE_ASTEROID; i++) {
            xAsteroid[i] = asteroid.getX() + i;
        }
    }

    public void getAllYAsteroid(Asteroid asteroid) {
        for (int i = 0; i < UNIT_SIZE_ASTEROID; i++) {
            yAsteroid[i] = asteroid.getY() + i;
        }
    }

    public void checkAsteroidCollision() {
        for (int i = asteroids.size() - 1; i >= 0; i--) {
            Asteroid asteroid = asteroids.get(i);

            if (checkXCollision(asteroid) && checkYCollision(asteroid)) {
                asteroids.remove(i);
                lives--;

                if (lives == 0) {
                    running = false;
                    break;
                }
            }
        }
    }

    public void checkHitTarget() {
        for (int i = asteroids.size() - 1; i >= 0; i--) {
            Asteroid asteroid = asteroids.get(i);

            for (int j = bullets.size() - 1; j >= 0; j--) {
                Bullet bullet = bullets.get(j);

                if (checkXHit(asteroid, bullet) && checkYHit(asteroid, bullet)) {
                    score++;
                    asteroids.remove(i);
                    bullets.remove(j);
                    break;
                }
            }
        }
    }

    private boolean checkXHit(Asteroid asteroid, Bullet bullet) {
        getAllXAsteroid(asteroid);

        for (int i = 0; i < UNIT_SIZE_ASTEROID; i++) {
            if (bullet.getX() <= xAsteroid[i] && xAsteroid[i] <= (bullet.getX() + UNIT_SIZE_BULLET)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkYHit(Asteroid asteroid, Bullet bullet) {
        getAllYAsteroid(asteroid);

        for (int i = 0; i < UNIT_SIZE_ASTEROID; i++) {
            if (bullet.getY() <= yAsteroid[i] && yAsteroid[i] <= (bullet.getY() + UNIT_SIZE_BULLET)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkXCollision(Asteroid asteroid) {
        getAllXAsteroid(asteroid);

        for (int i = 0; i < UNIT_SIZE_ASTEROID; i++) {
            if (spaceship.getX() <= xAsteroid[i] && xAsteroid[i] <= (spaceship.getX() + spaceship.getWidthSpaceship())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkYCollision(Asteroid asteroid) {
        getAllYAsteroid(asteroid);

        for (int i = 0; i < UNIT_SIZE_ASTEROID; i++) {
            if (spaceship.getY() <= yAsteroid[i] && yAsteroid[i] <= (spaceship.getY() + spaceship.getHeightSpaceship())) {
                return true;
            }
        }
        return false;
    }

    public void gameOver(Graphics graphics) {
        timerGame.stop();
        spaceship.setVisible(false);
        resetMyKeyAdapter();
        asteroids.clear();
        bullets.clear();

        drawFinalScore(graphics);
        drawGameOver(graphics);
        drawInstructionsNewGame(graphics);
    }

    private void drawInstructionsNewGame(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Cambria", Font.BOLD, 24));
        FontMetrics metrics3 = getFontMetrics(graphics.getFont());
        graphics.drawString("Push n to start a new game", (SCREEN_WIDTH - metrics3.stringWidth("Push n to start a new game")) / 2, SCREEN_HEIGHT / 2 + 50);
    }

    private void drawFinalScore(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Cambria", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + score, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + score)) / 2, 100);
    }

    private void drawGameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Cambria", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    private void resetMyKeyAdapter() {
        myKeyAdapter.leftHeld = false;
        myKeyAdapter.rightHeld = false;
        myKeyAdapter.upHeld = false;
        myKeyAdapter.downHeld = false;
        myKeyAdapter.spaceHeld = false;
    }

    private void tickInput() {
        int xMove = 0;
        int yMove = 0;

        if (myKeyAdapter.leftHeld) {
            xMove -= 1;
        }
        if (myKeyAdapter.rightHeld) {
            xMove += 1;
        }
        if (myKeyAdapter.upHeld) {
            yMove -= 1;
        }
        if (myKeyAdapter.downHeld) {
            yMove += 1;
        }
        if (myKeyAdapter.spaceHeld) {
            if (timeUntilNextBullet <= 0) {
                shoot(spaceship.getX() + spaceship.getPositionGunSpaceship(UNIT_SIZE_BULLET), spaceship.getY());
                timeUntilNextBullet = 0.5f;
            }
        }

        if (xMove != 0) {
            int move = xMove * speedSpaceship;

            //Left
            if (move < 0 && Math.abs(move) > spaceship.getX()) {
                move = -spaceship.getX();
            }

            //Right
            int maxMove = SCREEN_WIDTH - (spaceship.getX() + spaceship.getWidthSpaceship());
            if (move > 0 && move > maxMove) {
                move = maxMove;
            }
            spaceship.setLocation(spaceship.getX() + move, spaceship.getY());
        }

        if (yMove != 0) {
            int move = yMove * 10;

            //Down
            if (move < 0 && Math.abs(move) > spaceship.getY()) {
                move = -spaceship.getY();
            }

            //Up
            if (move > 0 && move > (SCREEN_HEIGHT - (spaceship.getY() + spaceship.getHeightSpaceship()))) {
                move = SCREEN_HEIGHT - (spaceship.getY() + spaceship.getHeightSpaceship());
            }

            spaceship.setLocation(spaceship.getX(), spaceship.getY() + move);
        }
    }

    long prevWhen = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        long when = e.getWhen();
        long deltaMs = when - prevWhen;
        prevWhen = when;
        float deltaSeconds = deltaMs / 1000f;

        if (running) {
            tickInput();
            moveAsteroids();
            moveBullets();
            checkAsteroidCollision();
            checkHitTarget();

            timeUntilNextBullet = timeUntilNextBullet - deltaSeconds;
            timeUntilNextAsteroid = timeUntilNextAsteroid - deltaSeconds;

            if (timeUntilNextAsteroid <= 0) {
                createAsteroid();
                timeUntilNextAsteroid = 1f;
            }
        }
        repaint();
    }

    private class MyKeyAdapter implements KeyListener {
        boolean leftHeld = false;
        boolean rightHeld = false;
        boolean upHeld = false;
        boolean downHeld = false;
        boolean spaceHeld = false;

        @Override
        public void keyTyped(KeyEvent e) {
            if (!running && e.getKeyChar() == 'n') {
                startGame();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (running) {
                switch (e.getKeyCode()) {
                    case 37 -> leftHeld = true;
                    case 38 -> upHeld = true;
                    case 39 -> rightHeld = true;
                    case 40 -> downHeld = true;
                    case 32 -> spaceHeld = true;
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (running) {
                switch (e.getKeyCode()) {
                    case 37 -> leftHeld = false;
                    case 38 -> upHeld = false;
                    case 39 -> rightHeld = false;
                    case 40 -> downHeld = false;
                    case 32 -> spaceHeld = false;
                }
            }
        }
    }
}
