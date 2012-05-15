import com.ericsson.otp.erlang.*;
import java.util.Hashtable;

public class ErlController implements Runnable {
	private String nodeName = "sigui";
	private String mBoxName = "gui";
        private World world;

	//Constuctor
	public ErlController() {

	}

	public ErlController(World world) {
                this.world = world;
                world.setErlController(this);
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
                System.out.println(dir);
        }

        public void init() throws Exception {
		OtpNode myListeningNode = new OtpNode(nodeName);
		OtpMbox myListeningMbox = myListeningNode.createMbox(mBoxName);

		OtpErlangObject object;
		OtpErlangTuple msg;
                OtpErlangAtom atom;
		OtpErlangPid from;
		OtpErlangLong x;
		OtpErlangLong y;

		while (true) {
			try {
				object = myListeningMbox.receive();

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