package bg.sofia.uni.fmi.mjt.battleship.online.game.client;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Scanner;

import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class GamePlayerTest {

    private static final String PLAYER_NAME = "test";
    private static final int PORT = 1111;
    private static final boolean LOAD_GAME = true;

    private GamePlayer gamePlayer;
    private Scanner scanner;

    @Before
    public void setUp() {
        scanner = new Scanner(System.in);
    }

    @Test
    public void testConstructedGamePlayerIsNotNull() {
        gamePlayer = new GamePlayer(scanner, PORT, PLAYER_NAME);
        final String message = "The constructed game player client can't be null!";
        assertNotNull(message, gamePlayer);
    }

    @Test
    public void testConstructedLoadGamePlayerIsNotNull() {
        gamePlayer = new GamePlayer(scanner, PORT, PLAYER_NAME, LOAD_GAME);
        final String message = "The constructed load game player client can't be null!";
        assertNotNull(message, gamePlayer);
    }
}
