package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveShipTest {

    private ControlPanel controlPanel = new ControlPanel();
    private BattleShipHub battleShipHub;
    private Ship ship;

    @Before
    public void prepareToMoveShip() {
        this.battleShipHub = new BattleShipHub();
        ShipFactory factory = new ShipFactory();
        this.ship = factory.buildShip(ShipType.CARRIER);
        this.battleShipHub.placeShip(ship, "A1", "A5");
    }

    @Test
    public void testMoveShip() {
        BattleShipHub expectedHub = new BattleShipHub(battleShipHub);
        expectedHub.changeShipPosition("A1", "B1", "B5");
        BattleShipHub actualHub = battleShipHub;
        MoveShip moveShip = new MoveShip(controlPanel, battleShipHub, "A1", "B1", "B5");
        moveShip.execute();
        final String message = "The ship position is incorrect!";

        assertEquals(message, expectedHub, actualHub);
    }

    @Test
    public void testMoveDamagedShip() {
        battleShipHub.attack("A1");
        MoveShip moveShip = new MoveShip(controlPanel, battleShipHub, "A1", "B1", "B5");
        boolean actualResult = moveShip.execute();
        final String message = "The ship position can't be changed when the ship is damaged!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testMoveShipAction() {
        final String moveShipAction = "move A1 B1 B5";
        boolean actualResult = controlPanel.isMoveShipAction(moveShipAction);
        final String message = "The move command must be correct";

        assertTrue(message, actualResult);
    }
}
