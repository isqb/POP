import java.awt.Image;
import javax.swing.ImageIcon;

public class Char {
    double x;
    double y;
    ImageIcon image;

    public Char(double x, double y, ImageIcon image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public void setX(double x)
    {
    	this.x = x;
    }

    public void setY(double y)
    {
    	this.y = y;
    }

    public double getX()
    {
    	return x;
    }

    public double getY()
    {
    	return y;
    }

    public void setImage(ImageIcon img)
    {
        this.image = img;
    }

    public Image getImage()
    {
    	return image.getImage();
    }

    public String getDir() {
        return "";
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

    public String toString()
    {
    	return x + ", " + y;
    }
}