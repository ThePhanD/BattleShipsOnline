package bg.sofia.uni.fmi.mjt.battleship.files;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.exception.NonExistBattleShipFileException;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileReaderTest {

    private String playerName;
    private String gameName;
    private BattleShipFile battleShipFile;

    @Before
    public void setUpToWriteFile() throws InterruptFileSaveException {
        this.playerName = "test";
        this.gameName = "testGame";
        String playerOneLastTurn = "A1";
        String playerTwoLastTurn = "B2";
        BattleShipHub playerOneHub = new BattleShipHub();
        BattleShipHub playerTwoHub = new BattleShipHub();
        playerOneHub.attack("B2");
        playerTwoHub.attack("A1");
        this.battleShipFile = new BattleShipFile(playerOneLastTurn, playerOneHub, playerTwoLastTurn, playerTwoHub);
        FileWriter.writeToFile(playerName, gameName, playerOneLastTurn, playerOneHub, playerTwoLastTurn, playerTwoHub);
    }

    @Test
    public void testFileReader() throws NonExistBattleShipFileException, InterruptFileSaveException {
        BattleShipFile actual = FileReader.readFromFile(playerName, gameName);
        BattleShipFile expected = battleShipFile;
        final String message = "The battle ship file is incorrect!";

        assertEquals(message, expected, actual);
    }

    @Test(expected = NonExistBattleShipFileException.class)
    public void testNotExistingFileReader() throws NonExistBattleShipFileException, InterruptFileSaveException {
        FileReader.readFromFile(playerName, "unknownGame");
        final String message = "The battle ship file shouldn't be readable!";
        System.out.println(message);
    }

    @Test
    public void testFileReaderBattleShipFile() throws NonExistBattleShipFileException, InterruptFileSaveException {
        BattleShipFile actual = FileReader.readFromFile(playerName, gameName);
        BattleShipFile expected = battleShipFile;
        final String message = "The battle ship file is incorrect!";

        assertEquals(message, expected.getPlayerOneBattleShipHub(), actual.getPlayerOneBattleShipHub());
        assertEquals(message, expected.getPlayerTwoBattleShipHub(), actual.getPlayerTwoBattleShipHub());
        assertEquals(message, expected.getPlayerOneLastTurn(), actual.getPlayerOneLastTurn());
        assertEquals(message, expected.getPlayerTwoLastTurn(), actual.getPlayerTwoLastTurn());
    }
}
