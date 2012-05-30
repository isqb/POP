import com.ericsson.otp.erlang.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Creates a write/listen ("messagepassing") for erlang nodes (processes) 
 * for game logics. 
 * 
 */

public class ErlController {
    
    ////Parameters////
    
    private String nodeName = "sigui";
    private String mBoxName = "gui";
    private OtpNode node;
    private OtpMbox mbox;
    private OtpErlangPid playerPID = null;
    private OtpErlangPid mainPID = null;
    private World world;
    private boolean inBattle = false;

    //// Constructors////

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
    
 /**
 *Sends an atom "exit" to erlang node for closing down all erlang processes.
 * 
 */

    public void close() {
        mbox.send(mainPID, new OtpErlangAtom("exit"));
    }

 /**
 *Sends movement direction for playerPID process. 
 * 
 *@param direction 
 */    
    
    public void move(String direction) {
            OtpErlangAtom dir = new OtpErlangAtom(direction);
            OtpErlangObject[] o = new OtpErlangObject[]{new OtpErlangAtom("move"), dir};
            OtpErlangTuple tuple = new OtpErlangTuple(o);
            mbox.send(playerPID, tuple);
    }

    
    
 /**
 *
 * Sends results from a Battle between 2 Char objects to erlang nodes, 
 * also with necessery game logic parameters(atoms). 
 * 
 * @param loserPID
 * @param winnerPID
 * @param b
 * @param loser 
 * @param winner
 */
    
   
    public void kill(OtpErlangPid loserPID, OtpErlangPid winnerPID, Battle b, Char loser, Char winner)
    {
        try {
            if (b != null) {
                winner.setImage("RightShoot.png");
                loser.setY(loser.getY() - 10);
                loser.setImage(GridSimulate.createImageIcon("grave.png"));

                b.repaint();
                Thread.sleep(2000);
            }
            boolean humanBattle = false;
            if ((int) winnerPID.id() == (int) playerPID.id() || (int) loserPID.id() == (int) playerPID.id()) {
                humanBattle = true;
                inBattle = false;
            }
            world.endBattle(loserPID, humanBattle);
            mbox.send(winnerPID, new OtpErlangAtom("unfreeze"));
            mbox.send(loserPID, new OtpErlangAtom("kill"));
        } catch (InterruptedException ex) {
            Logger.getLogger(ErlController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
 /**
 * Initialize a thread with a new erlang listener, 
 * atom messages such as "human", "battle" and "move" for visualizing 
 * events accured in erlang.
 * 
 */
    
    
    public void run() {
        OtpErlangObject object;
        OtpErlangTuple msg;
        OtpErlangAtom command;
        OtpErlangAtom player;
        OtpErlangPid player1PID;
        OtpErlangPid player2PID;
        OtpErlangLong x;
        OtpErlangLong y;

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
                            inBattle = true;
                            world.startBattle(player1PID, player2PID);
                        }
                        else if(!inBattle  &&  (int)player2PID.id() == (int)playerPID.id()) {
                            inBattle = true;
                            world.startBattle(player2PID, player1PID);
                        }
                        else {
                            Random gen = new Random();
                            int deadBot = gen.nextInt(2);
                            if (deadBot == 1) {
                                kill(player1PID, player2PID, null, null, null);
                            }
                            else {
                                kill(player2PID, player1PID, null, null, null);
                            }
                        }
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
                else {
                    mainPID = (OtpErlangPid)object;
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