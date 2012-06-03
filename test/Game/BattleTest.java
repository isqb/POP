package Game;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Marcus Utter
 * @author Luis Mauricio
 * @author Olof Bj�rklund
 * @author Mark Tibblin
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
