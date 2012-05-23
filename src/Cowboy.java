import javax.swing.ImageIcon;

public class Cowboy extends Char {
    private String dir = "Graphics/cowboy";
    
    public Cowboy(double x, double y, ImageIcon image) {
        super(x, y, image);
    }

    public String getDir() {
        return dir;
    }
}