public class GridSimulate extends Thread
{
	public static void main(String[] args) {
		World world = new World();
		world.makeGrid();
		//Hashtable test = world.getCowboys();
		//PipedInputStream in = new PipedInputStream();
		//PipedOutputStream out = new PipedOutputStream();

		try {
			ErlController thread = new ErlController(world);
			new Thread(thread).start();
		} catch(Exception e) {
			System.out.println("fail ErlController: " + e);
		}

		while (true) {
			world.paint();
			try {
                                Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("fail simulation at: " + e);
			}
		}
	}
}