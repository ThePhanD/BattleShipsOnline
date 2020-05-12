package bg.sofia.uni.fmi.mjt.battleship.map;

public class GameMapFactory {

    /**
     * Get a default map.
     **/
    public GameMap buildMap() {
        return new GameMap();
    }

    /**
     * Get a copy of the @code map.
     **/
    public GameMap buildMap(char[][] map) {
        return new GameMap(map);
    }
}
