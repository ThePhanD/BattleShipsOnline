package bg.sofia.uni.fmi.mjt.battleship.hub;

import bg.sofia.uni.fmi.mjt.battleship.hub.action.ControlPanel;
import bg.sofia.uni.fmi.mjt.battleship.hub.action.MoveShip;
import bg.sofia.uni.fmi.mjt.battleship.hub.action.PlaceShip;
import bg.sofia.uni.fmi.mjt.battleship.map.GameMap;
import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BattleShipGame {

    private static final String INVALID_COMMAND = "Invalid command!";

    private static final ShipType CARRIER = ShipType.CARRIER;
    private static final ShipType CRUISER = ShipType.CRUISER;
    private static final ShipType DESTROYER = ShipType.DESTROYER;
    private static final ShipType SUBMARINE = ShipType.SUBMARINE;

    private int carrierCount;
    private int cruiserCount;
    private int destroyerCount;
    private int submarineCount;

    private BattleShipHub battleShipHub;
    private Map<Ship, Boolean> ships = new HashMap<>();
    private Scanner scanner;
    private ControlPanel controlPanel;

    public BattleShipGame(Scanner scanner) {
        this.scanner = scanner;
        this.battleShipHub = new BattleShipHub();
        this.controlPanel = new ControlPanel();
        updateShipAvailableCount();
        initShips();
    }

    public BattleShipHub getBattleShipHub() {
        return battleShipHub;
    }


    /**
     * Create every ship types and their counts.
     **/
    private void initShips() {
        createShips(CARRIER, carrierCount);
        createShips(CRUISER, cruiserCount);
        createShips(DESTROYER, destroyerCount);
        createShips(SUBMARINE, submarineCount);
    }

    /**
     * Add @code shipType to the game @code maxShipType times.
     **/
    private void createShips(ShipType shipType, int maxShipType) {
        ShipFactory factory = new ShipFactory();
        int zero = 0;
        for (int i = zero; i < maxShipType; ++i) {
            Ship ship = factory.buildShip(shipType);
            ships.put(ship, false);
        }
    }

    /**
     * Print up to date map.
     **/
    private void printUpdateMap() {
        char[][] map = battleShipHub.getGameMapEngine().getMap();
        GameMap.printMap(map);
    }

    /**
     * Create every ship types and their counts.
     **/
    public BattleShipHub initBattleShipGame() {
        System.out.println("Place your ship on the board.");
        printUpdateMap();

        setShipsPosition();

        return battleShipHub;
    }

    /**
     * Set every ship on the map.
     **/
    private void setShipsPosition() {
        while (isShipAvailable()) {
            printMenu();

            printAvailableShips();

            String command = getPlayerInput();

            executeCommand(command);

            printUpdateMap();
        }
    }

    private void printMenu() {
        System.out.println();
        String format = "| %-10s |\n";
        System.out.printf(format, "<place ship startPosition endPosition>");
        System.out.printf(format, "<move oldShipPosition newStartPosition newEndPosition>");
    }

    private String getPlayerInput() {
        System.out.print("\n-> ");
        return scanner.nextLine();
    }

    private void printAvailableShips() {
        System.out.println();
        updateShipAvailableCount();
        String format = "| %-10s | %9s |\n";
        System.out.printf(format, "ShipType", "Available");
        System.out.printf(format, "Carrier", carrierCount);
        System.out.printf(format, "Cruiser", cruiserCount);
        System.out.printf(format, "Destroyer", destroyerCount);
        System.out.printf(format, "Submarine", submarineCount);
    }

    private void executeCommand(String command) {
        String[] argv = command.split(" ");

        if (controlPanel.isPlaceShipAction(command)) {
            Ship ship = getShip(argv[1]); // Get a ship whit this ship type.
            PlaceShip placeShip = new PlaceShip(controlPanel, battleShipHub, ship, argv[2], argv[3]);
            if (placeShip.execute()) {
                ships.put(ship, true); // If the ship was placed on the map then it is added to ships.
            }
        } else if (controlPanel.isMoveShipAction(command)) {
            MoveShip moveShip = new MoveShip(controlPanel, battleShipHub, argv[1], argv[2], argv[3]);
            moveShip.execute();
        } else {
            System.out.println(INVALID_COMMAND);
        }
    }

    /**
     * Get a ship with given @code shipType.
     **/
    private Ship getShip(String shipType) {
        ShipType expectedType = getShipType(shipType);

        for (Map.Entry<Ship, Boolean> ship : ships.entrySet()) {
            ShipType actualType = ship.getKey().getShipType();

            boolean isNotAvailable = ship.getValue(); // Continue if the ship isn't available.
            if (isNotAvailable) {
                continue;
            }

            if (actualType == expectedType) { // Return the ship of expected type.
                return ship.getKey();
            }
        }

        return null;
    }

    private ShipType getShipType(String shipType) {
        switch (shipType) {
            case "carrier":
                return CARRIER;
            case "cruiser":
                return CRUISER;
            case "destroyer":
                return DESTROYER;
            case "submarine":
                return SUBMARINE;
            default: return null;
        }
    }

    // Return true if there is a avaialbe shipType
    private boolean isShipAvailable() {
        updateShipAvailableCount();
        return carrierCount > 0
                || cruiserCount > 0
                || destroyerCount > 0
                || submarineCount > 0;
    }

    // Update the count numbers of every shipType.
    private void updateShipAvailableCount() {
        this.carrierCount = battleShipHub.getShipAvailableNumber(CARRIER);
        this.cruiserCount = battleShipHub.getShipAvailableNumber(CRUISER);
        this.destroyerCount = battleShipHub.getShipAvailableNumber(DESTROYER);
        this.submarineCount = battleShipHub.getShipAvailableNumber(SUBMARINE);
    }
}
