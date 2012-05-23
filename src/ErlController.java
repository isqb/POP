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
    private boolean inBattle = false;

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

        public void close() {
            mbox.send(playerPID, new OtpErlangAtom("close"));
        }

        public void move(String direction) {
                OtpErlangAtom dir = new OtpErlangAtom(direction);
                OtpErlangObject[] o = new OtpErlangObject[]{new OtpErlangAtom("move"), dir};
                OtpErlangTuple tuple = new OtpErlangTuple(o);
                mbox.send(playerPID, tuple);
        }
        
        public void kill(OtpErlangPid loser, OtpErlangPid winner)
        {
            boolean humanBattle = false;
            if ((int)winner.id() == (int)playerPID.id()  ||
                (int)loser.id() == (int)playerPID.id()) {
                
                humanBattle = true;
            }

            inBattle = false;
            world.endBattle(loser, humanBattle);
            mbox.send(winner, new OtpErlangAtom("unfreeze"));
            mbox.send(loser, new OtpErlangAtom("kill")); // hmmm vilken pid?
        }

        public void run() {
		OtpErlangObject object;
		OtpErlangTuple msg;
                OtpErlangAtom command;
                OtpErlangAtom player;
		OtpErlangPid player1PID;
                OtpErlangPid player2PID;
		OtpErlangLong x;
		OtpErlangLong y;
                //OtpErlangAtom state;

		while (true) {
			try {
				object = mbox.receive();

				if(object instanceof OtpErlangTuple) {
					msg = (OtpErlangTuple)object;

                                        command = (OtpErlangAtom)msg.elementAt(0);
                                        player = (OtpErlangAtom)msg.elementAt(1);
                                        player1PID = (OtpErlangPid)msg.elementAt(2);
                                        player2PID = (OtpErlangPid)msg.elementAt(3);
                                        x = (OtpErlangLong)msg.elementAt(4);
                                        y = (OtpErlangLong)msg.elementAt(5);

                                        String cmd = (String)command.atomValue();
                                        String pl = (String)player.atomValue();
                                        
                                        boolean isHuman;
                                        if (pl.equals("human")) {
                                            
                                            isHuman = true;
                                        }
                                        else {
                                            
                                            isHuman = false;
                                        }
                                        
                                        if(cmd.equals("battle")) {
                                            
                                            if (!inBattle  &&  (int)player1PID.id() == (int)playerPID.id()) {
                                                world.startBattle(player1PID, player2PID);
                                            }
                                            else if(!inBattle  &&  (int)player2PID.id() == (int)playerPID.id()) {
                                                world.startBattle(player2PID, player1PID);
                                            }
                                            inBattle = true;
                                        }
                                        else if(cmd.equals("move")) {

                                            if (isHuman) {
                                                playerPID = player1PID;
                                            }
                                            Hashtable chars = world.getChars();
                                            int pid = (int)player1PID.id();
                                            double newX = x.intValue()*(1024/100);
                                            double newY = y.intValue()*(580/100);

                                            if (chars.containsKey(pid)) {
                                                world.move((Char)chars.get(pid), newX, newY);

                                            }
                                            else {

                                                world.createChar(pid, newX, newY, isHuman);
                                            }
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