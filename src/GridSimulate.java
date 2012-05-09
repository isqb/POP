import com.ericsson.otp.erlang.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridSimulate extends Thread
{
        /*private static class ErlController implements Runnable
        {
                public void run()
                {
                    try {
                        OtpNode myListeningNode = new OtpNode("server");
                        OtpMbox myListeningMbox = myListeningNode.createMbox("moveServer");
                        OtpErlangObject object = null;
                        OtpErlangTuple msg;
                        OtpErlangPid from;
                        OtpErlangObject command;
                        while (true) {
                            try {
                                try {
                                    object = myListeningMbox.receive();
                                } catch (OtpErlangDecodeException ex) {
                                    Logger.getLogger(GridSimulate.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                System.out.println("WEE!!");
                                msg = (OtpErlangTuple) object;
                                from = (OtpErlangPid) msg.elementAt(0);
                                command = (OtpErlangObject) msg.elementAt(1);
                                OtpErlangAtom atom = (OtpErlangAtom) command;
                                String s = atom.atomValue();
                                if (s.equals("move")) {
                                    System.out.println("MOVE!!!");
                                }
                            } catch (OtpErlangExit e) {
                                System.out.println("error :P");
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(GridSimulate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        }*/

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