
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

public class GridSimulate extends Thread implements WindowListener
{
    private static ErlController erl;

    public GridSimulate() {
            World world = new World();
            JFrame frame = new JFrame();
            frame.add(world);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 580);
            frame.setResizable(true);
            frame.setVisible(true);
            frame.addWindowListener(this);
            erl = new ErlController(world);
            erl.run();
    }
    
    public static void main(String[] args) {
            new GridSimulate();
    }

    public void windowOpened(WindowEvent e) {
        System.out.println("Open that sh*t up!");
    }

    public void windowClosing(WindowEvent e) {
        erl.close();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}