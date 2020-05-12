package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class ListGame {

    private ServerCommand serverCommand;
    private List<GameRoom> pendingGames;
    private List<GameRoom> activeGames;

    public ListGame(ServerCommand serverCommand, List<GameRoom> activeGames, Queue<GameRoom> pendingGames) {
        this.serverCommand = serverCommand;
        this.activeGames = activeGames;
        this.pendingGames = new ArrayList<>(pendingGames);
    }

    public List<GameRoom> execute() {
        return serverCommand.listGames(activeGames, pendingGames);
    }
}
