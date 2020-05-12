package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;

import java.util.List;
import java.util.Queue;


public class CreateGame implements Command {

    private ServerCommand serverCommand;
    private GameRoom gameRoom;
    private List<GameRoom> activeGames;
    private Queue<GameRoom> pendingGames;

    public CreateGame(ServerCommand serverCommand, GameRoom gameRoom,
                      List<GameRoom> activeGames, Queue<GameRoom> pendingGames) {
        this.serverCommand = serverCommand;
        this.gameRoom = gameRoom;
        this.activeGames = activeGames;
        this.pendingGames = pendingGames;
    }

    @Override
    public boolean execute() {
        return serverCommand.createGame(gameRoom, activeGames, pendingGames);
    }
}
