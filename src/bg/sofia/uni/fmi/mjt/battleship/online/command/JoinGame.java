package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;

import java.util.Queue;

public class JoinGame {

    private ServerCommand serverCommand;
    private String gameName;
    private Queue<GameRoom> pendingGames;

    public JoinGame(ServerCommand serverCommand, Queue<GameRoom> pendingGames) {
        this.serverCommand = serverCommand;
        this.pendingGames = pendingGames;
    }

    public JoinGame(ServerCommand serverCommand, Queue<GameRoom> pendingGames, String gameName) {
        this.serverCommand = serverCommand;
        this.gameName = gameName;
        this.pendingGames = pendingGames;
    }

    public GameRoom execute() {
        return serverCommand.joinGame(gameName, pendingGames);
    }
}
