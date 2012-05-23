import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;
import javax.swing.JFrame;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * @author lokarburken
 */
public class Battle extends JPanel implements Runnable, KeyListener {

    private static final Timer timer = new Timer(100, null);
    private int nr = 0;
    private float walkTopCounter = 10.0f;
    private float walkCounter = 0f;
    private static final float walkSpeed = 0.1f; // how quick you want to characters to walk
    private Cowboy cowboy;
    private Monster monster;
    private Random generator = new Random();
    private ErlController erl;
    private OtpErlangPid cowboyPID, monsterPID;
    private boolean fight, shoot;

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
        this.shoot = false;
        this.fight = false;
    }

    public void run() {
        JFrame battleFrame = new JFrame();
        battleFrame.setAlwaysOnTop(true);
        battleFrame.add(this);
        battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        battleFrame.setSize(1024, 320);
        battleFrame.setResizable(true);
        battleFrame.setVisible(true);
        addKeyListener(this);
        setFocusable(true);
        setDoubleBuffered(true);
        try {
            actionPerformed();
        } catch (InterruptedException ex) {
            Logger.getLogger(Battle.class.getName()).log(Level.SEVERE, null, ex);
        }
        battleFrame.dispose();
    }

    public void fight() throws InterruptedException {
        fight = true;
        int n = 0;
        int random = generator.nextInt(30)+10;
        repaint();
        while (true) {
            Thread.sleep(100);

            if (shoot) {
                cowboy.setImage("RightShoot.png");
                monster.setImage(GridSimulate.createImageIcon("grave.png"));
                repaint();
                Thread.sleep(3000);
                erl.kill(monsterPID, cowboyPID);
                break;
            } else if (n == random) {
                monster.setImage("LeftShoot.png");
                cowboy.setImage(GridSimulate.createImageIcon("grave.png"));
                repaint();
                Thread.sleep(3000);
                erl.kill(cowboyPID, monsterPID);
                System.out.println("You're dead.\nGame over...");
                break;
            }
            n++;
        }
    }

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

    public void actionPerformed() throws InterruptedException {

        Thread.sleep(1500);
        while (true) {
            Thread.sleep(100);
            if (walkCounter < walkTopCounter - 0.1f) {
                cowboy.setImage("Left.png");
                monster.setImage("Right.png");
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

    public void keyTyped(KeyEvent e) {
    }

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

    public void keyReleased(KeyEvent e) {
    }
}
