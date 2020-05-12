package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;


public class CreateGameTest {

    private ServerCommand serverCommand = new ServerCommand();
    private List<GameRoom> activeGames;
    private Queue<GameRoom> pendingGames;


    @Before
    public void prepareToCreateGame() {
        activeGames = new ArrayList<>();
        pendingGames = new LinkedList<>();
        addActiveGames();
        addPendingGames();
    }

    private void addActiveGames() {
        GameRoom room = new GameRoom("FMI", "Death", 3345, false);
        GameRoom room1 = new GameRoom("Next-Year", "FMI", 8821, true);

        room.increasePlayerNumber();
        room1.increasePlayerNumber();
        room.startGame();
        room1.startGame();

        activeGames.add(room);
        activeGames.add(room1);
    }

    private void addPendingGames() {
        GameRoom room = new GameRoom("join-me", "zahari", 1245, false);
        GameRoom room1 = new GameRoom("join-me-2", "ivan", 1321, true);
        GameRoom room2 = new GameRoom("join-me-3", "pesho", 5784, false);
        GameRoom room3 = new GameRoom("join-me-4", "dimiter", 7643, true);

        pendingGames.add(room);
        pendingGames.add(room1);
        pendingGames.add(room2);
        pendingGames.add(room3);
    }

    @Test
    public void testCreateGame() {
        GameRoom room = new GameRoom("first-game", "a.k.Op", 7777, false);
        CreateGame createGame = new CreateGame(serverCommand, room , activeGames, pendingGames);
        boolean actual = createGame.execute();
        final String message = "The game must be possible to be created!";

        assertTrue(message, actual);
    }

    @Test
    public void testCreateExistingGame() {
        GameRoom room = new GameRoom("join-me", "zahari", 1245, false);
        CreateGame createGame = new CreateGame(serverCommand, room , activeGames, pendingGames);
        boolean actual = createGame.execute();
        final String message = "The game mustn't be possible to be created!";

        assertFalse(message, actual);
    }

    @Test
    public void testCreateGameNumbers() {
        GameRoom room = new GameRoom("join-me-5", "krisi", 5457, true);
        CreateGame createGame = new CreateGame(serverCommand, room , activeGames, pendingGames);
        createGame.execute();
        final int expectedSize = 5;
        int actualSize = pendingGames.size();
        final String message = "The numbers of games are incorrect!";

        assertEquals(message, expectedSize, actualSize);
    }
}
