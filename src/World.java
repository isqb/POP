import java.awt.event.KeyEvent;
<<<<<<< HEAD
=======
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
>>>>>>> 66ceabb11f6d166e13fd4ecd4136ff3562686305
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.util.Hashtable;
import javax.swing.*;

<<<<<<< HEAD
public class World extends JPanel implements KeyListener {
    
    private final int MAXCOWBOYS = 25;
    private int numberOfCowboys = 0;
    Hashtable cowboys = new Hashtable();
    Cowboy[] cowboylist = new Cowboy[MAXCOWBOYS];
    
   
    public World()
    {
      this.setBackground(Color.BLACK);
      this.setFocusable(true);
      this.setDoubleBuffered(true);
      this.add(new JLabel(createImageIcon("background.png")));
      this.addKeyListener(this);
=======
public class World extends JFrame implements KeyListener, WindowListener {
    private int gridX = 25;
    private int gridY = 25;
    private JFrame frame = new JFrame();
    private JPanel[][] grid = new JPanel[gridX][gridY];
    private final int MAXCOWBOYS = 25;
    private int numberOfCowboys = 0;
    private Hashtable cowboys = new Hashtable();
    private ErlController erl;

    public void setGridX(int x) {
	gridX = x;
>>>>>>> 66ceabb11f6d166e13fd4ecd4136ff3562686305
    }
    
    
    private ErlController erl;

   

    public Hashtable getCowboys() {
        return cowboys;
    }

    public void setErlController(ErlController erl) {
        this.erl = erl;
    }

<<<<<<< HEAD
    

    @Override
    public void paint(Graphics g) 
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < numberOfCowboys; i++){
            Cowboy cb = cowboylist[i];
            g2d.drawImage(cb.getImage(), cb.getX(), cb.getY(), this);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
        
=======
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = World.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void makeGrid() {
	frame.setLayout(new GridLayout(gridX, gridY));
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(560, 930);
        frame.addKeyListener(this);
        frame.addWindowListener(this);
        frame.setFocusable(true);
	for (int x=0; x<gridX; x++) {
            for (int y=0; y<gridY; y++) {
		grid[y][x] = new JPanel();
		frame.add(grid[y][x]);
            }
	}
	frame.setVisible(true);
    }

    public void repaint() {
        frame.repaint();
	for (int x=0; x<gridX; x++) {
            for (int y=0; y<gridY; y++) {
		grid[x][y].revalidate();
            }
	}
>>>>>>> 66ceabb11f6d166e13fd4ecd4136ff3562686305
    }

    public void createCowboy(int pid, int x, int y, boolean isHuman) {
    	if (numberOfCowboys == MAXCOWBOYS) {
    		System.out.println("Too many cowboys already!!");
    		return;
    	}

        ImageIcon image;
        if (isHuman) {
            image = createImageIcon("cowboyDown.png");
        }
        else {
            image = createImageIcon("skeleton.png");
        }

    	cowboylist[numberOfCowboys] = new Cowboy(x, y, image,"standing");

        cowboys.put(pid, cowboylist[numberOfCowboys]);
    	numberOfCowboys++;
        this.repaint();
    }
    
    public void move(Cowboy cowboy, int newX, int newY) 
    {
    	// movement images
        /*
        if (state.equals("moveUpp"))
        {
            cowboy.setImage(createImageIcon("cowboyUpp.png"));
            cowboy.setY(newY);
        }else if (state.equals("moveDown"))
        {
            cowboy.setImage(createImageIcon("cowboyDown.png"));
            cowboy.setY(newY);
        }else if (state.equals("moveLeft"))
        {
            cowboy.setImage(createImageIcon("cowboyLeft.png"));
            cowboy.setX(newX);
        }else if (state.equals("moveRight"))
        {
            cowboy.setImage(createImageIcon("cowboyRight.png"));
            cowboy.setX(newX);
        }
    	*/
    	cowboy.setY(newY);
        cowboy.setX(newX);
        
    	this.repaint();    	
    	//Image image = cowboy.getImage();
    	
    }

    public void startBattle(Cowboy cb1,Cowboy cb2)
    {
        Battle battle = new Battle(cb1,cb2);
        JFrame battleFrame = new JFrame();
        battleFrame.setAlwaysOnTop(true);
        battleFrame.add(battle);
        battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        battle.addKeyListener(this);// 'this' orsakar att vi använder World keyListener
        battleFrame.setSize(1024, 320);
        battleFrame.setResizable(true);
        battleFrame.setVisible(true);
    }
        
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        
        if (c== 'o')
        {
            Cowboy cb1 = new Cowboy(499,250,createImageIcon("cowboyLeft.png"), "standing");
            Cowboy cb2 = new Cowboy(501,250,createImageIcon("cowboyRight.png"), "standing");
            this.startBattle(cb1,cb2);
        }
        else if (c == 'w'  ||  c == 'a'  ||
            c == 's'  ||  c == 'd') {

            erl.move(Character.toString(c));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                erl.move("up");
                break;
            case KeyEvent.VK_DOWN:
                erl.move("down");
                break;
            case KeyEvent.VK_LEFT:
                erl.move("left");
                break;
            case KeyEvent.VK_RIGHT:
                erl.move("right");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
<<<<<<< HEAD
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = World.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    
=======

    public void windowOpened(WindowEvent e) {
        System.out.println("Open that shit up!");
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("closing...");
        erl.close();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
>>>>>>> 66ceabb11f6d166e13fd4ecd4136ff3562686305
}