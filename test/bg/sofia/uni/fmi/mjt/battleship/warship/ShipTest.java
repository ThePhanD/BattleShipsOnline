package bg.sofia.uni.fmi.mjt.battleship.warship;

import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ShipTest {

    private ShipFactory factory;

    @Before
    public void prepareFactory() {
        factory = new ShipFactory();
    }

    @Test
    public void testShipTakeDamage() {
        Ship ship = factory.buildShip(ShipType.DESTROYER);
        ship.takeDamage();
        final int expectedHealth = 2;
        int actualHealth = ship.getHealth();
        final String message = "The ship must be with one less health!";

        assertEquals(message, expectedHealth, actualHealth);
    }

    @Test
    public void testShipTakeFullDamage() {
        Ship ship = factory.buildShip(ShipType.SUBMARINE);
        ship.takeDamage();
        ship.takeDamage();
        ship.takeDamage();
        final int expectedHealth = 0;
        int actualHealth = ship.getHealth();
        final String message = "The ship can't be with minus health!";

        assertEquals(message, expectedHealth, actualHealth);
    }

    @Test
    public void testRepairShip() {
        Ship ship = factory.buildShip(ShipType.CARRIER);
        ship.takeDamage();
        ship.repair();
        final int expectedHealth = 5;
        int actualHealth = ship.getHealth();
        final String message = "The ship must be full health!";

        assertEquals(message, expectedHealth, actualHealth);
    }

    @Test
    public void testRepairDestroyedShip() {
        Ship ship = factory.buildShip(ShipType.CRUISER);
        ship.takeDamage();
        ship.takeDamage();
        ship.takeDamage();
        ship.takeDamage();
        ship.repair();
        final int expectedHealth = 0;
        int actualHealth = ship.getHealth();
        final String message = "The ship can't be repair when it is destroyed!";

        assertEquals(message, expectedHealth, actualHealth);
    }

    @Test
    public void testBuildShips() {
        final String carrier = "carrier";
        final String cruiser = "cruiser";
        final String destroyer = "destroyer";
        final String submarine = "submarine";
        Ship shipCarrier = factory.buildShip(carrier);
        Ship shipCruiser = factory.buildShip(cruiser);
        Ship shipDestroyer = factory.buildShip(destroyer);
        Ship shipSubmarine = factory.buildShip(submarine);
        String message = "The ship can't be null!";

        assertNotNull(message, shipCarrier);
        assertNotNull(message, shipCruiser);
        assertNotNull(message, shipDestroyer);
        assertNotNull(message, shipSubmarine);
    }

    @Test
    public void testBuildNotExistingShipType() {
        final String unknownShipType = "unknown";
        final String empty = null;
        Ship unknownShip = factory.buildShip(unknownShipType);
        Ship notExistShipType = factory.buildShip(empty);
        String message = "The ship must be null!";

        assertNull(message, unknownShip);
        assertNull(message, notExistShipType);
    }
}
