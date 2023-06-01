import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 1000;
    static final int BLOCK_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/BLOCK_SIZE;
    float delay = 200; //how fast the snake moves - value updates as game progresses
    int size = 10; //initial size of the snake
    int foodCounter;
    int foodX;
    int foodY;
    char direction = 'R'; //initial direction ['U', 'D', 'L', 'R']
    boolean running = false;
    Timer timer;
    Random random;

    String musicPath = "/resources/song.wav";
    Clip music;

    int[] x = new int[GAME_UNITS];
    int[] y = new int[GAME_UNITS];

    float F = 50; //for scaling the delay, F is the max food count we use (not actual max)


    public GamePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new SnakeKey());
        startGame();
    }

    public void resetGame()
    {
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];
        size = 10;
        delay = 200;
        foodCounter = 0;
        direction = 'R';
        super.paintComponent(this.getGraphics()); //reset the screen
        startGame();
    }

    public void startGame()
    {
        PlayMusic(musicPath);
        addFood(); //on game start we need to give snake some food
        running = true;
        timer = new Timer((int) delay, this);
        timer.start();
    }

    private void PlayMusic(String musicPath)
    {
        try
        {
            URL sound = getClass().getResource(musicPath);
            AudioInputStream audioInput = null;
            if (sound != null)
            {
                audioInput = AudioSystem.getAudioInputStream(sound);
            }
            music = AudioSystem.getClip();
            music.open(audioInput);
            music.start();
            music.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (Exception e)
        {
            System.out.println("Error with finding the file");
        }
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        if(running)
        {
            g.setColor(Color.green);
            g.fillOval(foodX, foodY, BLOCK_SIZE, BLOCK_SIZE);
            for (int i = 0; i < size; i++)
            {
                g.setColor(Color.white);
                g.fillRect(x[i], y[i], BLOCK_SIZE, BLOCK_SIZE);
            }
        }
        else
        {
            gameOver(g);
        }
    }

    public void updateSpeed()
    {
        float change = (1 - (float) Math.exp((float)-2.5 * ((F - foodCounter) / F))); //function f(x) = 1-e^(-kx)
        delay = (200 * change);
        if(delay <= 55)
        {
            timer.setDelay(55);
            return;
        }
        timer.setDelay((int) delay);
    }

    public void addFood()
    {
        foodX = random.nextInt((SCREEN_WIDTH/BLOCK_SIZE)) * BLOCK_SIZE;
        foodY = random.nextInt((SCREEN_HEIGHT/BLOCK_SIZE)) * BLOCK_SIZE;
    }

    public void move()
    {
        for(int i = size; i>0; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - BLOCK_SIZE;

            case 'D' -> y[0] = y[0] + BLOCK_SIZE;

            case 'L' -> x[0] = x[0] - BLOCK_SIZE;

            case 'R' -> x[0] = x[0] + BLOCK_SIZE;
        }
    }

    public void checkFood()
    {
        if((x[0] == foodX) && (y[0] == foodY))
        {
            size++;
            foodCounter++;
            addFood();
            if(delay > 55)
            {
                updateSpeed();
            }
        }
    }

    public void checkCollisions()
    {
        for(int i = size; i > 0; i--)
        {
            if((x[0] == x[i]) && (y[0] == y[i])) //head has collided with body
            {
                running = false;
                break;
            }
        }
        if(x[0] < 0) //left border
        {
            running = false;
        }
        if(x[0] > SCREEN_WIDTH) //right border
        {
            running = false;
        }
        if(y[0] < 0) //top border
        {
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT) //bottom border
        {
            running = false;
        }

        if(!running)
        {
            timer.stop();
            music.stop();
        }
    }

    public void gameOver(Graphics g)
    {
        g.setColor(Color.white);
        g.setFont(new Font("Georgia", Font.PLAIN, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        //display game over
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, (SCREEN_HEIGHT/2));
        //display score
        g.drawString("Score: " + foodCounter, (SCREEN_WIDTH - metrics.stringWidth("Score: " + foodCounter)) / 2, (g.getFont().getSize()));
        //restart message
        g.drawString("Press 'C' to Restart", (SCREEN_WIDTH - metrics.stringWidth("Press 'C' to Restart")) / 2, SCREEN_HEIGHT - (g.getFont().getSize()));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(running)
        {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class SnakeKey extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch (e.getExtendedKeyCode()) //WASD and Arrow keys both work
            {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A ->
                {
                    if(direction != 'R') //if statements make sure we cant input direction *into* the snake
                    {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D ->
                {
                    if(direction != 'L')
                    {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W ->
                {
                    if(direction != 'D')
                    {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S ->
                {
                    if(direction != 'U')
                    {
                        direction = 'D';
                    }
                }
                case KeyEvent.VK_C ->
                {
                    timer.stop(); //if we don't stop the timer the snake goes hyper speed
//                    the reason it will go hyper speed is as the number of timers increases at different times,
//                    the number of ticks in the overall program will also increase because the timer
//                    ticks are not synchronized with each other which means that each snake movement
//                    will occur faster and faster (because more ticks)
                    music.stop();


                    resetGame();
                }
            }
        }
    }
}
