package bg.sofia.uni.fmi.mjt.battleship.online.game.server;

import bg.sofia.uni.fmi.mjt.battleship.files.BattleShipFile;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import org.junit.Before;
import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertNotNull;

public class GameServerTest {

    private static final String PLAYER_NAME = "test";
    private static final int PORT = 1111;

    private GameServer gameServer;
    private Scanner scanner;

    @Before
    public void setUp() {
        scanner = new Scanner(System.in);
    }

    @Test
    public void testConstructedGameServerIsNotNull() {
        gameServer = new GameServer(scanner, PORT, PLAYER_NAME);
        final String message = "The constructed game server can't be null!";
        assertNotNull(message, gameServer);
    }

    private BattleShipFile getBattleShipFile() {
        final String lastTurn = "unknown";
        BattleShipHub hub = new BattleShipHub();
        return new BattleShipFile(lastTurn, hub, lastTurn, hub);
    }
    @Test
    public void testConstructedLoadGameServerIsNotNull() {
        BattleShipFile file = getBattleShipFile();
        gameServer = new GameServer(scanner, PORT, PLAYER_NAME, file);
        final String message = "The constructed load game server can't be null!";
        assertNotNull(message, gameServer);
    }
}
