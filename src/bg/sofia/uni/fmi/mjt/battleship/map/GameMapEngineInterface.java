package bg.sofia.uni.fmi.mjt.battleship.map;

import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;

public interface GameMapEngineInterface {

    /**
     * Set a ship on the map with the given coordinates
     * @code rowX, colX, rowY, colY.
     **/
    boolean setShip(int rowX, int colX, int rowY, int colY, Ship ship);

    /**
     * Remove a ship on the map with the given coordinates
     * @code rowX, colX, rowY, colY.
     **/
    boolean removeShip(int rowX, int colX, int rowY, int colY, Ship ship);

    /**
     * Attack a field on the map with given @code row, col.
     **/
    boolean fireAtPosition(int row, int col);

    /**
     * Place a symbol on the map with given @code row, col.
     **/
    boolean placeAtPosition(int row, int col, char symbol);

    /**
     * Print the hidden map.
     **/
    void printHiddenMap();

    /**
     * Get the map.
     **/
    char[][] getMap();

    /**
     * Get the map without ships position.
     **/
    char[][] getHiddenMap();
}
