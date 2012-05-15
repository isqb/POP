import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Hashtable;

public class World extends JFrame implements KeyListener {
    private final int MAXCOWBOYS = 25;
    private int numberOfCowboys = 0;
    Hashtable cowboys = new Hashtable();

    private int gridX = 25;
    private int gridY = 25;
    private JFrame frame = new JFrame();
    private JPanel[][] grid = new JPanel[gridX][gridY];

    private ErlController erl;

    public void setGridX(int x) {
	gridX = x;
    }

    public void setGridY(int y) {
	gridY = y;
    }

    public Hashtable getCowboys() {
        return cowboys;
    }

    public void setErlController(ErlController erl) {
        this.erl = erl;
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = World.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public void makeGrid() {
	frame.setLayout(new GridLayout(gridX, gridY));
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(640, 640);
        frame.addKeyListener(this);
        frame.setFocusable(true);
	frame.pack();
	frame.setVisible(true);

	for (int x=0; x<gridX; x++) {
            for (int y=0; y<gridY; y++) {
		grid[y][x] = new JPanel();
		frame.add(grid[y][x]);
            }
	}	
    }

    public void paint() {
        frame.repaint();
	for (int x=0; x<gridX; x++) {
            for (int y=0; y<gridY; y++) {
		grid[x][y].revalidate();
            }
	}
    }

    public void createCowboy(int pid, int x, int y, boolean isHuman) {
    	if (numberOfCowboys == MAXCOWBOYS) {
    		System.out.println("Too many cowboys already!!");
    		return;
    	}

        ImageIcon image;
        if (isHuman) {
            image = createImageIcon("cb.jpg");
        }
        else {
            image = createImageIcon("bg.jpg");
        }

    	Cowboy cb = new Cowboy(x, y, image);
    	grid[x][y].add(new JLabel(image));

        cowboys.put(pid, cb);
    	numberOfCowboys++;
    }

    public void move(Cowboy cowboy, int newX, int newY) {
    	int x = cowboy.getX();
    	int y = cowboy.getY();
    	grid[x][y].removeAll();
    	
    	cowboy.setX(newX);
    	cowboy.setY(newY); 
    	    	
    	ImageIcon image = cowboy.getImage();
    	grid[newX][newY].add(new JLabel(image));
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'w'  ||  c == 'a'  ||
            c == 's'  ||  c == 'd') {
            erl.move(Character.toString(c));
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getID() == e.VK_UP) {
            System.out.println(":D");
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}