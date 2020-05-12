package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScanMapTest {

    private ControlPanel controlPanel = new ControlPanel();
    private BattleShipHub battleShipHub;
    private char[][] expectedScanMap;


    @Before
    public void prepareToScanMap() {
        this.battleShipHub = new BattleShipHub();
        ShipFactory factory = new ShipFactory();
        Ship ship = factory.buildShip(ShipType.CARRIER);
        this.battleShipHub.placeShip(ship, "A1", "A5");
        this.expectedScanMap = battleShipHub.scan("B2");
    }

    @Test
    public void testScanMap() {
        BattleShipHub actualHub = new BattleShipHub(battleShipHub);
        ScanMap scanMap = new ScanMap(controlPanel, actualHub, "B2");
        char[][] actualScanMap = scanMap.execute();
        final String message = "The scan map is incorrect!";

        assertArrayEquals(message, expectedScanMap, actualScanMap);
    }

    @Test
    public void testIncorrectScanMap() {
        BattleShipHub actualHub = new BattleShipHub(battleShipHub);
        ScanMap scanMap = new ScanMap(controlPanel, actualHub, "**");
        char[][] actualScanMap = scanMap.execute();
        final String message = "The scan map should be null with incorrect data!";

        assertNull(message, actualScanMap);

    }

    @Test
    public void testScanAction() {
        final String scanAction = "scan B5";
        boolean actualResult = controlPanel.isScanMapAction(scanAction);
        final String message = "The scan command must be correct";

        assertTrue(message, actualResult);
    }
}
