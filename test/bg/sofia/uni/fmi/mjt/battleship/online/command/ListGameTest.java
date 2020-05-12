package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class ListGameTest {

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
    public void testListGame() {
        ListGame listGame = new ListGame(serverCommand, activeGames, pendingGames);
        List<GameRoom> actualGameRooms = listGame.execute();
        List<GameRoom> expectedGameRooms = new ArrayList<>();
        expectedGameRooms.addAll(activeGames);
        expectedGameRooms.addAll(pendingGames);
        final String message = "The expected gamerooms are incorrect!";

        assertThat(message, actualGameRooms, is(expectedGameRooms));
    }

    @Test
    public void testListGameNumbers() {
        ListGame listGame = new ListGame(serverCommand, activeGames, pendingGames);
        List<GameRoom> actualGameRooms = listGame.execute();
        int actualNumber = actualGameRooms.size();
        final int expectedNumber = 6;
        final String message = "The expected gameroom numbers are incorrect!";

        assertEquals(message, expectedNumber, actualNumber);
    }
}

