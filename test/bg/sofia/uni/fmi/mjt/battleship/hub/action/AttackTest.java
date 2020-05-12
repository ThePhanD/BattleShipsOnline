package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttackTest {

    private ControlPanel controlPanel = new ControlPanel();
    private BattleShipHub battleShipHub;
    private String position;

    @Before
    public void prepareToAttack() {
        this.battleShipHub = new BattleShipHub();
        this.position = "A1";
    }

    @Test
    public void testAttack() {
        Attack attack = new Attack(controlPanel, battleShipHub, position);
        boolean actualResult = attack.execute();
        final String message = "The attack must be possible!";

        assertTrue(message, actualResult);
    }

    @Test
    public void testIncorrectAttack() {
        Attack attack = new Attack(controlPanel, battleShipHub, "%%");
        boolean actualResult = attack.execute();
        final String message = "The attack isn't possible!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testAttackAction() {
        final String attackAction = "attack A1";
        boolean actualResult = controlPanel.isAttackAction(attackAction);
        final String message = "The attack command must be correct";

        assertTrue(message, actualResult);
    }
}
