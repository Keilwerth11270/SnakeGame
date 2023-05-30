import javax.swing.JFrame;

public class GameFrame extends JFrame
{
    public GameFrame()
    {
        this.add(new GamePanel()); //Game Panel is the main code
        this.setTitle("Snake Game"); //title
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //quits if you hit the X
        this.setResizable(false); //we will decide the size later on
        this.pack(); //packs all of the frame components into the screen
        this.setVisible(true); //visibility of frame
        this.setLocationRelativeTo(null); //places jframe in center of screen
    }
}
