package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;

public class Attack implements Action {

    private ControlPanel controlPanel;
    private BattleShipHub battleShipHub;
    private String position;


    /**
     * Attack a field on the map with the given
     * @code controlPanel - to execute the action
     * @code battleshipHub and @code position
     **/
    public Attack(ControlPanel controlPanel, BattleShipHub battleShipHub, String position) {
        this.controlPanel = controlPanel;
        this.battleShipHub = battleShipHub;
        this.position = position;
    }

    @Override
    public boolean execute() {
        return controlPanel.attack(battleShipHub, position);
    }
}
