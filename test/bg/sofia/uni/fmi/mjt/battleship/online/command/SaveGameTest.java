package bg.sofia.uni.fmi.mjt.battleship.online.command;


import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.exception.NonExistBattleShipFileException;
import bg.sofia.uni.fmi.mjt.battleship.files.BattleShipFile;
import bg.sofia.uni.fmi.mjt.battleship.files.FileReader;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.warship.ShipFactory;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.Ship;
import bg.sofia.uni.fmi.mjt.battleship.warship.ship.ShipType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SaveGameTest {

    private ServerCommand serverCommand = new ServerCommand();
    private String playerName;
    private String gameName;
    private String playerOneLastTurn;
    private String playerTwoLastTurn;
    private BattleShipHub playerOneHub;
    private BattleShipHub playerTwoHub;

    @Before
    public void prepareToSaveGame() {
        this.playerName = "playerOne";
        this.gameName = "BestGame";
        this.playerOneLastTurn = "A1";
        this.playerTwoLastTurn = "B3";
        this.playerOneHub = getPlayerOneBattleShipHub();
        this.playerTwoHub = getPlayerTwoBattleShipHub();
    }

    @Test
    public void testSaveGame() throws InterruptFileSaveException, NonExistBattleShipFileException {
        SaveGame saveGame = new SaveGame(serverCommand, playerName, gameName,
                playerOneLastTurn, playerOneHub, playerTwoLastTurn, playerTwoHub);
        saveGame.execute();

        BattleShipFile expectedBattleShipFile = new BattleShipFile (playerOneLastTurn,
                playerOneHub, playerTwoLastTurn, playerTwoHub);
        BattleShipFile actualBattleShipFile = FileReader.readFromFile(playerName, gameName);
        final String message = "The battle ship file isn't saved correctly!";

        assertEquals(message, expectedBattleShipFile, actualBattleShipFile);
    }

    @Test(expected = InterruptFileSaveException.class)
    public void testIncorrectSaveGameWithNoneExistingPlayerNameSave() throws InterruptFileSaveException {
        SaveGame saveGame = new SaveGame(serverCommand, null, gameName,
                playerOneLastTurn, playerOneHub, playerTwoLastTurn, playerTwoHub);
        saveGame.execute();
        final String message = "If player name is null it should throw InterruptFileSaveException!";

        System.out.println(message);
    }

    private BattleShipHub getBattleShipHub() {
        ShipFactory factory = new ShipFactory();
        Ship ship = factory.buildShip(ShipType.DESTROYER);
        BattleShipHub hub = new BattleShipHub();
        hub.placeShip(ship, "A1", "A3");

        return hub;
    }

    private BattleShipHub getPlayerOneBattleShipHub() {
        BattleShipHub hubOne = getBattleShipHub();
        hubOne.attack("A1");
        hubOne.attack("A5");

        return hubOne;
    }

    private BattleShipHub getPlayerTwoBattleShipHub() {
        BattleShipHub hubTwo = getBattleShipHub();
        hubTwo.attack("A2");
        hubTwo.attack("A4");

        return hubTwo;
    }
}
