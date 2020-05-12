package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;

public class MoveShip implements Action {

    private ControlPanel controlPanel;
    private BattleShipHub battleShipHub;
    private String shipPosition;
    private String newStartPosition;
    private String newEndPosition;


    /**
     * Move ship from @code shipPosition to
     * @code newStartPosition and @code newEndPosition.
     **/
    public MoveShip(ControlPanel controlPanel, BattleShipHub battleShipHub,
                    String shipPosition, String newStartPosition, String newEndPosition) {
        this.controlPanel = controlPanel;
        this.battleShipHub = battleShipHub;
        this.shipPosition = shipPosition;
        this.newStartPosition = newStartPosition;
        this.newEndPosition = newEndPosition;
    }

    @Override
    public boolean execute() {
        return controlPanel.moveShip(battleShipHub, shipPosition, newStartPosition, newEndPosition);
    }
}
