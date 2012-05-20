import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageTest extends JPanel {

  private Image img;
       
        
        ImageTest() {
                // read the background image
                ImageIcon icon = new ImageIcon("/Users/lokarburken/Skola/POP(Processorienteradprogrammering)/Projekt/ProjektPOP/POP/src/battle-back.png");
                // extract the image out of it
                img = icon.getImage();
        }
       
        // the paint method toi be overload
    @Override
        public void paint(Graphics g) {
                // draw the background image
                // here we use the simplest g.drawImage() method that draw as much of the image it can
                // multiple different drawImage() methods permit to shrink or extend the image
                g.drawImage(img, 0, 0, this);
                // we declare the component as not opaque so
                setOpaque(false);
                // the standard paint() method won't redraw the background
                super.paint(g);
                setOpaque(true);
    }
}
    




