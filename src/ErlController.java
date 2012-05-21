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
    private OtpErlangPid killPID = null;
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
        public void kill(OtpErlangPid cowboyPID)
        {
            
            killPID = cowboyPID;
            OtpErlangObject[] o = new OtpErlangObject[]{new OtpErlangAtom("exit")};
            OtpErlangTuple tuple = new OtpErlangTuple(o);
            mbox.send(killPID, tuple); // hmmm vilken pid?
        }

        public void run() {
		OtpErlangObject object;
		OtpErlangTuple msg;
                OtpErlangAtom atom;
		OtpErlangPid from1;
                OtpErlangPid from2;
		OtpErlangLong x;
		OtpErlangLong y;
                //OtpErlangAtom state;

		while (true) {
			try {
				object = mbox.receive();

				if(object instanceof OtpErlangTuple) {
					msg = (OtpErlangTuple)object;

                                        atom = (OtpErlangAtom)msg.elementAt(0);
                                        from1 = (OtpErlangPid)msg.elementAt(1);
                                        from2 = (OtpErlangPid)msg.elementAt(2);
                                        x = (OtpErlangLong)msg.elementAt(3);
                                        y = (OtpErlangLong)msg.elementAt(4);
                                        //state = (OtpErlangAtom)msg.elementAt(4);

                                        String a = (String)atom.atomValue();
                                        int pid1 = (int)from1.id();
                                        int pid2 = (int)from2.id();
                                        int newX = ((int)x.intValue())*(560/100);
                                        int newY = ((int)y.intValue())*(560/100);
                                        //String s = (String)state.atomValue();

                                        if(a.equals("battle"))
                                        {
                                            //Cowboy cb1= (Cowboy)cowboys.get(pid1);// test
                                            //Cowboy cb2= (Cowboy)cowboys.get(pid2);// test
                                           
                                            world.startBattle(from1, from2);
                                        }
                                        
                                        Hashtable cowboys = world.getCowboys();
                                        boolean isHuman;
                                        if (a.equals("human")) 
                                            {
                                                isHuman = true;
                                                playerPID = from1;
                                            }
                                        else {
                                            isHuman = false;
                                        }
                                        
                                        if (cowboys.containsKey(pid1)) {
                                            world.move((Cowboy)cowboys.get(pid1), newX, newY);
                                            
                                        }
                                        else {
                                            
                                            world.createCowboy(pid1, newX, newY, isHuman);
                                        }
                                        
                                        
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