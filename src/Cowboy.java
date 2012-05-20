import java.awt.Image;
import javax.swing.ImageIcon;

public class Cowboy {
    int x;
    int y;
    ImageIcon image;
    //String state;
    
    
    //måste ses över 
    public Cowboy() {
        this.x = x;
        this.y = y;
        this.image = createImageIcon("cowboyLeft.png");
        
        
    }
    
    
    public Cowboy(int x, int y, ImageIcon image) {
        this.x = x;
        this.y = y;
        this.image = image;
        
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
    
    
    /*public void setState(String state)
    {
        this.state = state;
    }*/

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
    
    /*
    public String getState()
    {
        return state;
    }*/

    public Image getImage()
    {
        
    	return image.getImage();
    }
    
    
    
    
    @Override
    public String toString()
    {
    	return "x: " + x + ", y: " + y;
    }
}