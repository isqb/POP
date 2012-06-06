package Game;

import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Starts a thread of battle panel between 2 character objects with a 
 * KeyListener for the actions performed by the "human" character object.
 * 
 */




public class Battle extends JPanel implements Runnable, KeyListener {

    ////Parameters////
    
    private boolean fight = false, shoot = false;
    private boolean playerDead = false, monsterDead = false, test = false;
    private int nr = 0;
    private float walkTopCounter = 10.0f, walkCounter = 0f;
    private static final float WALKSPEED = 0.1f; //how quick you want to characters to walk
    private Random generator = new Random();
    private OtpErlangPid cowboyPID, monsterPID;
    private ErlController erl;
    private Cowboy cowboy;
    private Monster monster;

    
 
 ////Constructors////
    
    public Battle(boolean test) {
        
        cowboy = new Cowboy(480,250,SuddenImpact.createImageIcon("cowboyLeft.png"));
        monster = new Monster(520,257,SuddenImpact.createImageIcon("monsterRight.png"));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.add(new JLabel(SuddenImpact.createImageIcon("battle.jpg")));
        this.test = test;

    }

    public Battle(OtpErlangPid cowboyPID, OtpErlangPid monsterPID, ErlController erl) {
        this.erl = erl;
        this.cowboyPID = cowboyPID;
        this.monsterPID = monsterPID;
        this.cowboy = new Cowboy(480,250,SuddenImpact.createImageIcon("cowboyLeft.png"));
        this.monster = new Monster(520,257,SuddenImpact.createImageIcon("monsterRight.png"));
        this.setBackground(Color.BLACK);
        this.add(new JLabel(SuddenImpact.createImageIcon("battle.jpg")));
        this.setFocusable(true);
        this.setDoubleBuffered(true);
    }

 /**
 * Initialize a thread with a new "battle" JFrame
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
 * Kills the character that loses by sending a kill atom message to erlang 
 * process depending on "shoot" or "n".
 * 
 */

    public void fight() throws InterruptedException {
        fight = true;
        Char loser = null;
        int n = 0;
        int random = generator.nextInt(30)+10;
        while (true) {
            Thread.sleep(100);

            if (shoot) {
                if (test) {
                    monsterDead = true;
                    return;
                }
                cowboy.setImage("RightShoot.png");
                loser = monster;
                erl.kill(monsterPID, cowboyPID);
                break;
            } else if (n == random) {
                if (test) {
                    playerDead = true;
                    return;
                }
                monster.setImage("LeftShoot.png");
                loser = cowboy;
                erl.kill(cowboyPID, monsterPID);
                playerDead = true;
                break;
            }
            n++;
        }
        loser.setY(loser.getY() - 10);
        loser.setImage(SuddenImpact.createImageIcon("grave.png"));
        repaint();
        Thread.sleep(2000);
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
            g2d.drawImage(SuddenImpact.createImageIcon("Shoot.png").getImage(), 400, 20, this); // sets the shoot image
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
            
                    //shooting event
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
     * Mainly used for testing.
     * @return playerDead.
     */
    public boolean getPlayerDead() {
        return playerDead;
    }

    /**
     * Mainly used for testing.
     * @return monsterDead.
     */
    public boolean getMonsterDead() {
        return monsterDead;
    }

    /**
     * Mainly used for testing.
     * @return fight.
     */
    public boolean getFight() {
        return fight;
    }

    /**
     * Mainly used for testing.
     * @param shoot - Decides whether the player shoots the monster or not.
     */
    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }
    
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
