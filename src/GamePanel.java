import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 1000;
    static final int BLOCK_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/BLOCK_SIZE;
    int delay = 200; //how fast the snake moves - value updates as game progresses
    int size = 6; //initial size of the snake
    int foodctr;
    int foodX;
    int foodY;
    char direction = 'R'; //initial direction ['U', 'D', 'L', 'R']
    boolean running = false;
    Timer timer;
    Random random;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];


    public GamePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_WIDTH));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new SnakeKey());
        startGame();
    }

    public void startGame()
    {

    }

    public void paintComponent(Graphics g)
    {

    }

    public void draw(Graphics g)
    {

    }

    public void addFood()
    {

    }

    public void move()
    {

    }

    public void checkFood()
    {

    }

    public void checkCollisions()
    {

    }

    public void gameOver(Graphics g)
    {

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public class SnakeKey extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {

        }
    }
}
