import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Hashtable;
import javax.swing.*;

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
    }
    
    
    private ErlController erl;

   

    public Hashtable getCowboys() {
        return cowboys;
    }

    public void setErlController(ErlController erl) {
        this.erl = erl;
    }

    

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
        if (e.getID() == e.VK_UP) {
            System.out.println(":D");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = World.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    
}