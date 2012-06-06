package Game;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *	@author	Olof Björklund
 *	@author	Mark Tibblin
 *	@author	Luis Mauricio
 *	@author	Marcus Utter
 */

/**
 * main class that starts the game by creating a thread of main 
 * implemented with WindowListener components for synchronization 
 * with erlang node.
 * 
 */

public class SuddenImpact extends Thread implements WindowListener
{
    private static ErlController erl;
    private static String imgDir = "Graphics/";

    ////Constructor////
    
    public SuddenImpact() {
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
            new SuddenImpact();
    }
/**
 * Creates a ImageIcon according to filename.
 * 
 * @param path - name of file e.g. "background.jpg"
 */
    protected static ImageIcon createImageIcon(String path) {
        path = imgDir + path;
        java.net.URL imgURL = World.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    ////WindowListener components////
    
    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("Open that sh*t up!");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        erl.close();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}