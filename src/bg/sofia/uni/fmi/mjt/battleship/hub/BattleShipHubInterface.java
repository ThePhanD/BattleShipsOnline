package bg.sofia.uni.fmi.mjt.battleship.hub;

import bg.sofia.uni.fmi.mjt.battleship.map.GameMapEngine;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;

public interface BattleShipHubInterface {

    /**
     * Place a ship on the map.
     **/
    boolean placeShip(Ship ship, String startPosition, String endPosition);

    /**
     * Change ship position to a new position on the map.
     **/
    boolean changeShipPosition(String shipPosition, String newStartPosition, String newEndPosition);

    /**
     * Hit a field on the map.
     **/
    boolean attack(String position);

    /**
     * Repair a damaged ship.
     **/
    boolean repairShip(String position);

    /**
     * Get a 3x3 scan fields with center @code position.
     **/
    char[][] scan(String position);

    /**
     * Get a GameMapEngine.
     **/
    GameMapEngine getGameMapEngine();

    /**
     * Check if the game is over.
     **/
    boolean isGameOver();
}