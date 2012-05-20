
import javax.swing.JFrame;

public class GridSimulate extends Thread
{
    
    
	public static void main(String[] args) {
                World world = new World();
                JFrame frame = new JFrame();
                frame.add(world);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(560, 560);
                frame.setResizable(true);
                frame.setVisible(true);
                ErlController erl = new ErlController(world);
                erl.run();
	}
}