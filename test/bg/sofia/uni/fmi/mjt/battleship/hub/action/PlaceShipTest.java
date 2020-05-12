package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlaceShipTest {

    private ControlPanel controlPanel = new ControlPanel();
    private BattleShipHub battleShipHub;
    private Ship ship;

    @Before
    public void prepareToPlaceShip() {
        this.battleShipHub = new BattleShipHub();
        ShipFactory factory = new ShipFactory();
        this.ship = factory.buildShip(ShipType.CARRIER);
        this.battleShipHub.placeShip(ship, "A1", "A5");
    }

    @Test
    public void testPlaceShip() {
        BattleShipHub expectedHub = new BattleShipHub(battleShipHub);
        BattleShipHub actualHub = battleShipHub;
        PlaceShip placeShip = new PlaceShip(controlPanel, battleShipHub, ship, "A1", "A5");
        placeShip.execute();
        final String message = "The ship position is incorrect!";

        assertEquals(message, expectedHub, actualHub);
    }

    @Test
    public void testIncorrectPlaceShip() {
        PlaceShip placeShip = new PlaceShip(controlPanel, battleShipHub, ship, "A1", "A");
        boolean actualResult = placeShip.execute();
        final String message = "The ship can't be placed with incorrect data!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testPlaceShipAction() {
        final String placeShipAction = "place cruiser B1 B5";
        boolean actualResult = controlPanel.isPlaceShipAction(placeShipAction);
        final String message = "The place command must be correct";

        assertTrue(message, actualResult);
    }
}
