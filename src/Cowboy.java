import java.awt.Image;
import javax.swing.ImageIcon;

public class Cowboy {
    int x;
    int y;
    ImageIcon image;
    String state;
    
    
    //måste ses över 
    public Cowboy() {
        this.x = x;
        this.y = y;
        this.image = image;
        this.state = state;
    }
    
    
    public Cowboy(int x, int y, ImageIcon image, String state) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.state = state;
    }

   
    public void setState(String state)
    {
        this.state = state;
    }

    public void setX(int x)
    {
    	this.x = x;
    }
    
    public void setY(int y)
    {
    	this.y = y;
    }
    
    public void setImage(ImageIcon img)
    {
        this.image = img;
    }

    public int getX()
    {
    	return x;
    }

    public int getY()
    {
    	return y;
    }
     
    public String getState()
    {
        return state;
    }

    public Image getImage()
    {
        
    	return image.getImage();
    }
    

    @Override
    public String toString()
    {
    	return "x: " + x + ", y: " + y + "state: " + state ;
    }
}