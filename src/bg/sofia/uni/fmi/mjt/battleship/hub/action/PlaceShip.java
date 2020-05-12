package bg.sofia.uni.fmi.mjt.battleship.hub.action;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;

public class PlaceShip implements Action {

    private ControlPanel controlPanel;
    private BattleShipHub battleShipHub;
    private Ship ship;
    private String startPosition;
    private String endPosition;


    /**
     * Place a ship on the map with given
     * @code ship, @code startPosition and @code endPosition.
     **/
    public PlaceShip(ControlPanel controlPanel,
                     BattleShipHub battleShipHub, Ship ship,
                     String startPosition, String endPosition) {
        this.controlPanel = controlPanel;
        this.battleShipHub = battleShipHub;
        this.ship = ship;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    @Override
    public boolean execute() {
        return controlPanel.placeShip(battleShipHub, ship, startPosition, endPosition);
    }
}
