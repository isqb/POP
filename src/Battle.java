
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author lokarburken
 */

public class Battle extends JPanel implements ActionListener{
    
    private static final Timer timer = new Timer(100, null);
   
    private int nr = 0;
    private float counter = 0f;
    private float topCounter =0.99f;
    private float walkTopCounter = 10.0f;
    private float walkCounter = 0f;
    private static final float fadeSpeed = 0.1f; // how quik you want to fade
    private static final float walkSpeed = 0.1f; // how quik you want to characters to walk
    Cowboy cowboy1 = new Cowboy();
    Cowboy cowboy2 = new Cowboy(); 
    
    
    
    public Battle()
    {
      
      this.setBackground(Color.BLACK);
      this.setFocusable(true);
      this.setDoubleBuffered(true);
      this.add(new JLabel(createImageIcon("battle.jpg")));
      timer.setInitialDelay(1000);
      timer.addActionListener(this);
      timer.start();
      
    }
    
    public Battle(Cowboy cb1, Cowboy cb2)
    {
      this.cowboy1 = cb1;
      this.cowboy2 = cb2;
      this.setBackground(Color.BLACK);
      this.setFocusable(true);
      this.setDoubleBuffered(true);
      this.add(new JLabel(createImageIcon("battle.jpg")));
      timer.setInitialDelay(1000);
      timer.addActionListener(this);
      timer.start();
      
    }
    
    
    
    protected static ImageIcon createImageIcon(String path) 
        {
            java.net.URL imgURL = World.class.getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        }
    
    
    
    
    @Override
        public void paint(Graphics g) 
        {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.drawImage(cowboy1.getImage(), cowboy1.getX(), cowboy1.getY(), this);
            g2d.drawImage(cowboy2.getImage(), cowboy2.getX(), cowboy2.getY(), this);
            

            Toolkit.getDefaultToolkit().sync();
            g.dispose();

        }
        
    @Override
    protected void paintComponent(Graphics g) 
    {
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, counter));
        g2d.setPaint(Color.blue);
       
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
       
        
        if (counter < topCounter - 0.1f) 
        {
            counter += fadeSpeed;
            
            
        }
        if(walkCounter < walkTopCounter - 0.1f)
            {
                cowboy1.setImage(createImageIcon("cowboyLeft.png"));
                cowboy2.setImage(createImageIcon("cowboyRight.png"));
                if ( nr == 0)
                {
                walkCounter += walkSpeed;
                cowboy1.setX(cowboy1.getX() - 1);
                cowboy2.setX(cowboy2.getX() + 1);
                nr++;
                }else if(nr ==1)
                {
                 walkCounter += walkSpeed;
                cowboy1.setX(cowboy1.getX() - 1);
                cowboy2.setX(cowboy2.getX() + 1);   
                cowboy1.setY(cowboy1.getY() + 1);
                cowboy2.setY(cowboy2.getY() + 1);
                nr++;
                }else
                {
                    walkCounter += walkSpeed;
                cowboy1.setX(cowboy1.getX() - 1);
                cowboy2.setX(cowboy2.getX() + 1);   
                cowboy1.setY(cowboy1.getY() - 1);
                cowboy2.setY(cowboy2.getY() - 1);
                nr = 0;
                }
                if(cowboy1.getX() == 400 && cowboy2.getX() == 600)
                {
                    cowboy1.setImage(createImageIcon("cowboyRight.png"));
                    cowboy2.setImage(createImageIcon("cowboyLeft.png"));
                }
                
                       
            }
        repaint();
    }
    
    
   
}
