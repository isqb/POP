
import com.ericsson.otp.erlang.OtpErlangPid;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class Battle extends JPanel implements ActionListener {

    private static final Timer timer = new Timer(100, null);
    private int nr = 0;
    private float counter = 0f;
    private float topCounter = 0.99f;
    private float walkTopCounter = 10.0f;
    private float walkCounter = 0f;
    private static final float fadeSpeed = 0.1f; // how quik you want to fade
    private static final float walkSpeed = 0.1f; // how quik you want to characters to walk
    Cowboy cowboy1;
    Cowboy cowboy2;
    private Random generator = new Random();
    private ErlController erl;
    private boolean fight;
    private OtpErlangPid cowboy1PID;
    private OtpErlangPid cowboy2PID;
    boolean shoot;

    public Battle() {
        
        cowboy1 = new Cowboy(499,250,createImageIcon("Graphics/cowboyLeft.png"));
        cowboy2 = new Cowboy(501,250,createImageIcon("Graphics/cowboyRight.png"));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.add(new JLabel(createImageIcon("Graphics/battle.jpg")));
        timer.setInitialDelay(1000);
        timer.addActionListener(this);
        timer.start();

    }

    public Battle(Cowboy cb1, OtpErlangPid cowboy1, Cowboy cb2, OtpErlangPid cowboy2) {
        this.cowboy1PID = cowboy1;
        this.cowboy2PID = cowboy2;
        this.cowboy1 = cb1;
        this.cowboy2 = cb2;
        this.cowboy1.setX(499);
        this.cowboy1.setY(250);
        this.cowboy2.setX(501);
        this.cowboy2.setY(250);
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

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = World.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void fight() {
        int random = generator.nextInt(30);
        boolean cowboyAlive = true;
        boolean mexicanAlive = true;


        while (cowboyAlive && mexicanAlive) {
            if (random < 2) {
                cowboyAlive = false;
                erl.kill(cowboy1PID, cowboy2PID);
            } else if (shoot == true) {
                mexicanAlive = false;
                erl.kill(cowboy2PID, cowboy1PID);
            }


        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(cowboy1.getImage(), cowboy1.getX(), cowboy1.getY(), this);
        g2d.drawImage(cowboy2.getImage(), cowboy2.getX(), cowboy2.getY(), this);
        //if (fight == true) {
            g2d.drawImage(createImageIcon("Graphics/Shoot.png").getImage(), 200, 200, this); // sets the shoot image
        //}


        Toolkit.getDefaultToolkit().sync();
        g.dispose();

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, counter));
        g2d.setPaint(Color.blue);

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if (counter < topCounter - 0.1f) {
            counter += fadeSpeed;


        }
        if (walkCounter < walkTopCounter - 0.1f) {
            cowboy1.setImage(createImageIcon("Graphics/cowboyLeft.png"));
            cowboy2.setImage(createImageIcon("Graphics/cowboyRight.png"));
            if (nr == 0) {
                walkCounter += walkSpeed;
                cowboy1.setX(cowboy1.getX() - 1);
                cowboy2.setX(cowboy2.getX() + 1);
                nr++;
            } else if (nr == 1) {
                walkCounter += walkSpeed;
                cowboy1.setX(cowboy1.getX() - 1);
                cowboy2.setX(cowboy2.getX() + 1);
                cowboy1.setY(cowboy1.getY() + 1);
                cowboy2.setY(cowboy2.getY() + 1);
                nr++;
            } else {
                walkCounter += walkSpeed;
                cowboy1.setX(cowboy1.getX() - 1);
                cowboy2.setX(cowboy2.getX() + 1);
                cowboy1.setY(cowboy1.getY() - 1);
                cowboy2.setY(cowboy2.getY() - 1);
                nr = 0;
            }
            if (cowboy1.getX() == 400 && cowboy2.getX() == 600) {
                cowboy1.setImage(createImageIcon("Graphics/cowboyRight.png"));
                cowboy2.setImage(createImageIcon("Graphics/cowboyLeft.png"));
            }
            if (walkCounter == walkTopCounter - 0.1f) {

                this.setFight(true);
                this.fight();

            }

        }
        repaint();
    }
}
