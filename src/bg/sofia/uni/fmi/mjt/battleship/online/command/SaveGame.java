package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;

public class SaveGame implements Command  {

    private ServerCommand serverCommand;
    private String playerName;
    private String gameName;
    private String playerOneLastTurn;
    private BattleShipHub playerOneHub;
    private String playerTwoLastTurn;
    private BattleShipHub playerTwoHub;

    public SaveGame(ServerCommand serverCommand,
                    String playerName, String gameName,
                    String playerOneLastTurn, BattleShipHub playerOneHub,
                    String playerTwoLastTurn, BattleShipHub playerTwoHub) {

        this.playerName = playerName;
        this.serverCommand = serverCommand;
        this.gameName = gameName;
        this.playerOneLastTurn = playerOneLastTurn;
        this.playerOneHub = playerOneHub;
        this.playerTwoLastTurn = playerTwoLastTurn;
        this.playerTwoHub = playerTwoHub;
    }

    @Override
    public boolean execute() throws InterruptFileSaveException {
        return serverCommand.saveGame(
                playerName,
                gameName,
                playerOneLastTurn,
                playerOneHub,
                playerTwoLastTurn,
                playerTwoHub);
    }
}
