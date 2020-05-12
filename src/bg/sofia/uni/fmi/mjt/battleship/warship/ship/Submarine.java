package bg.sofia.uni.fmi.mjt.battleship.warship.ship;

public class Submarine extends Ship {

    private static final int SIZE = 2;
    private static final ShipType SUBMARINE = ShipType.SUBMARINE;

    public Submarine(String name) {
        super(name, SIZE, SUBMARINE);
    }
}
