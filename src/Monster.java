import javax.swing.ImageIcon;

public class Monster extends Char {
    private String dir = "Graphics/monster";

    public Monster(double x, double y, ImageIcon image) {
        super(x, y, image);
    }

    public String getDir() {
        return dir;
    }
}