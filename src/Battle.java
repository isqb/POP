
import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author lokarburken
 */
public class Battle extends JPanel implements Runnable, ActionListener {

    private static final Timer timer = new Timer(100, null);
    private int nr = 0;
    //private float counter = 0f;
    //private float topCounter = 0.99f;
    private float walkTopCounter = 10.0f;
    private float walkCounter = 0f;
    //private static final float fadeSpeed = 0.1f; // how quik you want to fade
    private static final float walkSpeed = 0.1f; // how quik you want to characters to walk
    Cowboy cowboy;
    Monster monster;
    private Random generator = new Random();
    private ErlController erl;
    private boolean fight;
    private OtpErlangPid cowboyPID;
    private OtpErlangPid monsterPID;
    boolean shoot;

    public Battle() {
        
        cowboy = new Cowboy(499,250,createImageIcon("Graphics/cowboyLeft.png"));
        monster = new Monster(501,250,createImageIcon("Graphics/monsterRight.png"));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.add(new JLabel(createImageIcon("Graphics/battle.jpg")));
        timer.setInitialDelay(1000);
        timer.addActionListener(this);
        timer.start();

    }

    public Battle(OtpErlangPid cowboyPID, OtpErlangPid monsterPID, ErlController erl) {
        this.erl = erl;
        this.cowboyPID = cowboyPID;
        this.monsterPID = monsterPID;
        this.cowboy = new Cowboy(499,250,createImageIcon("Graphics/cowboyLeft.png"));
        this.monster = new Monster(501,250,createImageIcon("Graphics/monsterRight.png"));
        this.cowboy.setX(480);
        this.cowboy.setY(250);
        this.monster.setX(520);
        this.monster.setY(255);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.add(new JLabel(createImageIcon("Graphics/battle.jpg")));
        timer.setInitialDelay(1000);
        timer.addActionListener(this);
        timer.start();
        this.shoot = false;
        this.fight = false;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public void setFight(boolean fight) {
        this.fight = fight;
    }

    public void run() {
        System.out.println("new thread!");
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

    public void fight() throws InterruptedException {
        int random;
        boolean bothAlive = true;

        while (bothAlive) {
            repaint();
            random = generator.nextInt(5000);
            //timer.setDelay(400);
            //Thread.sleep(400);
            if (shoot) {
                bothAlive = false;
                Thread.sleep(3000);
                erl.kill(monsterPID, cowboyPID);
            } else if (random == 1477) {
                bothAlive = false;
                Thread.sleep(3000);
                erl.kill(cowboyPID, monsterPID);
                System.out.println("You're dead.\nGame over...");
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(cowboy.getImage(), (int)cowboy.getX(), (int)cowboy.getY(), this);
        g2d.drawImage(monster.getImage(), (int)monster.getX(), (int)monster.getY(), this);
        if (fight == true) {
            g2d.drawImage(createImageIcon("Graphics/Shoot.png").getImage(), 400, 20, this); // sets the shoot image
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
/*
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, counter));
        g2d.setPaint(Color.blue);
    }
*/
    @Override
    public void actionPerformed(ActionEvent e) {

        /*if (counter < topCounter - 0.1f) {
            counter += fadeSpeed;

        }*/
        if (walkCounter < walkTopCounter - 0.1f) {
            cowboy.setImage(createImageIcon(cowboy.getDir()+"Left.png"));
            monster.setImage(createImageIcon(monster.getDir()+"Right.png"));
            walkCounter += walkSpeed;
            if (nr == 0) {
                cowboy.setX(cowboy.getX() - 1);
                monster.setX(monster.getX() + 1);
                nr++;
            } else if (nr == 1) {
                cowboy.setX(cowboy.getX() - 1);
                monster.setX(monster.getX() + 1);
                cowboy.setY(cowboy.getY() + 1);
                monster.setY(monster.getY() + 1);
                nr++;
            } else {
                cowboy.setX(cowboy.getX() - 1);
                monster.setX(monster.getX() + 1);
                cowboy.setY(cowboy.getY() - 1);
                monster.setY(monster.getY() - 1);
                nr = 0;
            }
            if (cowboy.getX() == 450 && monster.getX() == 550) {
                System.out.println("shoot!");
                setFight(true);
                cowboy.setImage(createImageIcon(cowboy.getDir()+"Right.png"));
                monster.setImage(createImageIcon(monster.getDir()+"Left.png"));
                repaint();
                try {
                    fight();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Battle.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        repaint();
    }
}
