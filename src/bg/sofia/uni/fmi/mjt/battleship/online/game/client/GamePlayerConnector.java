package bg.sofia.uni.fmi.mjt.battleship.online.game.client;

import bg.sofia.uni.fmi.mjt.battleship.files.BattleShipFile;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipGame;
import bg.sofia.uni.fmi.mjt.battleship.map.GameMap;
import bg.sofia.uni.fmi.mjt.battleship.online.command.ServerCommand;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GamePlayerConnector {

    private static final String INVALID_COMMAND = "Invalid command!";
    private static final String CONNECTION_ERROR = "The connection to the game was impossible!";
    private static final String CLOSE_ERROR = "The connection can't be closed!";
    private static final String NO_SERVER_RESPONSE = "The server wasn't able to reply!";

    private static final String FORMAT = "| %14s %4s |";

    private static final String WINNER = "You won!";
    private static final String LOSER = "You lost!";
    private static final String SCAN = "scan";
    private static final String QUIT = "quit";
    private static final String EOF = "EOF";

    private static final String SERVER_HOST = "localhost";

    private String playerName;
    private int serverPort;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Scanner scanner;
    private ObjectInput objectInput;
    private ObjectOutput objectOutput;
    private boolean isLoadGame;
    private ServerCommand serverCommand;

    public GamePlayerConnector(Scanner scanner, int serverPort, String playerName, boolean isLoadGame) {
        this.scanner = scanner;
        this.serverPort = serverPort;
        this.playerName = playerName;
        this.isLoadGame = isLoadGame;
        this.serverCommand = new ServerCommand();
    }

    public GamePlayerConnector(Scanner scanner, String playerName, int serverPort, Socket socket, PrintWriter writer,
                               BufferedReader reader, ObjectInput objectInput, ObjectOutput objectOutput,
                               boolean isLoadGame, ServerCommand serverCommand) {
        this.scanner = scanner;
        this.playerName = playerName;
        this.serverPort = serverPort;
        this.socket = socket;
        this.writer = writer;
        this.reader = reader;
        this.objectOutput = objectOutput;
        this.objectInput = objectInput;
        this.isLoadGame = isLoadGame;
        this.serverCommand = serverCommand;
    }

    public void run() {
        connectToServer(); //Create a connection to the server

        startGame(); //The game will start when the player two is ready

        if (!isLoadGame) {
            setUpBattleShipGame(); //Set up the player two battleship game
        }

        serverCommunication(); //Begins the turns

        closeConnection(); //End the game

        System.out.println("You left the game!");
    }

    private void connectToServer() {
        try {
            System.out.println("Connecting to the game...");

            socket = new Socket(SERVER_HOST, serverPort);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());

            System.out.println("Joined to the game.");
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
                writer.println(playerName);
                break;
            }
        }
    }

    public void sendRequestToServer(String message) {
        writer.println(message);
    }

    public void setUpBattleShipGame() {
        BattleShipGame game = new BattleShipGame(scanner);
        game.initBattleShipGame();
        try {
            objectOutput.writeObject(game.getBattleShipHub()); //Send the battleship info to the server
        } catch (IOException e) {
            System.out.println("The battle ship set up couldn't be sent to server");
            closeConnection();
        }
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

    private void serverCommunication() {
        while (true) {
            System.out.println("Waiting opponent turn.");
            String reply = getReplyFromServer();
            if (reply.equals(QUIT)) {
                break;
            }

            String message = processingPlayerRequest();
            if (message.equals(QUIT)) {
                sendRequestToServer(QUIT);
                break;
            }
        }
    }

    public String getReplyFromServer() {
        try {
            char[][] playerTwoMap = (char[][]) objectInput.readObject();
            char[][] playerOneMap = (char[][]) objectInput.readObject();
            String lastOpponentTurn = reader.readLine();

            while (reader.ready()) {
                String reply = reader.readLine();

                if (isGameOver(reply, playerTwoMap, playerOneMap, lastOpponentTurn)) {
                    return QUIT;
                } else if (reply.equals(EOF)) {
                    break;
                } else if (reply.equalsIgnoreCase(QUIT)) {
                    return getPlayerLastRequest();
                }

                System.out.println(reply);
            }

            printMenu();
            printUpdateMap(playerTwoMap, playerOneMap, lastOpponentTurn);
            return EOF;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(NO_SERVER_RESPONSE);
            return QUIT;
        }
    }

    private String getPlayerLastRequest() {
        System.out.println(WINNER);
        System.out.println("The opponent left the game.");
        lastRequest();
        return QUIT;
    }

    private String getPlayerRequest() {
        final String playerTurn = "Your turn: ";
        System.out.print(playerTurn);
        return scanner.nextLine();
    }

    public String processingPlayerRequest() {
        String request;
        while (true) {
            request = getPlayerRequest();
            if (request.equals(QUIT)) {
                System.out.println(LOSER);
                break;
            }

            sendRequestToServer(request);
            try {
                if (serverCommand.isSaveCommand(request)) {
                    System.out.println(reader.readLine());
                    continue;
                }

                String reply = reader.readLine();
                if (reply.equals(INVALID_COMMAND)) {
                    System.out.println(INVALID_COMMAND);
                    continue;
                } else if (reply.equals(SCAN)) {
                    printScanMap();
                }

                break;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(NO_SERVER_RESPONSE);
                closeConnection();
            }
        }
        return request;
    }

    private void printScanMap() throws IOException, ClassNotFoundException {
        char[][] scanMap = (char[][]) objectInput.readObject();
        reader.readLine();
        System.out.printf(FORMAT, "SCAN MAP", "");
        GameMap.printMap(scanMap);
    }

    public boolean isGameOver(String result, char[][] playerTwoMap, char[][] playerOneMap, String lastOpponentTurn) {
        if (result.equals(WINNER) || result.equals(LOSER)) {
            printUpdateMap(playerTwoMap, playerOneMap, lastOpponentTurn);
            System.out.println(result);
            return true;
        }

        return false;
    }

    public void lastRequest() {
        while (true) {
            System.out.println("Type <save gamename> or <quit>.");
            System.out.print("-> ");
            String lastRequest = scanner.nextLine();

            if (lastRequest.equalsIgnoreCase(QUIT)) {
                sendRequestToServer(QUIT);
                break;
            } else if (serverCommand.isSaveCommand(lastRequest)) {
                String gameName = lastRequest.split(" ")[1];
                if (!BattleShipFile.isFileNameExist(playerName, gameName)) {
                    sendRequestToServer(lastRequest);
                    break;
                }
                System.out.println("Choose another name!");
            }

            System.out.println(INVALID_COMMAND);
        }
    }

    private void printUpdateMap(char[][] playerTwoMap, char[][] playerOneMap, String lastOpponentTurn) {
        System.out.printf(FORMAT, "YOUR BOAR", "");
        GameMap.printMap(playerTwoMap);
        System.out.println();
        System.out.printf(FORMAT, "ENEMY BOAR", "");
        GameMap.printMap(playerOneMap);
        System.out.println(lastOpponentTurn);
    }

    public void closeConnection() {
        try {
            socket.close();
            writer.close();
            reader.close();
            objectInput.close();
            objectOutput.close();
        } catch (IOException e) {
            System.out.println(CLOSE_ERROR);
        }
    }
}
