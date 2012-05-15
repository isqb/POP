public class GridSimulate extends Thread
{
	public static void main(String[] args) {
		World world = new World();
		world.makeGrid();

		ErlController erl = new ErlController(world);
                erl.run();
	}
}