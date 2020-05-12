package bg.sofia.uni.fmi.mjt.battleship.online.game.server;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.files.BattleShipFile;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipGame;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.hub.action.*;
import bg.sofia.uni.fmi.mjt.battleship.map.GameMap;
import bg.sofia.uni.fmi.mjt.battleship.online.command.SaveGame;
import bg.sofia.uni.fmi.mjt.battleship.online.command.ServerCommand;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class GameServerHandler {

    private static final String PLAYER_CONNECTION_ERROR = "The player couldn't connect to the game!";
    private static final String CONNECTION_ERROR = "The connection to the game was impossible!";
    private static final String CLOSE_ERROR = "The server can't be closed!";

    private static final String INVALID_COMMAND = "Invalid command!";
    private static final String INVALID_NAME = "Choose another name!";
    private static final String GAME_SAVED = "The game was saved!";
    private static final String GAME_UNSAVED = "The game wasn't saved!";
    private static final String LAST_TURN = "Opponent last turn: ";
    private static final String UNKNOWN = "Unknown";
    private static final String SCAN = "scan";
    private static final String SAVE = "save";
    private static final String QUIT = "quit";
    private static final String EOF = "EOF";
    private static final String WINNER = "You won!";
    private static final String LOSER = "You lost!";
    private static final String FORMAT = "| %14s %4s |";

    private String playerOneName;
    private String playerTwoName;
    private String playerOneLastTurn;
    private String playerTwoLastTurn;
    private BattleShipHub playerOneHub;
    private BattleShipHub playerTwoHub;

    private int serverPort;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Scanner scanner;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ControlPanel controlPanel;
    private ServerCommand serverCommand;

    public GameServerHandler(Scanner scanner, int serverPort, String playerOneName) {
        setGameServer(scanner, serverPort, playerOneName, LAST_TURN + UNKNOWN, LAST_TURN + UNKNOWN);
        this.controlPanel = new ControlPanel();
        this.serverCommand = new ServerCommand();
    }

    public GameServerHandler(Scanner scanner, int serverPort, String playerOneName, BattleShipFile battleShipFile) {
        setGameServer(scanner, serverPort, playerOneName, battleShipFile.getPlayerOneLastTurn(),
                battleShipFile.getPlayerTwoLastTurn());
        this.playerOneHub = battleShipFile.getPlayerOneBattleShipHub();
        this.playerTwoHub = battleShipFile.getPlayerTwoBattleShipHub();
        this.controlPanel = new ControlPanel();
        this.serverCommand = new ServerCommand();
    }

    private void setGameServer(Scanner scanner, int serverPort, String playerOneName,
                               String playerOneLastTurn, String playerTwoLastTurn) {
        this.playerOneName = playerOneName;
        this.playerOneLastTurn = playerOneLastTurn;
        this.playerTwoLastTurn = playerTwoLastTurn;
        this.scanner = scanner;
        this.serverPort = serverPort;
    }

    public void loadGame() throws IOException {
        createGameServer(); //Create the server save game
        System.out.println("The game was loaded.");

        startGame(); //The game will start when the player one is ready

        waitPlayer(); //Wait for a player to join the game

        this.playerTwoName = reader.readLine(); // Receive player two name

        startGamePlay(); //Begin the turns

        closeConnection();

        System.out.println("You left the game!");
    }


    public void run() {
        createGameServer(); //Create the server game
        System.out.println("The game was created.");

        startGame(); //The game will start when the player one is ready

        setUpBattleShipHub(); //Set up the player one battleship game

        waitPlayer(); //Wait for a player to join the game

        getOpponentBattleShipHub(); //Received player two battleship set

        startGamePlay(); //Begin the turns

        closeConnection();

        System.out.println("You left the game!");
    }

    private void createGameServer() {
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.out.println(CONNECTION_ERROR);
            closeConnection();
        }
    }

    public void startGame() {
        final String start = "start";
        while (true) {
            System.out.println("Type <" + start + "> to start the game.");
            System.out.print("-> ");
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase(start)) {
                break;
            }
        }
    }

    private void setUpBattleShipHub() {
        BattleShipGame game = new BattleShipGame(scanner);
        game.initBattleShipGame();
        this.playerOneHub = game.getBattleShipHub();
    }

    private void waitPlayer() {
        try {
            System.out.println("Waiting for player...");

            clientSocket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            System.out.println("A player has joined.");
        } catch (IOException e) {
            System.out.println(PLAYER_CONNECTION_ERROR);
            closeConnection();
        }
    }

    private void getOpponentBattleShipHub() {
        System.out.println("Waiting opponent set up.\n");
        try {
            this.playerTwoName = reader.readLine(); // Receive player two name
            this.playerTwoHub = (BattleShipHub) objectInputStream.readObject(); // Receive player two hub
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("The battle ship set up couldn't be sent to server");
            closeConnection();
        }
    }

    private void getPlayerOneGameResult() {
        char[][] playerOneMap = playerOneHub.getGameMapEngine().getMap();
        char[][] playerTwoMap = playerTwoHub.getGameMapEngine().getHiddenMap();
        System.out.printf(FORMAT, "YOUR BOAR", "");
        GameMap.printMap(playerOneMap);
        System.out.println();
        System.out.printf(FORMAT, "ENEMY BOAR", "");
        GameMap.printMap(playerTwoMap);
        System.out.println("\n" + playerTwoLastTurn);
    }

    private void sendPlayerTwoGameResult() throws IOException {
        char[][] playerOneMap = playerOneHub.getGameMapEngine().getHiddenMap();
        char[][] playerTwoMap = playerTwoHub.getGameMapEngine().getMap();
        objectOutputStream.reset(); //free old reference
        objectOutputStream.writeObject(playerTwoMap);
        objectOutputStream.writeObject(playerOneMap);
        writer.println(playerOneLastTurn);
    }

    private void startGamePlay() {
        try {
            while (true) {
                printMenu();
                getPlayerOneGameResult(); // Show player One game result
                String message = executePlayerOneTurn(); // Player One turn
                if (isGameOver()) {
                    break;
                }

                if (message.equalsIgnoreCase(QUIT)) { // When player One left the game by typing <quit>
                    playerTwoLastRequest(); // Receive player Two last request
                    break;
                }

                sendPlayerTwoGameResult(); // Send player Two game result
                String inputLine = executePlayerTwoTurn(); // Player Two turn
                if (isGameOver()) {
                    break;
                }

                if (inputLine.equalsIgnoreCase(QUIT)) { // When player Two left the game by typing <quit>
                    playerOneLastRequest(); // Receive player One last request
                    break;
                }
            }
        } catch (IOException e) {
            closeConnection();
            System.out.println(CONNECTION_ERROR);
        }
    }

    private void updatePlayers() throws IOException {
        sendPlayerTwoGameResult();
        if (playerOneHub.isGameOver()) { // Player one lost the game
            writer.println(WINNER);
            writer.println(QUIT);
            getPlayerOneGameResult();
            System.out.println(LOSER);
        } else if (playerTwoHub.isGameOver()) { // Plyaer two lost the game
            writer.println(LOSER);
            writer.println(QUIT);
            getPlayerOneGameResult();
            System.out.println(WINNER);
        }
        System.out.println("The opponent Two left the game.");
    }

    private void printMenu() {
        String format = "| %-32s |\n";
        System.out.println();
        System.out.printf(format, "MENU");
        System.out.printf(format, "<attack position>");
        System.out.printf(format, "<move position newStart newEnd>");
        System.out.printf(format, "<repair position>");
        System.out.printf(format, "<scan position>");
        System.out.printf(format, "<save gamename>");
        System.out.println();
    }

    private String executePlayerOneTurn() throws IOException {
        while (true) {
            System.out.print("Your turn: ");
            String turn = scanner.nextLine();
            String message = executeTurn(turn, true);

            if (isGameOver()) {
                updatePlayers();
                return QUIT;
            }

            if (message.equalsIgnoreCase(SAVE)) {
                continue;
            } else if (!message.equalsIgnoreCase(INVALID_COMMAND)) {
                return message;
            }

            System.out.println(INVALID_COMMAND);
        }
    }

    private String executePlayerTwoTurn() throws IOException {
        System.out.println("Waiting opponent turn.");
        while (true) {
            String turn = reader.readLine();
            String message = executeTurn(turn, false);
            if (isGameOver()) {
                writer.println(EOF);
                updatePlayers();
                return QUIT;
            }

            if (message.equalsIgnoreCase(SAVE)) {
                continue;
            } else if (!message.equalsIgnoreCase(INVALID_COMMAND)) {
                writer.println(EOF);
                return message;
            }

            writer.println(INVALID_COMMAND);
        }
    }

    private String executeTurn(String turn, boolean isPlayerOne) throws IOException {
        if (turn.equalsIgnoreCase(QUIT)) {
            return QUIT;
        }

        if (controlPanel.isAttackAction(turn)) {
            if (isPlayerOne) {
                return executeAttackUtility(playerTwoHub, playerOneLastTurn, turn);
            }

            return executeAttackUtility(playerOneHub, playerTwoLastTurn, turn);
        } else if (controlPanel.isMoveShipAction(turn)) {
            if (isPlayerOne) {
                return executeMoveUtility(playerOneHub, playerOneLastTurn, turn);
            }

            return executeMoveUtility(playerTwoHub, playerTwoLastTurn, turn);
        } else if (controlPanel.isRepairShipAction(turn)) {
            if (isPlayerOne) {
                return executeRepairUtility(playerOneHub, playerOneLastTurn, turn);
            }

            return executeRepairUtility(playerTwoHub, playerTwoLastTurn, turn);
        } else if (controlPanel.isScanMapAction(turn)) {
            if (isPlayerOne) {
                return executePlayerOneScanMapUtility(turn);
            }

            return executePlayerTwoScanMapUtility(turn);
        } else if (serverCommand.isSaveCommand(turn)) {
            if (isPlayerOne) {
                executePlayerOneSave(turn);
            } else {
                executePlayerTwoSave(turn);
            }

            return SAVE;
        } else {
            return INVALID_COMMAND;
        }
    }

    private String[] splitToTokens(String sentence) {
        final String splitBy = " ";
        return sentence.split(splitBy);
    }

    private String updateLastTurn(String player, String lastTurn) {
        if (player == playerOneLastTurn) {
            this.playerOneLastTurn = LAST_TURN + lastTurn;
        } else {
            this.playerTwoLastTurn = LAST_TURN + lastTurn;
        }
        return LAST_TURN + lastTurn;
    }

    private String executeAttackUtility(BattleShipHub battleShipHub, String playerLastTurn, String turn) {
        String[] argv = splitToTokens(turn);
        Attack attack = new Attack(controlPanel, battleShipHub, argv[1]);

        if (attack.execute()) {
            return updateLastTurn(playerLastTurn, argv[1]);
        }

        return INVALID_COMMAND;
    }

    private String executeMoveUtility(BattleShipHub battleShipHub, String playerLastTurn, String turn) {
        String[] argv = splitToTokens(turn);
        MoveShip moveShip = new MoveShip(controlPanel, battleShipHub, argv[1], argv[2], argv[3]);

        if (moveShip.execute()) {
            return updateLastTurn(playerLastTurn, UNKNOWN);
        }

        return INVALID_COMMAND;
    }

    private String executeRepairUtility(BattleShipHub battleShipHub, String playerLastTurn, String turn) {
        String[] argv = splitToTokens(turn);
        RepairShip repairShip = new RepairShip(controlPanel, battleShipHub, argv[1]);

        if (repairShip.execute()) {
            return updateLastTurn(playerLastTurn, turn);
        }

        return INVALID_COMMAND;
    }

    private char[][] getScanMap(BattleShipHub battleShipHub, String turn) {
        String[] argv = splitToTokens(turn);
        ScanMap scanMap = new ScanMap(controlPanel, battleShipHub, argv[1]);
        return scanMap.execute();
    }

    private String executePlayerOneScanMapUtility(String turn) {
        char[][] scanPlayerTwoMap = getScanMap(playerTwoHub, turn);

        if (scanPlayerTwoMap != null) {
            System.out.printf(FORMAT, "SCAN MAP", "");
            GameMap.printMap(scanPlayerTwoMap);
            return updateLastTurn(playerOneLastTurn, SCAN);
        }

        return INVALID_COMMAND;
    }

    private String executePlayerTwoScanMapUtility(String turn) throws IOException {
        char[][] scanPlayerOneMap = getScanMap(playerOneHub, turn);

        if (scanPlayerOneMap != null) {
            writer.println(SCAN);
            objectOutputStream.writeObject(scanPlayerOneMap);
            return updateLastTurn(playerTwoLastTurn, SCAN);
        }

        return INVALID_COMMAND;
    }

    private void executePlayerOneSave(String command) {
        String gameName = splitToTokens(command)[1];
        if (BattleShipFile.isFileNameExist(playerOneName, gameName)) {
            System.out.println(INVALID_NAME);
            return;
        }
        SaveGame saveGame = new SaveGame(serverCommand, playerOneName, gameName, playerOneLastTurn,
                playerOneHub, playerTwoLastTurn, playerTwoHub);
        try {
            saveGame.execute();
            System.out.println(GAME_SAVED);

        } catch (InterruptFileSaveException e) {
            System.out.println(GAME_UNSAVED);
        }
    }

    private void executePlayerTwoSave(String command) {
        String gameName = splitToTokens(command)[1];
        SaveGame saveGame = new SaveGame(serverCommand, playerTwoName, gameName, playerTwoLastTurn,
                playerTwoHub, playerOneLastTurn, playerOneHub);
        try {
            saveGame.execute();
            writer.println(GAME_SAVED);

        } catch (InterruptFileSaveException e) {
            writer.println(GAME_UNSAVED);
        }
    }

    private boolean isGameOver() {
        if (playerOneHub.isGameOver()) {
            return true;
        }

        return playerTwoHub.isGameOver();
    }

    private void playerOneLastRequest() throws IOException {
        System.out.println(WINNER);
        while (true) {
            System.out.println("Type <save gamename> or <quit>.");
            System.out.print("-> ");
            String lastRequest = scanner.nextLine();

            if (lastRequest.equalsIgnoreCase(QUIT)) {
                break;
            } else if (serverCommand.isSaveCommand(lastRequest)) {
                String gameName = splitToTokens(lastRequest)[1];
                if (!BattleShipFile.isFileNameExist(playerOneName, gameName)) {
                    executeTurn(lastRequest, true);
                    break;
                }
                System.out.println(INVALID_NAME);
                continue;
            }

            System.out.println(INVALID_COMMAND);
        }
    }

    private void playerTwoLastRequest() throws IOException {
        System.out.println(LOSER);
        System.out.println("Disconnecting from the game...");
        sendPlayerTwoGameResult();
        writer.println(QUIT);
        String turn = reader.readLine();
        executeTurn(turn, false);
    }

    private void closeConnection() {
        try {
            serverSocket.close();
            clientSocket.close();
            writer.close();
            reader.close();
            objectInputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.println(CLOSE_ERROR);
        }
    }
}
