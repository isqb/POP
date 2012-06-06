package Game;

import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *	@author	Olof Björklund
 *	@author	Mark Tibblin
 *	@author	Luis Mauricio
 *	@author	Marcus Utter
 */

/**
 * Creates a 2-dimensional world with character objects for gaming interaction.
 *	
 */

public class World extends JPanel implements KeyListener  {
    
    
    private Hashtable chars = new Hashtable(); // Hasttable for charachter objects
    private boolean inBattle = false;
    private ErlController erl;
    private Battle battle;
    private JFrame battleFrame;
    
    //// Constructors////
    
    public World()
    {
      this.setBackground(Color.BLACK);
      this.setFocusable(true);
      this.setDoubleBuffered(true);
      this.add(new JLabel(SuddenImpact.createImageIcon("background.png")));
      this.addKeyListener(this);
    }


 /**
 * Paints the World JPanel according to what at the moment exists in Hashtable chars.
 * 
 * @param g
 */
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

 /**
 * Creates human or bot character and stores in Hasttable "chars".
 * 
 * 
 * @param pid - Process number for character declared from erlang nodes
 * @param x  - x coordinate
 * @param y  - y coordinate
 * @param isHuman - defines if bot or human character
 */
     
    public void createChar(int pid, double x, double y, boolean isHuman) {

        if (isHuman) {
            ImageIcon image = SuddenImpact.createImageIcon("cowboyDown.png");
            chars.put(pid, new Cowboy(x, y, image));
        }
        else {
            ImageIcon image = SuddenImpact.createImageIcon("monsterDown.png");
            chars.put(pid, new Monster(x, y, image));
        }

        this.repaint();
    }
    
    
 /**
 * Updates characters movements and images according to new x, y coordinates.
 * 
 * 
 * @param character 
 * @param newX 
 * @param newY 
 */
    
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
    	
    	
    }

/**
 * Initialize a new thread with a subclass Battle.
 *
 * @param cowboy 
 * @param monster
 */
    
    public void startBattle(OtpErlangPid cowboy, OtpErlangPid monster)
    {
        inBattle = true;
        battle = new Battle(cowboy, monster, erl);
        new Thread(battle).start();
    }
    
 /**
 * Sets the battle status to "false" and changes image of loser in battle.
 *
 * 
 * @param loser
 * @param humanBattle 
 */
    
    public void endBattle(OtpErlangPid loser, boolean humanBattle) {
        
        if (humanBattle) {
            
            inBattle = false;
        }
        Char ch = (Char)chars.get(loser.id());
        ch.setImage(SuddenImpact.createImageIcon("grave.png"));
    }
    
    ////get methods////
    
    public Hashtable getChars() { 
        return chars;
    }

    ////KeyListener  components////
    
    public void setErlController(ErlController erl) {
        this.erl = erl;
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