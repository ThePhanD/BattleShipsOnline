package bg.sofia.uni.fmi.mjt.battleship.map;

import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameMapEngineTest {

    private static final char MISS = 'O';

    private GameMapEngine gameMapEngine;
    private Ship ship;

    @Before
    public void prepareToTestGameMapEngine() {
        gameMapEngine = new GameMapEngine();
        ShipFactory factory = new ShipFactory();
        ship = factory.buildShip(ShipType.DESTROYER);
        gameMapEngine.setShip(0, 0, 0, 2, ship);
    }

    @Test
    public void testSetShip() {
        GameMapEngine actualGameMapEngine = new GameMapEngine();
        actualGameMapEngine.setShip(0, 0, 0, 2, ship);
        GameMapEngine expectedGameMapEngine = gameMapEngine;
        final String message = "The ship position on the map is incorrect!";

        assertEquals(message, expectedGameMapEngine, actualGameMapEngine);
    }

    @Test
    public void testIncorrectSetShip() {
        GameMapEngine expectedGameMapEngine = new GameMapEngine();
        GameMapEngine actualGameMapEngine = new GameMapEngine();
        actualGameMapEngine.setShip(0, 1, 0, 2, ship);
        final String message = "The ship can't be set with incorrect data!";

        assertEquals(message, expectedGameMapEngine, actualGameMapEngine);
    }

    @Test
    public void testRemoveShip() {
        GameMapEngine expectedGameMapEngine = new GameMapEngine();
        gameMapEngine.removeShip(0, 0, 0, 2, ship);
        final String message = "The ship mustn't exist on the map!";

        assertEquals(message, expectedGameMapEngine, gameMapEngine);
    }

    @Test
    public void testIncorrectRemoveShip() {
        GameMapEngine expectedGameMapEngine = new GameMapEngine();
        expectedGameMapEngine.setShip(0, 0, 0, 2, ship);
        gameMapEngine.removeShip(0, 1, 0, 2, ship);
        final String message = "The ship can't be removed with incorrect data!";

        assertEquals(message, expectedGameMapEngine, gameMapEngine);
    }

    @Test
    public void testFireAtShipPosition() {
        GameMapEngine actualGameMapEngine = new GameMapEngine();
        actualGameMapEngine.setShip(0, 0, 0, 2, ship);
        actualGameMapEngine.fireAtPosition(0, 0);
        GameMapEngine expectedGameMapEngine = gameMapEngine;
        expectedGameMapEngine.fireAtPosition(0, 0);
        final String message = "The ship should supposed to be hit!";

        assertEquals(message, expectedGameMapEngine, actualGameMapEngine);
    }

    @Test
    public void testFireAtPosition() {
        GameMapEngine actualGameMapEngine = new GameMapEngine();
        actualGameMapEngine.setShip(0, 0, 0, 2, ship);
        actualGameMapEngine.fireAtPosition(6, 8);
        GameMapEngine expectedGameMapEngine = gameMapEngine;
        expectedGameMapEngine.fireAtPosition(6, 8);
        final String message = "The hit field is incorrect!";

        assertEquals(message, expectedGameMapEngine, actualGameMapEngine);
    }

    @Test
    public void testFireAtIncorrectPosition() {
        boolean actualResult = gameMapEngine.fireAtPosition(-6, 300);
        final String message = "The fire can't be outside of the map border!";

        assertFalse(message, actualResult);
    }

    @Test
    public void testPlateAtPosition() {
        GameMapEngine actualGameMapEngine = new GameMapEngine();
        actualGameMapEngine.setShip(0, 0, 0, 2, ship);
        actualGameMapEngine.placeAtPosition(0, 3, MISS);
        GameMapEngine expectedGameMapEngine = gameMapEngine;
        expectedGameMapEngine.placeAtPosition(0, 3, MISS);
        final String message = "The miss field is incorrect!";

        assertEquals(message, expectedGameMapEngine, actualGameMapEngine);
    }

    @Test
    public void testPlaceAtIncorrectPosition() {
        boolean actualResult = gameMapEngine.placeAtPosition(-6, 300, MISS);
        final String message = "The placing can't be outside of the map border!";

        assertFalse(message, actualResult);
    }
}
