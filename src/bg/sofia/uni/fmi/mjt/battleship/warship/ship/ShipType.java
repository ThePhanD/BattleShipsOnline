package bg.sofia.uni.fmi.mjt.battleship.warship.ship;

public enum ShipType {

    CARRIER("CARRIER"),
    CRUISER("CRUISER"),
    DESTROYER("DESTROYER"),
    SUBMARINE("SUBMARINE");

    private final String name;
    private int currentCounter;

    ShipType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNameId(String name) {
        int current = currentCounter;
        currentCounter++;

        return String.format("%s-%d", name, current);
    }

}
