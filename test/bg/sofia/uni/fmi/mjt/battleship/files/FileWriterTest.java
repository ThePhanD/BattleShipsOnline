package bg.sofia.uni.fmi.mjt.battleship.files;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FileWriterTest {

    private String playerName;
    private String gameName;
    private String playerOneLastTurn;
    private String playerTwoLastTurn;
    private BattleShipHub playerOneHub;
    private BattleShipHub playerTwoHub;

    @Before
    public void setUpToWriteFile() {
        this.playerName = "test";
        this.gameName = "testGame";
        this.playerOneLastTurn = "A1";
        this.playerTwoLastTurn = "B2";
        this.playerOneHub = new BattleShipHub();
        this.playerTwoHub = new BattleShipHub();
        playerOneHub.attack("B2");
        playerTwoHub.attack("A1");
    }

    @Test
    public void testFileWriter() throws InterruptFileSaveException {
        FileWriter.writeToFile(playerName, gameName, playerOneLastTurn,
                playerOneHub, playerTwoLastTurn, playerTwoHub);

        boolean actualResult = BattleShipFile.isFileNameExist(playerName, gameName);
        final String message = "The file should exist!";

        assertTrue(message, actualResult);
    }

    @Test(expected = InterruptFileSaveException.class)
    public void testIncorrectFileWriter() throws InterruptFileSaveException {
        FileWriter.writeToFile(playerName, gameName, playerOneLastTurn, playerOneHub, null, null);
        final String message = "The file shouldn't exist!";

        System.out.println(message);
    }
}
