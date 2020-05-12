package bg.sofia.uni.fmi.mjt.battleship.warship;

import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Carrier;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Cruiser;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Destroyer;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Submarine;

public class ShipFactory {

    private static final String AIRCRAFT_CARRIER = ShipType.CARRIER.getName();
    private static final String HEAVY_CRUISER = ShipType.CRUISER.getName();
    private static final String LIGHT_DESTROYER = ShipType.DESTROYER.getName();
    private static final String FMI_SUBMARINE = ShipType.SUBMARINE.getName();

    public Ship buildShip(ShipType shipType) {
        if(shipType == null) {
            return null;
        }

        switch (shipType) {
            case CARRIER: return new Carrier(AIRCRAFT_CARRIER);
            case CRUISER: return new Cruiser(HEAVY_CRUISER);
            case DESTROYER: return new Destroyer(LIGHT_DESTROYER);
            case SUBMARINE: return new Submarine(FMI_SUBMARINE);
            default: return null;
        }
    }

    public Ship buildShip(String shipType) {
        if(shipType == null) {
            return null;
        }

        String ship = shipType.toUpperCase();
        switch (ship) {
            case "CARRIER": return new Carrier(AIRCRAFT_CARRIER);
            case "CRUISER": return new Cruiser(HEAVY_CRUISER);
            case "DESTROYER": return new Destroyer(LIGHT_DESTROYER);
            case "SUBMARINE": return new Submarine(FMI_SUBMARINE);
            default: return null;
        }
    }

}
