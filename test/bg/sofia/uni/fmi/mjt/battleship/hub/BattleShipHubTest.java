package bg.sofia.uni.fmi.mjt.battleship.hub;

import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BattleShipHubTest {

    private BattleShipHub battleShipHub;
    private ShipFactory factory;
    private Ship ship;

    @Before
    public void prepareToPlaceShip() {
        this.battleShipHub = new BattleShipHub();
        this.factory = new ShipFactory();
        this.ship = factory.buildShip(ShipType.CARRIER);
        this.battleShipHub.placeShip(ship, "A1", "A5");
    }

    @Test
    public void testPlaceShip() {
        BattleShipHub expectedHub = new BattleShipHub(battleShipHub);
        BattleShipHub actualHub = new BattleShipHub();
        actualHub.placeShip(ship, "A1", "A5");
        final String message = "The battleship hub is incorrect!";

        assertEquals(message, expectedHub, actualHub);
    }

    @Test
    public void testPlaceUnavailableShipType() {
        BattleShipHub actualHub = new BattleShipHub();
        actualHub.placeShip(factory.buildShip(ShipType.CARRIER), "A1", "A5");
        boolean actualResult = actualHub.placeShip(factory.buildShip(ShipType.CARRIER), "B1", "B5");
        final String message = "The carrier ships number can't more than one!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testChangeShipPosition() {
        BattleShipHub expectedHub = new BattleShipHub(battleShipHub);
        expectedHub.changeShipPosition("A1", "B1", "B5");
        BattleShipHub actualHub = new BattleShipHub();
        actualHub.placeShip(ship, "A1", "A5");
        actualHub.changeShipPosition("A1", "B5", "B1");
        final String message = "The ship position after change is incorrect!";

        assertEquals(message, expectedHub, actualHub);
    }

    @Test
    public void testChangeDamagedShipPosition() {
        BattleShipHub expectedHub = new BattleShipHub(battleShipHub);
        expectedHub.attack("A2");
        boolean actualResult = expectedHub.changeShipPosition("A1", "B1", "B5");
        final String message = "The ship position can't be change when the ship is damaged!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testRepairShip() {
        BattleShipHub actualHub = new BattleShipHub(battleShipHub);
        actualHub.attack("A2");
        boolean actualResult = actualHub.repairShip("A2");
        final String message = "The ship should be repaired!";

        assertTrue(message, actualResult);
    }

    @Test
    public void testRepairDestroyedShip() {
        BattleShipHub actualHub = new BattleShipHub(battleShipHub);
        actualHub.attack("A1");
        actualHub.attack("A2");
        actualHub.attack("A3");
        actualHub.attack("A4");
        actualHub.attack("A5");
        boolean actualResult = actualHub.repairShip("A2");
        final String message = "The ship can't be repaired after it is destroyed!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testScan() {
        final String position = "B2";
        BattleShipHub expectedHub = new BattleShipHub(battleShipHub);
        char[][] expectedScanMap = expectedHub.scan(position);
        BattleShipHub actualHub = battleShipHub;
        char[][] actualScanMap = actualHub.scan(position);
        final String message = "The scan map is incorrect!";

        assertArrayEquals(message, expectedScanMap, actualScanMap);
    }

    @Test
    public void testIncorrectScan() {
        final String position = "B27";
        BattleShipHub actualHub = battleShipHub;
        char[][] actualScanMap = actualHub.scan(position);
        final String message = "The scan map is null with incorrect data!";

        assertNull(message, actualScanMap);
    }
}
