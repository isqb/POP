import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.JFrame;

/**
 * Starts a thread with JPanel battle between 2 character objects with a KeyListener 
 * for the actions performed by the "human" character object.
 * 
 */




public class Battle extends JPanel implements Runnable, KeyListener {

    private boolean fight = false, shoot = false;
    private int nr = 0;
    private float walkTopCounter = 10.0f, walkCounter = 0f;
    private static final float WALKSPEED = 0.1f; // how quick you want to characters to walk
    private Random generator = new Random();
    private OtpErlangPid cowboyPID, monsterPID;
    private ErlController erl;
    private Cowboy cowboy;
    private Monster monster;

    
 
 //// Constructors////
    
    public Battle() {
        
        cowboy = new Cowboy(480,250,GridSimulate.createImageIcon("cowboyLeft.png"));
        monster = new Monster(520,257,GridSimulate.createImageIcon("monsterRight.png"));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.add(new JLabel(GridSimulate.createImageIcon("battle.jpg")));

    }

    public Battle(OtpErlangPid cowboyPID, OtpErlangPid monsterPID, ErlController erl) {
        this.erl = erl;
        this.cowboyPID = cowboyPID;
        this.monsterPID = monsterPID;
        this.cowboy = new Cowboy(480,250,GridSimulate.createImageIcon("cowboyLeft.png"));
        this.monster = new Monster(520,257,GridSimulate.createImageIcon("monsterRight.png"));
        this.setBackground(Color.BLACK);
        this.add(new JLabel(GridSimulate.createImageIcon("battle.jpg")));
        this.setFocusable(true);
        this.setDoubleBuffered(true);
    }

    
 /**
 *Initialize a thread with a new "battle" JFrame
 * 
 */
    @Override
    public void run() {
        JFrame battleFrame = new JFrame();
        battleFrame.setAlwaysOnTop(true);
        battleFrame.add(this);
        battleFrame.setTitle("Battle!!!");
        battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        battleFrame.setSize(1024, 320);
        battleFrame.setResizable(true);
        battleFrame.setVisible(true);
        addKeyListener(this);
        try {
            actionPerformed();
        } catch (InterruptedException ex) {
            Logger.getLogger(Battle.class.getName()).log(Level.SEVERE, null, ex);
        }
        battleFrame.dispose();
    }
    
 /**
 * Kills the erlang node(process) depending on "shoot" or when random generator equals "n".
 * 
 */

    public void fight() throws InterruptedException {
        fight = true;
        int n = 0;
        int random = generator.nextInt(30)+10;
        repaint();
        while (true) {
            Thread.sleep(100);

            if (shoot) {
                erl.kill(monsterPID, cowboyPID, this, monster, cowboy);
                break;
            } else if (n == random) {
                monster.setImage("LeftShoot.png");
                cowboy.setImage(GridSimulate.createImageIcon("grave.png"));
                repaint();
                Thread.sleep(2000);
                erl.kill(cowboyPID, monsterPID, this, cowboy, monster);
                break;
            }
            n++;
        }
    }

 /**
 *Paints graphics for the JPanel Battle
 * 
 *@param g 
 */
    
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(cowboy.getImage(), (int)cowboy.getX(), (int)cowboy.getY(), this);
        g2d.drawImage(monster.getImage(), (int)monster.getX(), (int)monster.getY(), this);
        if (fight) {
            g2d.drawImage(GridSimulate.createImageIcon("Shoot.png").getImage(), 400, 20, this); // sets the shoot image
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    
 /**
 *Simulates JPanel movement in Battle.
 * 
 */
    public void actionPerformed() throws InterruptedException {

        Thread.sleep(1500);
        while (true) {
            Thread.sleep(100);
            if (walkCounter < walkTopCounter - 0.1f) 
                {
                    cowboy.setImage("Left.png");
                    monster.setImage("Right.png");
                    walkCounter += WALKSPEED;
                    if (nr == 0) 
                        {
                            cowboy.setX(cowboy.getX() - 1);
                            monster.setX(monster.getX() + 1);
                            nr++;
                    } else if (nr == 1) 
                        {
                            cowboy.setX(cowboy.getX() - 1);
                            monster.setX(monster.getX() + 1);
                            cowboy.setY(cowboy.getY() + 1);
                            monster.setY(monster.getY() + 1);
                            nr++;
                        } else 
                            {
                            cowboy.setX(cowboy.getX() - 1);
                            monster.setX(monster.getX() + 1);
                            cowboy.setY(cowboy.getY() - 1);
                            monster.setY(monster.getY() - 1);
                            nr = 0;
                            }
            
                    
                    if (cowboy.getX() == 450 && monster.getX() == 550) {
                    cowboy.setImage("Right.png");
                    monster.setImage("Left.png");
                    repaint();
                    try {
                        fight();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Battle.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
            repaint();
        }
    }

    
 /**
 *
 * 
 */
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

 /**
 *Listens to keys pressed in JPanel Battle
 *@param e  
 */
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch(keyCode) {
            case KeyEvent.VK_SPACE:
                if (fight) {
                    shoot = true;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
