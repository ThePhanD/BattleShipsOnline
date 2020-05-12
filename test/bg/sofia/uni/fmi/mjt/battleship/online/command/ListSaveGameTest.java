package bg.sofia.uni.fmi.mjt.battleship.online.command;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


public class ListSaveGameTest {

    private static final String PLAYER_NAME = "zahari";
    private static final int EXPECTED_SIZE = 3;

    private ServerCommand serverCommand = new ServerCommand();


    private List<String> getExpectedFileNames() {
        List<String> expectedFileNames = new ArrayList<>();
        expectedFileNames.add("alpha");
        expectedFileNames.add("firstGame");
        expectedFileNames.add("myGame");

        return expectedFileNames;
    }

    @Test
    public void testListSaveGamesIsNotNull() {
        ListSaveGame listSaveGame = new ListSaveGame(serverCommand, PLAYER_NAME);
        List<String> saveGames = listSaveGame.execute();
        final String message = "The list of save games can't be null!";

        assertNotNull(message, saveGames);
    }

    @Test
    public void testListSaveGame() {
        ListSaveGame listSaveGame = new ListSaveGame(serverCommand, PLAYER_NAME);
        List<String> actualSaveGames = listSaveGame.execute();
        List<String> expectedSaveGames = getExpectedFileNames();
        final String message = "The list of save games are incorrect!";

        assertThat(message, actualSaveGames, is(expectedSaveGames));
    }

    @Test
    public void testListSaveGameSize() {
        ListSaveGame listSaveGame = new ListSaveGame(serverCommand, PLAYER_NAME);
        List<String> actualSaveGames = listSaveGame.execute();
        int actualSize = actualSaveGames.size();
        final String message = "The number of save games is incorrect!";

        assertEquals(message, EXPECTED_SIZE, actualSize);
    }

    @Test
    public void testListSaveGameWithNonExistingPlayerName() {
        ListSaveGame listSaveGame = new ListSaveGame(serverCommand, "FMI");
        List<String> actualSaveGames = listSaveGame.execute();
        final String message = "The list of save games must be null!";

        assertNull(message, actualSaveGames);
    }

}
