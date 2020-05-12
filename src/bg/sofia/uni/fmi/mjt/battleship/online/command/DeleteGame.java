package bg.sofia.uni.fmi.mjt.battleship.online.command;

public class DeleteGame implements Command {

    private ServerCommand serverCommand;
    private String playerName;
    private String gameName;

    public DeleteGame(ServerCommand serverCommand, String playerName, String gameName) {
        this.serverCommand = serverCommand;
        this.playerName = playerName;
        this.gameName = gameName;
    }

    @Override
    public boolean execute() {
        return serverCommand.deleteGame(playerName, gameName);
    }
}
