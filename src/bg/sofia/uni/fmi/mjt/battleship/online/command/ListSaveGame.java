package bg.sofia.uni.fmi.mjt.battleship.online.command;

import java.util.List;

public class ListSaveGame {

    private ServerCommand serverCommand;
    private String playerName;

    public ListSaveGame(ServerCommand serverCommand, String playerName) {
        this.serverCommand = serverCommand;
        this.playerName = playerName;
    }

    public List<String> execute() {
        return serverCommand.listSaveGames(playerName);
    }
}
