package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;

public class ScanMap {
    private ControlPanel controlPanel;
    private BattleShipHub battleShipHub;
    private String position;

    /**
     * Scan 3x3 fields from the map
     * with center @code position.
     **/
    public ScanMap(ControlPanel controlPanel, BattleShipHub battleShipHub, String position) {
        this.controlPanel = controlPanel;
        this.battleShipHub = battleShipHub;
        this.position = position;
    }

    public char[][] execute() {
        return controlPanel.scanMap(battleShipHub, position);
    }
}
