import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image snake;
    private Image apple;
    private int appleX;
    private int appleY;

    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];

    private int snakeSize;
    private Timer timer;
    private int timerInterval = 250;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    private boolean checkGame = true;

    public GameField()
    {
        setBackground(Color.BLACK);
        loadImages();
        startGame();
        addKeyListener(new KeyListener());
        setFocusable(true);
    }

    public void loadImages()
    {
        ImageIcon imageIconApple = new ImageIcon("apple.png");
        apple = imageIconApple.getImage();
        ImageIcon imageIconSnake = new ImageIcon("dot.png");
        snake = imageIconSnake.getImage();
    }

    public void startGame()
    {
        snakeSize = 3;
        for (int i = 0; i < snakeSize; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(timerInterval, this);
        timer.start();
        createApple();
    }

    public void createApple()
    {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame)
        {
            if (snakeSize == ALL_DOTS)
            {
                String winText = "You Win!";
                g.setColor(Color.white);
                g.drawString(winText, 125, SIZE / 2);
            }
            g.drawImage(apple, appleX, appleY, this);

            for (int i = 0; i < snakeSize; i++) {
                g.drawImage(snake, x[i], y[i], this);
            }
        }
        else
        {
            String finalString = "Game Over";
            g.setColor(Color.white);
            g.drawString(finalString, 125, SIZE / 2);
        }
    }

    public void move()
    {
        for (int i = snakeSize; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left)
        {
            x[0] -= DOT_SIZE;
        }
        if (right)
        {
            x[0] += DOT_SIZE;
        }
        if (up)
        {
            y[0] -= DOT_SIZE;
        }
        if (down)
        {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple()
    {
        if (x[0] == appleX && y[0] ==appleY)
        {
            snakeSize++;
            createApple();
            if (timerInterval >= 120)
            {
                timer.setDelay(timerInterval -= 5);
            }
            else if (timerInterval >= 100)
            {
                timer.setDelay(timerInterval -= 2.5);
            }
            else if (timerInterval > 50)
            {
                timer.setDelay(timerInterval -= 2);
            }
        }
    }

    public void checkCollision()
    {
        for (int i = snakeSize; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i])
            {
                inGame = false;
            }
        }

        if (x[0] > SIZE)
        {
            inGame = false;
        }
        if (x[0] < 0)
        {
            inGame = false;
        }
        if (y[0] > SIZE)
        {
            inGame = false;
        }
        if (y[0] < 0)
        {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame)
        {
            checkCollision();
            checkApple();
            move();
        }

        repaint();
    }

    class KeyListener extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right)
            {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left)
            {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down)
            {
                up = true;
                right = false;
                left = false;
            }
            if (key == KeyEvent.VK_DOWN && !up)
            {
                down = true;
                left = false;
                right = false;
            }

            if (checkGame == true && key == KeyEvent.VK_ESCAPE)
            {
                timer.stop();
                checkGame = false;
            }
            else if (checkGame == false && key == KeyEvent.VK_ESCAPE)
            {
                timer.start();
                checkGame = true;
            }
        }
    }
}
