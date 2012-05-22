import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;
import javax.swing.*;

public class World extends JPanel implements KeyListener  {
    
    private final int MAXCOWBOYS = 25;
    private int numberOfCowboys = 0;
    private Hashtable cowboys = new Hashtable();
    private Cowboy[] cowboylist = new Cowboy[MAXCOWBOYS];
    
    private boolean inBattle = false;
    Battle battle;
    JFrame battleFrame;
    
   
    public World()
    {
      this.setBackground(Color.BLACK);
      this.setFocusable(true);
      this.setDoubleBuffered(true);
      this.add(new JLabel(createImageIcon("Graphics/background.png")));
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
            image = createImageIcon("Graphics/cowboyDown.png");
        }
        else {
            image = createImageIcon("Graphics/skeleton.png");
        }

    	cowboylist[numberOfCowboys] = new Cowboy(x, y, image);

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

    public void startBattle(OtpErlangPid cowboy1, OtpErlangPid cowboy2, boolean humanBattle, ErlController erl)
    {
        inBattle = humanBattle;
        battle = new Battle(cowboy1,cowboy2, erl);
        battleFrame = new JFrame();
        battleFrame.setAlwaysOnTop(true);
        battleFrame.add(battle);
        battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        battle.addKeyListener(this);// 'this' orsakar att vi använder World keyListener
        battleFrame.setSize(1024, 320);
        battleFrame.setResizable(true);
        battleFrame.setVisible(true);
        new Thread(battle).start();
    }
    
    public void endBattle(OtpErlangPid loser, boolean humanBattle) {
        
        if (humanBattle) {
            
            inBattle = false;
        }
        cowboys.remove(loser.id());
        battleFrame.dispose();
    }
        
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if (c == 'w'  ||  c == 'a'  ||
            c == 's'  ||  c == 'd') {
            
            if (!inBattle) {
                erl.move(Character.toString(c));
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch(keyCode) {
            case KeyEvent.VK_SPACE:
                if (battle != null) {
                    battle.setShoot(true);
                }
                break;
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