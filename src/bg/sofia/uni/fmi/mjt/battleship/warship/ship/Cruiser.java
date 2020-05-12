package bg.sofia.uni.fmi.mjt.battleship.warship.ship;

public class Cruiser extends Ship {

    private static final int SIZE = 4;
    private static final ShipType CRUISER = ShipType.CRUISER;

    public Cruiser(String name) {
        super(name, SIZE, CRUISER);
    }

}
