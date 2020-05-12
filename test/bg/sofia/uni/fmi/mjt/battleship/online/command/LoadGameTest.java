package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.files.FileWriter;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoadGameTest {

    private static final String PLAYER_NAME = "testLoad";
    private static final String GAME_NAME = "saveFile";

    private ServerCommand serverCommand = new ServerCommand();

    @Before
    public void prepareFile() throws InterruptFileSaveException {
        String playerOneLastTurn = "A1";
        String playerTwoLastTurn = "B3";
        BattleShipHub playerOneHub = new BattleShipHub();
        BattleShipHub playerTwoHub = new BattleShipHub();

        FileWriter.writeToFile(PLAYER_NAME, GAME_NAME, playerOneLastTurn, playerOneHub, playerTwoLastTurn, playerTwoHub);
    }

    @Test
    public void testLoadGame() {
        LoadGame loadGame = new LoadGame(serverCommand, PLAYER_NAME, GAME_NAME);
        boolean actual = loadGame.execute();
        final String message = "The save file must exist!";

        assertTrue(message, actual);
    }

    @Test
    public void testLoadNoneExistingGame() {
        DeleteGame deleteGame = new DeleteGame(serverCommand, PLAYER_NAME, "emptyFile");
        boolean actual = deleteGame.execute();
        final String message = "The save file can't be loaded because it doesn't exist!";

        assertFalse(message, actual);
    }
}
