package bg.sofia.uni.fmi.mjt.battleship.position;

import bg.sofia.uni.fmi.mjt.battleship.exception.IllegalCoordinatesException;
import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShipPositionTest {

    private static final String START_POSITION = "A1";
    private static final String END_POSITION = "A3";

    private Ship ship;
    private ShipPosition shipPosition;

    @Before
    public void prepareShipPosition() throws IllegalCoordinatesException {
        ShipFactory factory = new ShipFactory();
        this.ship = factory.buildShip(ShipType.DESTROYER);
        this.shipPosition = new ShipPosition(ship, START_POSITION, END_POSITION);
    }

    @Test
    public void testShipPosition() throws IllegalCoordinatesException {
        ShipPosition expectedShipPosition = new ShipPosition(ship, START_POSITION, END_POSITION);
        final String message = "The ship positions must be equals!";

        assertEquals(message, expectedShipPosition.getStartPosition(), shipPosition.getStartPosition());
        assertEquals(message, expectedShipPosition.getEndPosition(), shipPosition.getEndPosition());
        assertEquals(message, expectedShipPosition, shipPosition);
    }

    @Test(expected = IllegalCoordinatesException.class)
    public void testIncorrectCoordinatesShipPosition() throws IllegalCoordinatesException {
        new ShipPosition(ship, "2^", "%%");
        final String message = "The ship positions are incorrect must be thrown IllegalCoordinatesException!";

        System.out.println(message);
    }

    @Test
    public void testIsWithinShipField() throws IllegalCoordinatesException {
        Position position = new Position(START_POSITION);
        boolean actualResult = shipPosition.isWithinShipField(position);
        final String message = "The position is in within row ship field ";

        assertTrue(message, actualResult);
    }

    @Test
    public void testIncorrectIsWithinShipField() throws IllegalCoordinatesException {
        Position position = new Position("A7");
        boolean actualResult = shipPosition.isWithinShipField(position);
        final String message = "The position isn't in within ship field ";

        assertFalse(message, actualResult);
    }

    @Test
    public void testIsWithinShipFieldCol() throws IllegalCoordinatesException {
        ShipPosition shipPosition =new ShipPosition(ship, "B4", "E4");
        Position position = new Position("C4");
        boolean actualResult = shipPosition.isWithinShipField(position);
        final String message = "The position is in within column ship field ";

        assertTrue(message, actualResult);
    }

}
