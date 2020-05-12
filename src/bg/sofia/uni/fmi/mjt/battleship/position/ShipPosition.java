package bg.sofia.uni.fmi.mjt.battleship.position;

import bg.sofia.uni.fmi.mjt.battleship.exception.IllegalCoordinatesException;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;

import java.io.Serializable;
import java.util.Objects;

public class ShipPosition implements Serializable {

    private static final long serialVersionUID = 12345L;

    private Ship ship;
    private Position startPosition;
    private Position endPosition;

    public ShipPosition(Ship ship, String startPosition, String endPosition) throws IllegalCoordinatesException {
        Position start = new Position(startPosition);
        Position end = new Position(endPosition);
        init(ship, start, end);
    }

    public Ship getShip() {
        return ship;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    /**
     * Check if the @code position is within ship fields.
     **/
    public boolean isWithinShipField(Position position) {
        int startPositionRow = startPosition.getRow();
        int startPositionCol = startPosition.getCol();
        int endPositionRow = endPosition.getRow();
        int endPositionCol = endPosition.getCol();
        int row = position.getRow();
        int col = position.getCol();

        if (startPositionRow == endPositionRow) {
            if (startPositionRow == row) {
                return isWithinShipFields(startPositionCol, endPositionCol, col);
            }
        } else if (startPositionCol == endPositionCol) {
            if (startPositionCol == col) {
                return isWithinShipFields(startPositionRow, endPositionRow, row);
            }
        }

        return false;
    }

    private boolean isWithinShipFields(int startPositionField, int endPositionField, int field) {
        if (startPositionField <= field && field <= endPositionField) {
            return true;
        }

        return endPositionField <= field && field <= startPositionField;
    }

    private void init(Ship ship, Position startPosition, Position endPosition) {
        this.ship = ship;
        this.startPosition = new Position(startPosition);
        this.endPosition = new Position(endPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShipPosition that = (ShipPosition) o;
        return Objects.equals(ship, that.ship) &&
                ((Objects.equals(startPosition, that.startPosition) && Objects.equals(endPosition, that.endPosition)) ||
                        ((Objects.equals(startPosition, that.endPosition) && Objects.equals(endPosition, that.startPosition)))
                );
    }

    @Override
    public int hashCode() {
        return Objects.hash(ship);
    }
}
