package bg.sofia.uni.fmi.mjt.battleship.warship.ship;

public class Carrier extends Ship {

    private static final int SIZE = 5;
    private static final ShipType CARRIER = ShipType.CARRIER;

    public Carrier(String name) {
        super(name, SIZE, CARRIER);
    }

}
