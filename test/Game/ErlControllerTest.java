package Game;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.ericsson.otp.erlang.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mark
 */
public class ErlControllerTest implements Runnable {

    private OtpMbox mbox = null;
    private OtpErlangAtom atom = null;

    public ErlControllerTest() throws IOException {
        this.mbox = new OtpNode("testNode").createMbox("testMbox");
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private OtpErlangAtom getAtom() {
        return atom;
    }

    private OtpMbox getMbox() {
        return mbox;
    }

    private void setMbox(OtpMbox mbox) throws IOException {
        this.mbox = mbox;
    }

    private void resetAtom() {
        atom = null;
    }

    /**
     * Test of close method, of class ErlController.
     */
    @Test
    public void testClose() throws IOException, OtpErlangExit, OtpErlangDecodeException, InterruptedException {
        System.out.println("close");
        ErlController erl = new ErlController(new World());
        ErlControllerTest erlTest = new ErlControllerTest();
        new Thread(erlTest).start();
        while(erlTest.getMbox() == null);
        erl.setMainPID(erlTest.getMbox());
        erl.close();
        while (erlTest.getAtom() == null);
        assertEquals("exit", erlTest.getAtom().atomValue());
    }

    /**
     * Test of move method, of class ErlController.
     */
    @Test
    public void testMove() throws IOException, OtpErlangExit, OtpErlangDecodeException {
        System.out.println("move");
        String west = "w";
        String north = "n";
        String south = "s";
        String east = "e";
        ErlController erl = new ErlController(new World());
        
        
        ErlControllerTest erlTest = new ErlControllerTest();
        new Thread(erlTest).start();
        while (erlTest.getMbox() == null);
        erl.setPlayerPID(erlTest.getMbox());
        
        erl.move(west);
        /*OtpErlangObject o = mbox.receive();
        OtpErlangTuple tuple = (OtpErlangTuple)o;
        OtpErlangAtom atom = (OtpErlangAtom)tuple.elementAt(1);*/
        while (erlTest.getAtom() == null);
        assertEquals(west, erlTest.getAtom().atomValue());
        erlTest.resetAtom();

        new Thread(erlTest).start();
        erl.move(north);
        /*o = mbox.receive();
        tuple = (OtpErlangTuple)o;
        atom = (OtpErlangAtom)tuple.elementAt(1);*/
        while (erlTest.getAtom() == null);
        assertEquals(north, erlTest.getAtom().atomValue());
        erlTest.resetAtom();

        new Thread(erlTest).start();
        erl.move(south);
        /*o = mbox.receive();
        tuple = (OtpErlangTuple)o;
        atom = (OtpErlangAtom)tuple.elementAt(1);*/
        while (erlTest.getAtom() == null);
        assertEquals(south, erlTest.getAtom().atomValue());
        erlTest.resetAtom();

        new Thread(erlTest).start();
        erl.move(east);
        /*o = mbox.receive();
        tuple = (OtpErlangTuple)o;
        atom = (OtpErlangAtom)tuple.elementAt(1);*/
        while (erlTest.getAtom() == null);
        assertEquals(east, erlTest.getAtom().atomValue());
        erlTest.resetAtom();
    }

    /**
     * Test of kill method, of class ErlController.
     */
    /*@Test
    public void testKill() throws IOException, OtpErlangExit, OtpErlangDecodeException {
        System.out.println("kill");
        World world = new World();
        ErlController erl = new ErlController(world);
        ErlControllerTest loserTest = new ErlControllerTest();
        new Thread(loserTest).start();
        ErlControllerTest winnerTest = new ErlControllerTest();
        new Thread(winnerTest).start();
        loserTest.setMbox(new OtpNode("loserNode").createMbox("loserMbox"));
        winnerTest.setMbox(new OtpNode("winnerNode").createMbox("winnerMbox"));
        while (loserTest.getMbox() == null  ||  winnerTest.getMbox() == null);
//        OtpNode node = new OtpNode("testNode");
//        OtpMbox loserMbox = node.createMbox("testLoserMbox");
//        OtpMbox winnerMbox = node.createMbox("testWinnerMbox");
        world.createChar(loserTest.getMbox().self().id(), 20.0, 15.0, true);
        OtpErlangPid loserPID = loserTest.getMbox().self();
        OtpErlangPid winnerPID = winnerTest.getMbox().self();
        System.out.println("loserPID: " + loserPID);
        System.out.println("winnerPID: " + winnerPID);
        erl.setPlayerPID(loserTest.getMbox());
        erl.kill(loserPID, winnerPID);
        
        while (loserTest.getAtom() == null);
        OtpErlangAtom loserAtom = loserTest.getAtom();
        assertEquals("kill", loserAtom.atomValue());

        while (winnerTest.getAtom() == null);
        OtpErlangAtom winnerAtom = winnerTest.getAtom();
        assertEquals("unfreeze", winnerAtom.atomValue());
    }*/

    public void run() {
        try {
            OtpErlangObject msg = mbox.receive();
            if (msg instanceof OtpErlangTuple) {
                OtpErlangTuple tuple = (OtpErlangTuple)msg;
                atom = (OtpErlangAtom)tuple.elementAt(1);
            }
            else if (msg instanceof OtpErlangAtom) {
                atom = (OtpErlangAtom)msg;
            }
            
        } catch (OtpErlangExit ex) {
            Logger.getLogger(ErlControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OtpErlangDecodeException ex) {
            Logger.getLogger(ErlControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of run method, of class ErlController.
     */
    /*@Test
    public void testRun() throws IOException {
        System.out.println("run");
        ErlController erl = new ErlController(new World());
        OtpMbox mbox = new OtpNode("testNode").createMbox("testMbox");
        erl.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
