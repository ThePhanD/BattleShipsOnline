package bg.sofia.uni.fmi.mjt.battleship.warship.ship;

import java.io.Serializable;
import java.util.Objects;

public class Ship implements ShipInterface, Serializable {

    private static final long serialVersionUID = 123456L;

    private static final int NO_HEALTH = 0;

    private String id;
    private int size;
    private ShipType shipType;
    private int health;

    public Ship(String name, int size, ShipType shipType) {
        initShip(name, size, shipType, size);
    }

    public Ship(Ship ship) {
        initShip(ship.getId(), ship.getSize(), ship.getShipType(), ship.getHealth());
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ShipType getShipType() {
        return shipType;
    }

    @Override
    public boolean isAlive() {
        return health > NO_HEALTH;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void takeDamage() {
        if (isAlive()) {
            this.health--;
        }
    }

    @Override
    public boolean repair() {
        if(!isAlive()) { // A destroyed ship can't be repaired.
            return false;
        }

        int addOneHealth = 1;
        int oldHealth = getHealth() + addOneHealth;
        if(oldHealth > this.size) { // A full health ship can't be repaired.
            return false;
        }

        this.health++;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ship ship = (Ship) o;
        return size == ship.size &&
                health == ship.health &&
                Objects.equals(id, ship.id) &&
                shipType == ship.shipType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, size, shipType, health);
    }

    private void initShip(String name, int size, ShipType shipType, int health) {
        this.id = shipType.getNameId(name);
        this.size = size;
        this.shipType = shipType;
        setShipHealth(health);
    }

    private void setShipHealth(int health) {
        this.health = health;
    }
}
