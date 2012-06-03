package Game;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Enumeration;
import java.util.Hashtable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marcus Utter
 * @author Luis Mauricio
 * @author Olof Björklund
 * @author Mark Tibblin
 */
public class WorldTest {
    
    public WorldTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of createChar method, of class World.
     */
    @Test
    public void testCreateChar() {
        System.out.println("createChar");
        World world = new World();
        Hashtable chars = world.getChars();
        Enumeration e = chars.elements();
        assertFalse(e.hasMoreElements());
        
        world.createChar(50, 10.0, 10.0, false);
        world.createChar(51, 20.0, 15.0, true);
        chars = world.getChars();
        e = chars.elements();
        int nr = 0;
        while (e.hasMoreElements()) {
            e.nextElement();
            nr++;
        }
        assertEquals(nr, 2);
    }

    /**
     * Test of move method, of class World.
     */
    @Test
    public void testMove() {
        System.out.println("move");
        World world = new World();
        int key = 51;
        double x = 20.0;
        double y = 15.0;
        world.createChar(key, x, y, true);
        Char cowboy = (Char)world.getChars().get(key);
        assertEquals((int)cowboy.getX(), (int)x);
        assertEquals((int)cowboy.getY(), (int)y);
        
        double newX = 21.0;
        double newY = 15.0;
        world.move(cowboy, newX, newY);
        assertEquals((int)cowboy.getX(), (int)newX);
        assertEquals((int)cowboy.getY(), (int)newY);
    }
}
