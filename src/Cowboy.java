import javax.swing.ImageIcon;

public class Cowboy extends Char {
    private String name = "cowboy";
    
    public Cowboy(double x, double y, ImageIcon image) {
        super(x, y, image);
    }

    public void setImage(String img) {
        setImage(GridSimulate.createImageIcon(name+img));
    }
}