package Game;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mark
 */
public class BattleTest {
    
    public BattleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    

    /**
     * Test of fight method, of class Battle.
     */
    @Test
    public void testFight() throws Exception {
        System.out.println("fight");
        Battle battle = new Battle(true);
        
        assertFalse(battle.getPlayerDead());
        battle.fight();
        assertTrue(battle.getPlayerDead());
        
        assertFalse(battle.getMonsterDead());
        battle.setShoot(true);
        battle.fight();
        assertTrue(battle.getMonsterDead());
    }

    /**
     * Test of actionPerformed method, of class Battle.
     */
    @Test
    public void testActionPerformed() throws Exception {
        System.out.println("actionPerformed");
        Battle battle = new Battle(true);
        assertFalse(battle.getFight());
        battle.actionPerformed();
        assertTrue(battle.getFight());
    }
}
