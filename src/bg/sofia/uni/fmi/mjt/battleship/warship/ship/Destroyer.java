package bg.sofia.uni.fmi.mjt.battleship.warship.ship;

public class Destroyer extends Ship {

    private static final int SIZE = 3;
    private static final ShipType DESTROYER = ShipType.DESTROYER;

    public Destroyer(String name) {
        super(name, SIZE, DESTROYER);
    }

}
