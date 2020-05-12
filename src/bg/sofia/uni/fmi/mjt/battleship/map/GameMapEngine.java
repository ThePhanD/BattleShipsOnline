package bg.sofia.uni.fmi.mjt.battleship.map;

import bg.sofia.uni.fmi.mjt.battleship.exception.IllegalCoordinatesException;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;

import java.io.Serializable;
import java.util.Objects;

public class GameMapEngine implements GameMapEngineInterface, Serializable {

    private static final long serialVersionUID = 12345678L;

    private static final String FIELD_INTERRUPTION = "The chosen field is interrupted!";
    private static final String FIELD_ALREADY_DESTROYED = "The chosen field is already destroyed, choose another one!";
    private static final String INCORRECT_SHIP_FIELD = "The chosen field isn't correct size for the ship!";
    private static final String INCORRECT_COORDINATES = "The coordinates must be between row A to J," +
            " and between col 1 to 10!";

    private static final char EMPTY_FIELD = '-';
    private static final char SHIP_FIELD = '*';
    private static final char HIT_SHIP_FIELD = 'X';
    private static final char HIT_EMPTY_FIELD = 'O';

    private GameMap map;

    public GameMapEngine() {
        GameMapFactory gameMapFactory = new GameMapFactory();
        this.map = gameMapFactory.buildMap();
    }

    public GameMapEngine(GameMap map) {
        char[][] newMap = map.getMap();
        copyMap(newMap);
    }

    public GameMapEngine(GameMapEngine gameMapEngine) {
        char[][] newMap = gameMapEngine.getMap();
        copyMap(newMap);
    }

    private void copyMap(char[][] toCopyMap) {
        GameMapFactory gameMapFactory = new GameMapFactory();
        this.map = gameMapFactory.buildMap(toCopyMap);
    }


    @Override
    public boolean setShip(int rowX, int colX, int rowY, int colY, Ship ship) {
        return modifyShipField(rowX, colX, rowY, colY, ship, EMPTY_FIELD, SHIP_FIELD);
    }

    @Override
    public boolean removeShip(int rowX, int colX, int rowY, int colY, Ship ship) {
        return modifyShipField(rowX, colX, rowY, colY, ship, SHIP_FIELD, EMPTY_FIELD);
    }

    @Override
    public boolean fireAtPosition(int row, int col) {
        char[][] oldMap = map.getMap();

        try {
            if (isCoordinatesCorrect(row, col)) {
                char fieldAt = map.getAtPosition(row, col);

                if (fieldAt == SHIP_FIELD) {
                    setField(row, col, col, true, HIT_SHIP_FIELD);
                } else if (fieldAt == EMPTY_FIELD) {
                    setField(row, col, col, true, HIT_EMPTY_FIELD);
                } else {
                    throw new IllegalCoordinatesException(FIELD_ALREADY_DESTROYED);
                }
            }
        } catch (IllegalCoordinatesException e) {
            updateMap(oldMap);
            return false;
        }

        return true;
    }

    @Override
    public boolean placeAtPosition(int row, int col, char symbol) {
        char[][] oldMap = map.getMap();
        try {
            if (isCoordinatesCorrect(row, col)) {
                setField(row, col, col, true, symbol);
            }
        } catch (IllegalCoordinatesException e) {
            updateMap(oldMap);
            return false;
        }

        return true;
    }

    /**
     * Print the hidden map.
     **/
    @Override
    public void printHiddenMap() {
        map.printHiddenMap();
    }

    /**
     * Get the map.
     **/
    @Override
    public char[][] getMap() {
        return map.getMap();
    }

    /**
     * Get the map without ships position.
     **/
    @Override
    public char[][] getHiddenMap() {
        return map.getHideMap();
    }

    /**
     * Change the map to the given @code map.
     **/
    private void updateMap(char[][] map) {
        this.map = new GameMap(map);
    }

    /**
     * Change ship field from given @code fromSymbol
     * to given @code toSymbol.
     **/
    private boolean modifyShipField(int rowX, int colX, int rowY, int colY, Ship ship, char fromSymbol, char toSymbol) {
        char[][] oldMap = map.getMap();
        int shipSize = ship.getSize() - 1;

        try {
            if (isPositionCoordinatesCorrect(rowX, colX, rowY, colY, shipSize, fromSymbol)) {
                changeShipFieldTo(rowX, colX, rowY, colY, toSymbol);
            }
        } catch (IllegalCoordinatesException e) {
            updateMap(oldMap);
            return false;
        }

        return true;
    }

    /**
     * Change the ship field on the map to the given @code symbol.
     **/
    private void changeShipFieldTo(int rowX, int colX, int rowY, int colY, char symbol) {
        if (rowX == rowY) {
            setField(rowX, colX, colY, true, symbol);
        } else if (colX == colY) {
            setField(colX, rowX, rowY, false, symbol);
        }
    }

    /**
     * Set the field base on the @code symbol.
     **/
    private void setField(int atLine, int x, int y, boolean checkRow, char symbol) {
        int from = Math.min(x, y);
        int to = Math.max(x, y);

        for (int i = from; i <= to; ++i) {
            if (checkRow) {
                map.setAtPosition(atLine, i, symbol);
            } else {
                map.setAtPosition(i, atLine, symbol);
            }
        }
    }

    /**
     * Check if the coordinates are correct.
     * if the coordinates aren't correct
     * then throw new IllegalCoordinatesException
     **/
    public boolean isCoordinatesCorrect(int row, int col) throws IllegalCoordinatesException {
        if (row >= 0 && row <= 9) {
            if (col >= 0 && col <= 9) {
                return true;
            }
        }
        throw new IllegalCoordinatesException(INCORRECT_COORDINATES);
    }

    /**
     * Check if the chosen position is correct.
     * if the chosen position  isn't correct
     * then throw new IllegalCoordinatesException
     **/
    private boolean isPositionCoordinatesCorrect(int rowX, int colX, int rowY, int colY, int shipSize, char symbol) throws IllegalCoordinatesException {
        isCoordinatesCorrect(rowX, colX);
        isCoordinatesCorrect(rowY, colY);

        if (rowX == rowY) {
            int fieldSize = Math.abs(colX - colY);
            if (isChosenFieldEnough(fieldSize, shipSize)) {
                return isShipFieldCorrect(rowX, colX, colY, true, symbol);
            }
        } else if (colX == colY) {
            int fieldSize = Math.abs(rowX - rowY);
            if (isChosenFieldEnough(fieldSize, shipSize)) {
                return isShipFieldCorrect(colX, rowX, rowY, false, symbol);
            }
        }

        throw new IllegalCoordinatesException(FIELD_INTERRUPTION);
    }

    /**
     * Check if the field size is equals ship size.
     * if the field size isn't correct
     * then throw new IllegalCoordinatesException
     **/
    private boolean isChosenFieldEnough(int shipSize, int fieldSize) throws IllegalCoordinatesException {
        if (fieldSize == shipSize) {
            return true;
        }
        throw new IllegalCoordinatesException(INCORRECT_SHIP_FIELD);
    }

    /**
     * Check if ship can be placed in these field.
     * If @code checkRow is true it is checking the row
     * if @code checkRow is false it is checking the col
     * if the field is different from @code symbol
     * then throw new IllegalCoordinatesException
     **/
    private boolean isShipFieldCorrect(int atLine, int x, int y, boolean checkRow, char symbol) throws IllegalCoordinatesException {
        int from = Math.min(x, y);
        int to = Math.max(x, y);
        for (int i = from; i <= to; ++i) {
            if (checkRow) {
                if (map.getAtPosition(atLine, i) != symbol) {
                    throw new IllegalCoordinatesException(FIELD_INTERRUPTION);
                }
            } else {
                if (map.getAtPosition(i, atLine) != symbol) {
                    throw new IllegalCoordinatesException(FIELD_INTERRUPTION);
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameMapEngine that = (GameMapEngine) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
