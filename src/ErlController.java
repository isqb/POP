import com.ericsson.otp.erlang.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErlController implements Runnable {
	private String nodeName = "sigui";
	private String mBoxName = "gui";
        private OtpNode node;
        private OtpMbox mbox;
        private OtpErlangPid playerPID = null;
        private World world;

	//Constuctor
	public ErlController() {

	}

	public ErlController(World world) {
                this.world = world;
                world.setErlController(this);
                try {
                    node = new OtpNode(nodeName);
                    mbox = node.createMbox(mBoxName);
                } catch (IOException ex) {
                    Logger.getLogger(ErlController.class.getName()).log(Level.SEVERE, null, ex);
                }
	}

	public void run() {
		try {
			init();
		} catch(Exception e) {
			System.out.println("failed init: " + e);
		}
	}

        public void move(String direction) {
                OtpErlangAtom dir = new OtpErlangAtom(direction);
                mbox.send(playerPID, dir);
        }

        public void init() throws Exception {
		OtpErlangObject object;
		OtpErlangTuple msg;
                OtpErlangAtom atom;
		OtpErlangPid from;
		OtpErlangLong x;
		OtpErlangLong y;

		while (true) {
			try {
				object = mbox.receive();

				if(object instanceof OtpErlangTuple) {
					msg = (OtpErlangTuple)object;

                                        atom = (OtpErlangAtom)msg.elementAt(0);
                                        from = (OtpErlangPid)msg.elementAt(1);
                                        x = (OtpErlangLong)msg.elementAt(2);
                                        y = (OtpErlangLong)msg.elementAt(3);

                                        String a = (String)atom.atomValue();
                                        int pid = (int)from.id();
                                        int newX = (int)x.intValue();
                                        int newY = (int)y.intValue();

                                        Hashtable cowboys = world.getCowboys();
                                        boolean isHuman;
                                        if (a.equals("human")) {
                                            isHuman = true;
                                            playerPID = from;
                                        }
                                        else {
                                            isHuman = false;
                                        }
                                        
                                        if (cowboys.containsKey(pid)) {
                                            world.move((Cowboy)cowboys.get(pid), newX, newY);
                                        }
                                        else {
                                            world.createCowboy(pid, newX, newY, isHuman);
                                        }
                                        
                                        world.paint();
				}
			} catch(OtpErlangExit e) {
				System.out.println("error: " + e);
			}
		}
	}
}