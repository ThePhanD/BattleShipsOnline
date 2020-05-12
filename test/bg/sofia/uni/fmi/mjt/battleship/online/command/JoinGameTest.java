package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class JoinGameTest {

    private ServerCommand serverCommand = new ServerCommand();
    private Queue<GameRoom> pendingGames;

    @Before
    public void preparePendingGames() {
        this.pendingGames = new LinkedList<>();
        addPendingGames();
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
    public void testJoinRandomGame() {
        JoinGame joinGame = new JoinGame(serverCommand, pendingGames);
        GameRoom expectedGameRoom = new GameRoom("join-me", "zahari", 1245, false);
        GameRoom actualGameRoom = joinGame.execute();
        final String message = "The random game room is incorrect!";

        assertEquals(message, expectedGameRoom, actualGameRoom);
    }

    @Test
    public void testJoinNoneExistingGame() {
        String noneExistingGameName = "Summer without FMI";
        JoinGame joinGame = new JoinGame(serverCommand, pendingGames, noneExistingGameName);
        GameRoom actualGameRoom = joinGame.execute();
        final String message = "The none existing game room must be null";

        assertNull(message, actualGameRoom);
    }

    @Test
    public void testJoinExistingGame() {
        final String existingGameName = "join-me-4";
        JoinGame joinGame = new JoinGame(serverCommand, pendingGames, existingGameName);
        GameRoom expectedGameRoom = new GameRoom("join-me-4", "dimiter", 7643, true);
        GameRoom actualGameRoom = joinGame.execute();
        final String message = "The existing game room is incorrect!";


        assertEquals(message, expectedGameRoom, actualGameRoom);
    }

    @Test
    public void testPendingGamesNumbersAfterJoin() {
        JoinGame joinGame = new JoinGame(serverCommand, pendingGames);
        joinGame.execute();
        final int expecdNumber = 3;
        int actualNumber = pendingGames.size();
        final String message = "The numbers of pending games isn't correct after join!";

        assertEquals(message, expecdNumber, actualNumber);
    }
}
