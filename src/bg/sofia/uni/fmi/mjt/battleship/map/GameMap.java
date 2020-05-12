package bg.sofia.uni.fmi.mjt.battleship.map;

import bg.sofia.uni.fmi.mjt.battleship.exception.IllegalMapException;

import java.io.Serializable;
import java.util.Arrays;

public class GameMap implements Serializable {

    private static final long serialVersionUID = 123L;

    private static final String INCORRECT_MAP = "The map can't be null!";
    private static final String INIT_DEFAULT_MAP = "The map couldn't be copied.\nA default map was created.";
    private static final char INCORRECT_FIELD = '#';

    private static final int ZERO = 0;
    private static final int TEN = 10;

    private char[][] map;

    public GameMap() {
        initDefaultMap();
    }

    /**
     * Create a new map with the given @code map.
     * If the given @code map is null
     * then create a default map.
     **/
    public GameMap(char[][] map) {
        try {
            if (map == null) {
                throw new IllegalMapException(INCORRECT_MAP);
            }
            this.map = copyMap(map);
        } catch (IllegalMapException e) {
            System.out.println(INIT_DEFAULT_MAP);
            initDefaultMap();
        }
    }

    /**
     * Get the field from the map.
     **/
    public char getAtPosition(int row, int col) {
        if (isPositionCorrect(row, col)) {
            return map[row][col];
        }

        return INCORRECT_FIELD;
    }

    /**
     * Set the symbol on the map.
     **/
    public void setAtPosition(int row, int col, char symbol) {
        if (isPositionCorrect(row, col)) {
            map[row][col] = symbol;
        }
    }

    /**
     * Return the map.
     **/
    public char[][] getMap() {
        return map;
    }

    /**
     * Return the map with ships position.
     **/
    public char[][] getHideMap() {
        return hideShips();
    }


    /**
     * Print the map.
     **/
    public static void printMap(char[][] map) {
        if (map == null) {
            map = new GameMap().getMap();
        }

        printNumbers();

        for (int i = ZERO; i < TEN; ++i) {
            char s = (char) ('A' + i);
            System.out.print(s + " ");
            for (int j = ZERO; j < TEN; ++j) {
                System.out.print("|" + map[i][j]);
            }
            System.out.print("|\n");
        }
    }

    /**
     * Print the hidden map.
     **/
    public void printHiddenMap() {
        printNumbers();

        for (int i = ZERO; i < TEN; ++i) {
            char s = (char) ('A' + i);
            System.out.print(s + " ");
            for (int j = ZERO; j < TEN; ++j) {
                if (map[i][j] != '*') {
                    System.out.print("|" + map[i][j]);
                } else {
                    System.out.print("|-");
                }
            }
            System.out.print("|\n");
        }
    }

    private static void printNumbers() {
        System.out.println();
        System.out.print("   ");
        for (int i = 1; i <= TEN; ++i) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /**
     * Hide the ships position.
     **/
    private char[][] hideShips() {
        char[][] newMap = copyMap(getMap());
        char shipField = '*';
        char emptyField = '-';

        for (int i = ZERO; i < TEN; ++i) {
            for (int j = ZERO; j < TEN; ++j) {
                if (newMap[i][j] == shipField) {
                    newMap[i][j] = emptyField;
                }
            }
        }

        return newMap;
    }

    /**
     * Create a default map.
     **/
    private void initDefaultMap() {
        map = new char[TEN][TEN];
        fillMap();
    }

    /**
     * Copy the given map.
     **/
    private char[][] copyMap(char[][] map) {
        return Arrays.stream(map)
                .map(char[]::clone)
                .toArray(char[][]::new);
    }

    /**
     * Fill the map with empty field.
     **/
    private void fillMap() {
        final char emptySpace = '-';
        for (char[] row : map) {
            Arrays.fill(row, emptySpace);
        }
    }

    /**
     * Check if the position is in limit of the map.
     **/
    private boolean isPositionCorrect(int row, int col) {
        if (row < ZERO || row >= TEN) {
            return false;
        } else {
            return col >= ZERO && col < TEN;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameMap gameMap = (GameMap) o;
        return Arrays.deepEquals(map, gameMap.map);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(map);
    }
}
