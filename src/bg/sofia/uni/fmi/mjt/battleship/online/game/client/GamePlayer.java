package bg.sofia.uni.fmi.mjt.battleship.online.game.client;

import java.util.Scanner;

public class GamePlayer {

    private GamePlayerConnector gamePlayerConnector;

    public GamePlayer(Scanner scanner, int serverPort, String playerName) {
        gamePlayerConnector = new GamePlayerConnector(scanner, serverPort, playerName,  false);
    }

    public GamePlayer(Scanner scanner, int serverPort, String playerName, boolean isLoadGame) {
        gamePlayerConnector = new GamePlayerConnector(scanner, serverPort, playerName, isLoadGame);
    }

    public void startGame() {
        gamePlayerConnector.run();
    }

}
