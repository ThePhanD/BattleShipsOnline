package bg.sofia.uni.fmi.mjt.battleship.online.game.server;

import bg.sofia.uni.fmi.mjt.battleship.files.BattleShipFile;

import java.io.IOException;
import java.util.Scanner;

public class GameServer {

    private GameServerHandler gameServerHandler;

    public GameServer(Scanner scanner, int serverPort, String playerName) {
        gameServerHandler = new GameServerHandler(scanner, serverPort, playerName);
    }

    public GameServer(Scanner scanner, int serverPort, String playerName, BattleShipFile battleShipFile) {
        gameServerHandler = new GameServerHandler(scanner, serverPort, playerName, battleShipFile);
    }

    public void createGame() {
        gameServerHandler.run();
    }

    public void loadGame() throws IOException {
        gameServerHandler.loadGame();
    }
}
