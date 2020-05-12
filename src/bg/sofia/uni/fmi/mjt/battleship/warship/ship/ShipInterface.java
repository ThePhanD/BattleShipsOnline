package bg.sofia.uni.fmi.mjt.battleship.warship.ship;

public interface ShipInterface {

    int getSize();

    String getId();

    ShipType getShipType();

    boolean isAlive();

    int getHealth();

    /**
     * Ship take 1 damage.
     **/
    void takeDamage();

    /**
     * Ship get 1 health.
     **/
    boolean repair();
}
