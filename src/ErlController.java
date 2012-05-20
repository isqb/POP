import com.ericsson.otp.erlang.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ErlController {
    private String nodeName = "sigui";
    private String mBoxName = "gui";
    private OtpNode node;
    private OtpMbox mbox;
    private OtpErlangPid playerPID = null;
    private World world;

	//Constuctor
    public ErlController() {}

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

        public void move(String direction) {
                OtpErlangAtom dir = new OtpErlangAtom(direction);
                OtpErlangObject[] o = new OtpErlangObject[]{new OtpErlangAtom("move"), dir};
                OtpErlangTuple tuple = new OtpErlangTuple(o);
                mbox.send(playerPID, tuple);
        }

        public void close() {
                OtpErlangAtom close = new OtpErlangAtom("close");
                mbox.send(playerPID, close);
        }

        public void run() {
		OtpErlangObject object;
		OtpErlangTuple msg;
                OtpErlangAtom atom;
		OtpErlangPid from;
		OtpErlangLong x;
		OtpErlangLong y;
                //OtpErlangAtom state;

		while (true) {
			try {
				object = mbox.receive();

				if(object instanceof OtpErlangTuple) {
					msg = (OtpErlangTuple)object;

                                        atom = (OtpErlangAtom)msg.elementAt(0);
                                        from = (OtpErlangPid)msg.elementAt(1);
                                        x = (OtpErlangLong)msg.elementAt(2);
                                        y = (OtpErlangLong)msg.elementAt(3);
                                        //state = (OtpErlangAtom)msg.elementAt(4);

                                        String a = (String)atom.atomValue();
                                        int pid = (int)from.id();
                                        int newX = ((int)x.intValue())*(560/100);
                                        int newY = ((int)y.intValue())*(560/100);
                                        //String s = (String)state.atomValue();

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
                                        
<<<<<<< HEAD
                                        
=======
                                        world.repaint();
>>>>>>> 66ceabb11f6d166e13fd4ecd4136ff3562686305
				}
			} catch (OtpErlangRangeException ex) {
                                Logger.getLogger(ErlController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (OtpErlangDecodeException ex) {
                                Logger.getLogger(ErlController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch(OtpErlangExit e) {
				System.out.println("error: " + e);
			}
		}
	}
}