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
      this.add(new JLabel(GridSimulate.createImageIcon("background.png")));
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
            ImageIcon image = GridSimulate.createImageIcon("cowboyDown.png");
            chars.put(pid, new Cowboy(x, y, image));
        }
        else {
            ImageIcon image = GridSimulate.createImageIcon("monsterDown.png");
            chars.put(pid, new Monster(x, y, image));
        }

        this.repaint();
    }
    
    public void move(Char character, double newX, double newY)
    {
        if (newY < character.getY())
        {
            character.setImage("Upp.png");
            
        }else if (newY > character.getY())
        {
            character.setImage("Down.png");
            
        }else if (newX < character.getX())
        {
            character.setImage("Left.png");
            
        }else if (newX > character.getX())
        {
            character.setImage("Right.png");
            
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
        new Thread(battle).start();
    }
    
    public void endBattle(OtpErlangPid loser, boolean humanBattle) {
        
        if (humanBattle) {
            
            inBattle = false;
        }
        Char ch = (Char)chars.get(loser.id());
        ch.setImage(GridSimulate.createImageIcon("grave.png"));
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
}