package Game;

import javax.swing.ImageIcon;

/**
 *	@author	Olof Björklund
 *	@author	Mark Tibblin
 *	@author	Luis Mauricio
 *	@author	Marcus Utter
 */

/**
 * Subclass for Char.
 * 
 */


public class Cowboy extends Char {
    private String name = "cowboy";
    
    ////Constructor////
    
    public Cowboy(double x, double y, ImageIcon image) {
        super(x, y, image);
    }

    ////set methods////
    public void setImage(String img) {
        setImage(SuddenImpact.createImageIcon(name+img));
    }
}