package bg.sofia.uni.fmi.mjt.battleship.hub;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.Assert.*;

public class BattleShipGameTest {

    private static final String PLACE_CARRIER = "place carrier A1 A5\n";
    private static final String PLACE_CRUISER_ONE = "place cruiser B1 B4\n";
    private static final String PLACE_CRUISER_TWO = "place cruiser C1 C4\n";
    private static final String PLACE_DESTROYER_ONE = "place destroyer D1 D3\n";
    private static final String PLACE_DESTROYER_TWO = "place destroyer E1 E3\n";
    private static final String PLACE_DESTROYER_THREE = "place destroyer F1 F3\n";
    private static final String PLACE_SUBMARINE_ONE = "place submarine G1 G2\n";
    private static final String PLACE_SUBMARINE_TWO = "place submarine H1 H2\n";
    private static final String PLACE_SUBMARINE_THREE = "place submarine H3 H4\n";
    private static final String PLACE_SUBMARINE_FOUR = "place submarine J1 J2\n";

    private InputStream in;
    private BattleShipHub battleShipHub;

    @Before
    public void setUp() {
        this.in = new ByteArrayInputStream((PLACE_CARRIER +
                PLACE_CRUISER_ONE + PLACE_CRUISER_TWO +
                PLACE_DESTROYER_ONE + PLACE_DESTROYER_TWO +
                PLACE_DESTROYER_THREE + PLACE_SUBMARINE_ONE +
                PLACE_SUBMARINE_TWO + PLACE_SUBMARINE_THREE +
                PLACE_SUBMARINE_FOUR).getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        BattleShipGame battleShipGame = new BattleShipGame(scanner);
        this.battleShipHub = battleShipGame.initBattleShipGame();

    }

    private void setUpShips() {
        this.in = new ByteArrayInputStream((PLACE_CARRIER +
                PLACE_CRUISER_ONE + PLACE_CRUISER_TWO +
                PLACE_DESTROYER_ONE + PLACE_DESTROYER_TWO +
                PLACE_DESTROYER_THREE + PLACE_SUBMARINE_ONE +
                PLACE_SUBMARINE_TWO + PLACE_SUBMARINE_THREE +
                PLACE_SUBMARINE_FOUR).getBytes());
        System.setIn(in);
    }

    @Test
    public void testBattleShipGame() {
        setUpShips();
        Scanner scanner = new Scanner(System.in);
        BattleShipGame battleShipGame = new BattleShipGame(scanner);
        battleShipGame.initBattleShipGame();
        BattleShipHub actualHub = battleShipGame.getBattleShipHub();
        String message = "The map is incorrect";

        assertArrayEquals(message, battleShipHub.getGameMapEngine().getMap(),
                actualHub.getGameMapEngine().getMap());
    }

    private void setUpShipsTwo() {
        this.in = new ByteArrayInputStream((PLACE_CARRIER +
                PLACE_CRUISER_ONE + PLACE_CRUISER_TWO +
                PLACE_DESTROYER_ONE + PLACE_DESTROYER_TWO +
                PLACE_DESTROYER_THREE + PLACE_SUBMARINE_ONE +
                PLACE_SUBMARINE_TWO + "place submarine I1 I2\n" +
                "move I1 H3 H4\n" + PLACE_SUBMARINE_FOUR).getBytes());
        System.setIn(in);
    }

    @Test
    public void testBattleShipGameWithChangeShipPosition() {
        setUpShipsTwo();
        Scanner scanner = new Scanner(System.in);
        BattleShipGame battleShipGame = new BattleShipGame(scanner);
        battleShipGame.initBattleShipGame();
        BattleShipHub actualHub = battleShipGame.getBattleShipHub();
        String message = "The map is incorrect";

        assertArrayEquals(message, battleShipHub.getGameMapEngine().getMap(),
                actualHub.getGameMapEngine().getMap());
    }
}
