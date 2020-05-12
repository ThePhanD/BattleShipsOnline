package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.files.FileWriter;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DeleteGameTest {

    private static final String PLAYER_NAME = "testDelete";
    private static final String GAME_NAME = "deleteFile";

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
    public void testDeleteGame() {
        DeleteGame deleteGame = new DeleteGame(serverCommand, PLAYER_NAME, GAME_NAME);
        boolean actual = deleteGame.execute();
        final String message = "The save file can't exist anymore!";

        assertTrue(message, actual);
    }

    @Test
    public void testDeleteNoneExistingGame() {
        DeleteGame deleteGame = new DeleteGame(serverCommand, PLAYER_NAME, "emptyFile");
        boolean actual = deleteGame.execute();
        final String message = "The save file can't be deleted because it doesn't exist!";

        assertFalse(message, actual);
    }
}
