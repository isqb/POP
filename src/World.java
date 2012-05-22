import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.*;

public class World extends JPanel implements KeyListener  {
    
    private Hashtable chars = new Hashtable();
    private boolean inBattle = false;
    private ErlController erl;
    private Battle battle;
    private JFrame battleFrame;
    
    public World()
    {
      this.setBackground(Color.BLACK);
      this.setFocusable(true);
      this.setDoubleBuffered(true);
      this.add(new JLabel(createImageIcon("Graphics/background.png")));
      this.addKeyListener(this);
    }

    public Hashtable getChars() {
        return chars;
    }

    public void setErlController(ErlController erl) {
        this.erl = erl;
    }
    
    @Override
    public void paint(Graphics g) 
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        Enumeration cbs = chars.elements();
        while (cbs.hasMoreElements()) {
            Char ch = (Char)cbs.nextElement();
            g2d.drawImage(ch.getImage(), (int)ch.getX(), (int)ch.getY(), this);
        }
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void createChar(int pid, double x, double y, boolean isHuman) {

        if (isHuman) {
            ImageIcon image = createImageIcon("Graphics/cowboyDown.png");
            chars.put(pid, new Cowboy(x, y, image));
        }
        else {
            ImageIcon image = createImageIcon("Graphics/monsterDown.png");
            chars.put(pid, new Monster(x, y, image));
        }

        this.repaint();
    }
    
    public void move(Char character, double newX, double newY)
    {
        if (newY < character.getY())
        {
            character.setImage(createImageIcon(character.getDir()+"Upp.png"));
            
        }else if (newY > character.getY())
        {
            character.setImage(createImageIcon(character.getDir()+"Down.png"));
            
        }else if (newX < character.getX())
        {
            character.setImage(createImageIcon(character.getDir()+"Left.png"));
            
        }else if (newX > character.getX())
        {
            character.setImage(createImageIcon(character.getDir()+"Right.png"));
            
        }
    	
    	character.setY(newY);
        character.setX(newX);

    	this.repaint();
    	//Image image = cowboy.getImage();
    	
    }

    public void startBattle(OtpErlangPid cowboy, OtpErlangPid monster)
    {
        inBattle = true;
        battle = new Battle(cowboy, monster, erl);
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
        Char cb = (Char)chars.get(loser.id());
        cb.setImage(createImageIcon("Graphics/grave.png"));
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