package Game;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *	@author	Olof Björklund
 *	@author	Mark Tibblin
 *	@author	Luis Mauricio
 *	@author	Marcus Utter
 */

/**
 * Super class for all in game character objects.
 * 
 */

public class Char {
    
    
    ////Parameters////
    double x;
    double y;
    ImageIcon image;

    
    ////Contructor////
    public Char(double x, double y, ImageIcon image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    
    ////set methods////
    
    public void setX(double x)
    {
    	this.x = x;
    }

    public void setY(double y)
    {
    	this.y = y;
    }
    
    public void setImage(ImageIcon img)
    {
        image = img;
    }

    public void setImage(String img)
    {
        image = SuddenImpact.createImageIcon(img);
    }

    
    ////get methods////
    
    public double getX()
    {
    	return x;
    }

    public double getY()
    {
    	return y;
    }

    public Image getImage()
    {
    	return image.getImage();
    }

    
    
    public String toString()
    {
    	return x + ", " + y;
    }
}