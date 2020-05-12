package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;

public class RepairShip implements Action {

    private ControlPanel controlPanel;
    private BattleShipHub battleShipHub;
    private String shipPosition;


    /**
     * Repair a ship field on @code shipPosition
     **/
    public RepairShip(ControlPanel controlPanel, BattleShipHub battleShipHub, String shipPosition) {
        this.controlPanel = controlPanel;
        this.battleShipHub = battleShipHub;
        this.shipPosition = shipPosition;
    }

    @Override
    public boolean execute() {
        return controlPanel.repairShip(battleShipHub, shipPosition);
    }
}
