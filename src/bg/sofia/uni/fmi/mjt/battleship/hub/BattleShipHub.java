package bg.sofia.uni.fmi.mjt.battleship.hub;

import bg.sofia.uni.fmi.mjt.battleship.exception.IllegalCoordinatesException;
import bg.sofia.uni.fmi.mjt.battleship.exception.ShipUnavailableException;
import bg.sofia.uni.fmi.mjt.battleship.exception.UnrepairableShipException;
import bg.sofia.uni.fmi.mjt.battleship.map.GameMapEngine;
import bg.sofia.uni.fmi.mjt.battleship.position.Position;
import bg.sofia.uni.fmi.mjt.battleship.position.ShipPosition;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


public class BattleShipHub implements BattleShipHubInterface, Serializable {

    private static final long serialVersionUID = 12334567L;

    private static final String SHIP_UNREPAIRABLE = "The ship can't be repaired!";
    private static final String SHIP_UNCHANGEABLE = "The ship can't change his position!";
    private static final String SHIP_UNAVAILABLE = "This type of ship can't be placed anymore!";
    private static final String SHIP_NOT_EXIST = "This ship doesn't exist!";
    private static final String CHANGE_SHIP = "Please choose a different ship!";

    private int carrierNumbers = 1;
    private int cruiserNumbers = 2;
    private int destroyerNumbers = 3;
    private int submarineNumbers = 4;

    private int maxRepairs = 5;

    private GameMapEngine gameMapEngine;
    private Set<ShipPosition> ships;

    public BattleShipHub() {
        this.gameMapEngine = new GameMapEngine();
        this.ships = new HashSet<>();
    }

    public BattleShipHub(BattleShipHub hub) {
        GameMapEngine engine = hub.getGameMapEngine();
        this.gameMapEngine = new GameMapEngine(engine);
        this.ships = new HashSet<>();
        this.ships.addAll(hub.ships);
        this.carrierNumbers = hub.carrierNumbers;
        this.cruiserNumbers = hub.cruiserNumbers;
        this.destroyerNumbers = hub.destroyerNumbers;
        this.submarineNumbers = hub.submarineNumbers;
        this.maxRepairs = hub.maxRepairs;
    }

    @Override
    public boolean placeShip(Ship ship, String startPosition, String endPosition) {
        try {
            ShipPosition shipPosition = new ShipPosition(ship, startPosition, endPosition);
            // Check if this shipType is available and it doesn't been place on the map
            if (isShipAvailable(ship) && !ships.contains(shipPosition)) {

                //If the ship is successfully placed on the map then it is added to the game.
                if (modifyShipPosition(shipPosition, false)) { // false == place
                    changeShipAvailableNumber(ship, true); // Reduce the available shipType
                    ships.add(shipPosition);
                    return true;
                }
            }
        } catch (ShipUnavailableException e) {
            System.out.println(CHANGE_SHIP);
            return false;
        } catch (IllegalCoordinatesException e) {
            return false;
        }

        return false;
    }

    private boolean modifyShipPosition(ShipPosition shipPosition, boolean removeShip) {
        Position startPosition = shipPosition.getStartPosition();
        Position endPosition = shipPosition.getEndPosition();
        Ship ship = shipPosition.getShip();

        int startPositionRow = startPosition.getRow();
        int startPositionCol = startPosition.getCol();
        int endPositionRow = endPosition.getRow();
        int endPositionCol = endPosition.getCol();

        if (removeShip) { // Remove the ship from the map.
            return gameMapEngine.removeShip(startPositionRow, startPositionCol,
                    endPositionRow, endPositionCol, ship);
        } else { // Place the ship on the map.
            return gameMapEngine.setShip(startPositionRow, startPositionCol,
                    endPositionRow, endPositionCol, ship);
        }
    }

    @Override
    public boolean changeShipPosition(String shipPosition, String newStartPosition, String newEndPosition) {
        try {
            Position newPosition = new Position(shipPosition);
            ShipPosition oldShipPosition = getShipPositionAt(newPosition);

            if (oldShipPosition != null) {

                //If the ship can't be changed to the new position then the old ship position is added back.
                if (!changeShipToNewPosition(oldShipPosition, newStartPosition, newEndPosition)) {
                    ships.add(oldShipPosition);
                    throw new ShipUnavailableException(SHIP_UNCHANGEABLE);
                }

                return true;
            } else {
                throw new ShipUnavailableException(SHIP_UNCHANGEABLE);
            }
        } catch (ShipUnavailableException e) {
            System.out.println(CHANGE_SHIP);
            return false;
        } catch (IllegalCoordinatesException e) {
            return false;
        }
    }

    private boolean changeShipToNewPosition(ShipPosition oldShipPosition,
                                            String newStartPosition, String newEndPosition) {
        Ship ship = oldShipPosition.getShip();

        //Remove the ship old position.
        if (modifyShipPosition(oldShipPosition, true)) {
            changeShipAvailableNumber(ship, false); // Increate the count of this shipType
            if (!placeShip(ship, newStartPosition, newEndPosition)) {

                //If the ship can't be placed to the new position then it is placed to the old position.
                modifyShipPosition(oldShipPosition, false);
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean attack(String position) {
        try {
            Position newPosition = new Position(position);
            int row = newPosition.getRow();
            int col = newPosition.getCol();
            if (gameMapEngine.fireAtPosition(row, col)) {
                ShipPosition shipPosition = getShipPositionAt(newPosition); // Get a ship from the hit field
                if (shipPosition != null) {
                    attackShip(shipPosition); // If there is a ship then the ship takes damage.
                }
                return true;
            }

            return false;
        } catch (IllegalCoordinatesException e) {
            return false;
        }
    }

    private void attackShip(ShipPosition shipPosition) {
        Ship ship = shipPosition.getShip();
        ship.takeDamage(); // Ship health is reduced by 1.

        //If the ship is still alive then its added back.
        if (ship.isAlive()) {
            ships.add(shipPosition);
        }
    }

    @Override
    public boolean repairShip(String position) {
        if (maxRepairs == 0) { // If there is available repair then return false.
            return false;
        }

        try {
            Position newPosition = new Position(position);
            ShipPosition oldShipPosition = getShipPositionAt(newPosition);
            try {
                repairShipHealth(newPosition, oldShipPosition);
                maxRepairs--;
                return true;
            } catch (Exception e) {

                //If the ship can't be repaired then the old ship position is added back.
                if (oldShipPosition != null) {
                    ships.add(oldShipPosition);
                }

                return false;
            }
        } catch (IllegalCoordinatesException e) {
            return false;
        }
    }

    private void repairShipHealth(Position newPosition, ShipPosition shipPosition)
            throws UnrepairableShipException, ShipUnavailableException {
        int row = newPosition.getRow();
        int col = newPosition.getCol();
        char repairShip = '*';

        if (shipPosition != null) {
            Ship ship = shipPosition.getShip();

            //Repair the ship if possible or throw UnrepairableShipException.
            if (ship.repair() && gameMapEngine.placeAtPosition(row, col, repairShip)) {
                ships.add(shipPosition);
            } else {
                throw new UnrepairableShipException(SHIP_UNREPAIRABLE);
            }
        } else {
            throw new ShipUnavailableException(SHIP_NOT_EXIST);
        }
    }

    @Override
    public char[][] scan(String position) {
        char[][] realMap = gameMapEngine.getMap();
        try {
            Position newPosition = new Position(position);
            int row = newPosition.getRow();
            int col = newPosition.getCol();

            return scanMapAtPosition(realMap, row, col);
        } catch (IllegalCoordinatesException e) {
            return null;
        }

    }

    private char[][] scanMapAtPosition(char[][] realMap, int row, int col) {
        if (row < 0 || row > 10 || col < 0 || col > 10) {
            return null;
        }

        char[][] hiddenMap = gameMapEngine.getHiddenMap();

        final int zero = 0;
        final int one = 1;
        final int ten = 10;

        // Get 3x3 fields from the map.
        for (int i = row - one; i <= row + one; ++i) {
            for (int j = col - one; j <= col + one; ++j) {
                if ((i >= zero && i < ten) && (j >= zero && j < ten)) {
                    hiddenMap[i][j] = realMap[i][j];
                }
            }
        }

        return hiddenMap;
    }

    @Override
    public GameMapEngine getGameMapEngine() {
        return gameMapEngine;
    }

    @Override
    public boolean isGameOver() {
        return ships.isEmpty();
    }

    /**
     * Return a ship position if it is on the given @code position.
     * Or return null if there is no ship on the position.
     **/
    private ShipPosition getShipPositionAt(Position position) {
        Iterator<ShipPosition> iterator = ships.iterator();
        while (iterator.hasNext()) {
            ShipPosition sp = iterator.next();
            if (sp.isWithinShipField(position)) {
                iterator.remove();
                return sp;
            }
        }

        return null;
    }

    private boolean isShipAvailable(Ship ship) throws ShipUnavailableException {
        if (ship == null) {
            throw new ShipUnavailableException(SHIP_NOT_EXIST);
        }

        ShipType shipType = ship.getShipType();
        int shipNumber = getShipAvailableNumber(shipType);

        // If the limit of this ship type is reached throw ShipUnavailableException.
        if (shipNumber == 0) {
            throw new ShipUnavailableException(SHIP_UNAVAILABLE);
        } else if (shipNumber < 0) {
            throw new ShipUnavailableException(SHIP_NOT_EXIST);
        }

        return true;
    }

    public int getShipAvailableNumber(ShipType shipType) {
        switch (shipType) {
            case CARRIER:
                return carrierNumbers;
            case CRUISER:
                return cruiserNumbers;
            case DESTROYER:
                return destroyerNumbers;
            case SUBMARINE:
                return submarineNumbers;
            default:
                return -1;
        }
    }

    private void changeShipAvailableNumber(Ship ship, boolean reduce) {
        ShipType shipType = ship.getShipType();
        int i = 1;
        if (reduce) {
            i = -1;
        }

        switch (shipType) {
            case CARRIER:
                carrierNumbers = carrierNumbers + i;
                break;
            case CRUISER:
                cruiserNumbers = cruiserNumbers + i;
                break;
            case DESTROYER:
                destroyerNumbers = destroyerNumbers + i;
                break;
            case SUBMARINE:
                submarineNumbers = submarineNumbers + i;
                break;
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
        BattleShipHub that = (BattleShipHub) o;
        return carrierNumbers == that.carrierNumbers
                && cruiserNumbers == that.cruiserNumbers
                && destroyerNumbers == that.destroyerNumbers
                && submarineNumbers == that.submarineNumbers
                && maxRepairs == that.maxRepairs
                && Objects.equals(gameMapEngine, that.gameMapEngine)
                && Objects.deepEquals(ships, that.ships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carrierNumbers, cruiserNumbers, destroyerNumbers,
                submarineNumbers, maxRepairs, gameMapEngine, ships);
    }
}
