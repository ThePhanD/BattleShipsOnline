package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;

public class ControlPanel {

    private static final String MOVE = "move";
    private static final String PLACE = "place";
    private static final String ATTACK = "attack";
    private static final String REPAIR = "repair";
    private static final String SCAN = "scan";

    private static final int PLACE_ARG_COUNT = 4;
    private static final int MOVE_ARG_COUNT = 4;
    private static final int ATTACK_ARG_COUNT = 2;
    private static final int REPAIR_ARG_COUNT = 2;
    private static final int SCAN_ARG_COUNT = 2;

    private static final int ACTION_NAME = 0;

    public boolean isPlaceShipAction(String action) {
        String[] tokens = getTokens(action);
        return tokens[ACTION_NAME].equalsIgnoreCase(PLACE) && tokens.length == PLACE_ARG_COUNT;
    }

    public boolean isMoveShipAction(String action) {
        String[] tokens = getTokens(action);
        return tokens[ACTION_NAME].equalsIgnoreCase(MOVE) && tokens.length == MOVE_ARG_COUNT;
    }

    public boolean isAttackAction(String action) {
        String[] tokens = getTokens(action);
        return tokens[ACTION_NAME].equalsIgnoreCase(ATTACK) && tokens.length == ATTACK_ARG_COUNT;
    }

    public boolean isRepairShipAction(String action) {
        String[] tokens = getTokens(action);
        return tokens[ACTION_NAME].equalsIgnoreCase(REPAIR) && tokens.length == REPAIR_ARG_COUNT;
    }

    public boolean isScanMapAction(String action) {
        String[] tokens = getTokens(action);
        return tokens[ACTION_NAME].equalsIgnoreCase(SCAN) && tokens.length == SCAN_ARG_COUNT;
    }


    public boolean placeShip(BattleShipHub battleShipHub, Ship ship,
                             String startPosition, String endPosition) {
        return battleShipHub.placeShip(ship, startPosition, endPosition);
    }

    public boolean moveShip(BattleShipHub battleShipHub, String shipPosition,
                            String newStartPosition, String newEndPosition) {
        return battleShipHub.changeShipPosition(shipPosition, newStartPosition, newEndPosition);
    }

    public boolean repairShip(BattleShipHub battleShipHub, String shipPosition) {
        return battleShipHub.repairShip(shipPosition);
    }

    public boolean attack(BattleShipHub battleShipHub, String position) {
        return battleShipHub.attack(position);
    }

    public char[][] scanMap(BattleShipHub battleShipHub, String position) {
        return battleShipHub.scan(position);
    }

    private String[] getTokens(String input) {
        final String separator = " ";
        return input.split(separator);
    }
}
