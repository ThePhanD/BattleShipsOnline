package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RepairShipTest {

    private ControlPanel controlPanel = new ControlPanel();
    private BattleShipHub battleShipHub;
    private Ship ship;

    @Before
    public void prepareToRepairShip() {
        this.battleShipHub = new BattleShipHub();
        ShipFactory factory = new ShipFactory();
        this.ship = factory.buildShip(ShipType.CARRIER);
        this.battleShipHub.placeShip(ship, "A1", "A5");
        this.battleShipHub.attack("A1");
    }

    @Test
    public void testRepairShip() {
        RepairShip repairShip = new RepairShip(controlPanel, battleShipHub, "A1");
        boolean actualResult = repairShip.execute();
        final String message = "The ship must be repaired!";

        assertTrue(message, actualResult);
    }

    @Test
    public void testRepairDestroyedShip() {
        BattleShipHub hub = new BattleShipHub();
        ShipFactory factory = new ShipFactory();
        hub.placeShip(factory.buildShip(ShipType.SUBMARINE), "A1", "A2");
        hub.attack("A1");
        hub.attack("A2");

        RepairShip repairShip = new RepairShip(controlPanel, hub, "A1");
        boolean actualResult = repairShip.execute();
        final String message = "The ship can't be repaired when is destroyed!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testRepairShipAction() {
        final String repairShipAction = "repair B5";
        boolean actualResult = controlPanel.isRepairShipAction(repairShipAction);
        final String message = "The repair command must be correct";

        assertTrue(message, actualResult);
    }
}
