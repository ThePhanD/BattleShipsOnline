package bg.sofia.uni.fmi.mjt.battleship.position;

import bg.sofia.uni.fmi.mjt.battleship.exception.IllegalCoordinatesException;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {

    private static final String INCORRECT_COORDINATES = "The coordinates must be between row A to J," +
            " and between col 1 to 10!";

    private static final char startPosition = 'A';
    private static final int ROW = 0;
    private static final int COL = 1;

    private int row;
    private int col;

    public Position(String position) throws IllegalCoordinatesException {
        setCoordinates(position);
    }

    public Position(Position position) {
        this.row = position.getRow();
        this.col = position.getCol();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private void setCoordinates(String position) throws IllegalCoordinatesException {
        String realPosition = position.toUpperCase();
        char row = realPosition.charAt(ROW);
        String col = position.substring(COL);
        this.row = getRow(row);
        this.col = getCol(col);
    }

    /**
     * Get row index.
     **/
    private int getRow(char row) throws IllegalCoordinatesException {
        if ('A' > row || row > 'J') {
            throw new IllegalCoordinatesException(INCORRECT_COORDINATES);
        }

        return row - startPosition;
    }

    /**
     * Get column index.
     **/
    private int getCol(String col) throws IllegalCoordinatesException {
        int startPosition = 1;

        try {
            return Integer.parseInt(col) - startPosition; // Transform the string to int.
        } catch (NumberFormatException e) {
            throw new IllegalCoordinatesException(INCORRECT_COORDINATES);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return row == position.row &&
                col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
