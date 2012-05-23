import javax.swing.ImageIcon;

public class Monster extends Char {
    private String name = "monster";

    public Monster(double x, double y, ImageIcon image) {
        super(x, y, image);
    }

    public void setImage(String img) {
        setImage(GridSimulate.createImageIcon(name+img));
    }
}