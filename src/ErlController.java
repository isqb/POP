import com.ericsson.otp.erlang.*;
import java.util.Hashtable;

public class ErlController implements Runnable {
	private String nodeName = "sigui";
	private String mBoxName = "gui";
        private World world;
	private String cmd;

	//Constuctor
	public ErlController(String cmd) {
		this.cmd = cmd;
	}

	public ErlController(World world) {
                this.world = world;
	}

	public void run() {
		try {
			init();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

        public void init() throws Exception {
		OtpNode myListeningNode = new OtpNode(nodeName);
		OtpMbox myListeningMbox = myListeningNode.createMbox(mBoxName);

		OtpErlangObject object;
		OtpErlangTuple msg;
		OtpErlangPid from = null;
		OtpErlangLong x;
		OtpErlangLong y = new OtpErlangLong(0);


		while (true) {
			try {
				object = myListeningMbox.receive();
				msg = (OtpErlangTuple) object;

				if(object instanceof OtpErlangTuple) {
					msg = (OtpErlangTuple)object;
                                        
                                        from = (OtpErlangPid)(msg.elementAt(0));
                                        x = (OtpErlangLong)msg.elementAt(1);
                                        y = (OtpErlangLong)msg.elementAt(2);

                                        int pid = (int)from.id();
                                        int newX = (int)x.intValue();
                                        int newY = (int)y.intValue();
					System.out.println(from.id() + ", " + x + ", " + y);

                                        Hashtable cowboys = world.getCowboys();
                                        if (cowboys.containsKey(pid)) {
                                            world.move((Cowboy)cowboys.get(pid), newX, newY);
                                        }
                                        else {
                                            world.createCowboy(pid, newX, newY);
                                        }
				}
			} catch(OtpErlangExit e) {
				System.out.println("error: " + e);
			}
		}
	}
}