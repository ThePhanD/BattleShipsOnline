package bg.sofia.uni.fmi.mjt.battleship.online.command;

public class LoadGame implements Command {

    private ServerCommand serverCommand;
    private String playerName;
    private String gameName;

    public LoadGame(ServerCommand serverCommand, String playerName, String gameName) {
        this.serverCommand = serverCommand;
        this.playerName = playerName;
        this.gameName = gameName;
    }

    @Override
    public boolean execute() {
        return serverCommand.loadGame(playerName, gameName);
    }
}
