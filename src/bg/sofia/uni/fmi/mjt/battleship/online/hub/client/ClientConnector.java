package bg.sofia.uni.fmi.mjt.battleship.online.hub.client;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptPlayerConnectionException;
import bg.sofia.uni.fmi.mjt.battleship.exception.NonExistBattleShipFileException;
import bg.sofia.uni.fmi.mjt.battleship.files.BattleShipFile;
import bg.sofia.uni.fmi.mjt.battleship.files.FileReader;
import bg.sofia.uni.fmi.mjt.battleship.online.command.ServerCommand;
import bg.sofia.uni.fmi.mjt.battleship.online.game.client.GamePlayer;
import bg.sofia.uni.fmi.mjt.battleship.online.game.server.GameServer;
import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientConnector implements AutoCloseable {

    private static final String INTERRUPT_CONNECTION = "The connection to the server is interrupted!";
    private static final String INVALID_COMMAND = "Invalid command!";
    private static final String DISCONNECT = "disconnect";
    private static final String MAIN = "-> ";
    private static final String QUIT = "quit";
    private static final String JOIN = "join";

    private String username;
    private String serverHost;
    private int serverPort;
    private boolean isConnect = true;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;
    private ServerCommand serverCommand;

    public ClientConnector(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public ClientConnector(String serverHost, int serverPort, Socket socket, BufferedReader reader,
                           PrintWriter writer, Scanner scanner, ServerCommand serverCommand) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.scanner = scanner;
        this.serverCommand = serverCommand;
    }

    private void setUpConnection() throws InterruptPlayerConnectionException {
        try {
            socket = new Socket(serverHost, serverPort);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
            serverCommand = new ServerCommand();

            System.out.println("Connected to the server: " + serverHost + " with port: " + serverPort);
        } catch (IOException e) {
            close();
            throw new InterruptPlayerConnectionException(INTERRUPT_CONNECTION);
        }
    }

    public void connectToServer() throws InterruptPlayerConnectionException {
        setUpConnection();

        setUsername(); //Set the username

        while (isConnect) {
            String message = getClientInput();
            if (DISCONNECT.equals(message)) {
                break;
            }

            sendRequestToServer(message);
            getReplyFromServer(message);
        }

        close();
        System.out.println("Disconnected from the server: " + serverHost);
    }

    public void setUsername() throws InterruptPlayerConnectionException {
        ServerCommand serverCommand = new ServerCommand();
        try {
            while (true) {
                System.out.println("Create a username before you can play.");
                System.out.println("Action: <username name>");
                System.out.print("-> ");
                String input = scanner.nextLine();

                String[] splitInput = input.trim().split(" ");
                if (!serverCommand.isUsernameCommand(input)) {
                    continue;
                }

                if (isSetUsernameCorrect(input)) {
                    this.username = splitInput[1];
                    break;
                }
            }
        } catch (IOException e) {
            throw new InterruptPlayerConnectionException(INTERRUPT_CONNECTION);
        }
    }

    private boolean isSetUsernameCorrect(String request) throws IOException {
        sendRequestToServer(request);
        String reply = reader.readLine();
        if (reply.equals(INVALID_COMMAND)) {
            System.out.println("Please choose another name!");
            return false;
        }

        System.out.println(reply);
        return true;
    }

    private void printMenu() {
        String format = "| %-27s |\n";
        System.out.println();
        System.out.printf(format, "MENU");
        System.out.printf(format, "<create gamename>");
        System.out.printf(format, "<join> random game");
        System.out.printf(format, "<join gamename>");
        System.out.printf(format, "<load gamename>");
        System.out.printf(format, "<delete gamename>");
        System.out.printf(format, "<list> current game room");
        System.out.printf(format, "<list-save> games");
        System.out.println();
    }

    private String getClientInput() {
        printMenu();
        System.out.print(MAIN);
        return scanner.nextLine();
    }

    private void sendRequestToServer(String request) {
        writer.println(request);
    }

    public void getReplyFromServer(String request) throws InterruptPlayerConnectionException {
        try {
            String reply = reader.readLine();

            if (serverCommand.isCreateCommand(request)) {
                createGame(reply);
            } else if (request.trim().equals(JOIN)) {
                joinChosenGame(reply);
            } else if (serverCommand.isJoinCommand(request)) {
                joinChosenGame(reply);
            } else if (serverCommand.isLoadCommand(request)) {
                loadGame(reply, request);
            } else if (serverCommand.isListCommand(request)) {
                listGame(reply);
            } else if (serverCommand.isListSaveCommand(request)) {
                listSaveGames(reply);
            } else {
                System.out.println(reply);
            }

        } catch (IOException e) {
            throw new InterruptPlayerConnectionException(INTERRUPT_CONNECTION);
        }
    }

    private int getPort(String port) {
        return Integer.parseInt(port.substring(1));
    }

    private boolean isLoadGame(String port) {
        final char isLoad = '1';
        return port.charAt(0) == isLoad;
    }

    private boolean isNotPort(String port) {
        final int portLength = 5;
        return port.length() != portLength;
    }

    private void createGame(String reply) throws IOException {
        if (isNotPort(reply)) {
            System.out.println(reply);
            return;
        }

        int port = getPort(reply);
        GameServer gameServer = new GameServer(scanner, port, username);
        gameServer.createGame();
        writer.println(QUIT);
        reader.readLine();
    }

    private void joinGame(int port, String isLoadGame) {
        if (isLoadGame(isLoadGame)) {
            GamePlayer gamePlayer = new GamePlayer(scanner, port, username, true);
            gamePlayer.startGame();
        } else {
            GamePlayer gamePlayer = new GamePlayer(scanner, port, username);
            gamePlayer.startGame();
        }
    }

    private void joinChosenGame(String reply) {
        if (isNotPort(reply)) {
            System.out.println(reply);
            return;
        }

        int port = getPort(reply);
        joinGame(port, reply);
    }

    private BattleShipFile getBattleShipFile(String gameName) {
        try {
            return FileReader.readFromFile(username, gameName);
        } catch (NonExistBattleShipFileException | InterruptFileSaveException e) {
            return null;
        }
    }

    private void loadGame(String reply, String request) throws IOException {
        if (isNotPort(reply)) {
            System.out.println(reply);
            return;
        }

        int port = getPort(reply);
        String gameName = request.trim().split(" ")[1];
        BattleShipFile battleShipFile = getBattleShipFile(gameName);
        GameServer gameServer = new GameServer(scanner, port, username, battleShipFile);
        gameServer.loadGame();
        writer.println(QUIT);
        reader.readLine();
    }

    private List<GameRoom> getGameRooms(String filePath) {
        List<GameRoom> gameRooms;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(filePath))) {
            gameRooms = (List<GameRoom>) reader.readObject();

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }

        return gameRooms;
    }

    private void listGame(String filePath) {
        List<GameRoom> gameRooms = getGameRooms(filePath);
        if (gameRooms == null) {
            System.out.println("No active or pending games!");
        } else {
            GameRoom.printRoomsInfo(gameRooms);
        }
    }

    private void listSaveGames(String reply) {
        System.out.println(reply);
    }

    private void serverDisconnection() {
        this.isConnect = false;
    }

    @Override
    public void close() {
        try {
            socket.close();
            reader.close();
            writer.close();
            scanner.close();
            serverDisconnection();
        } catch (IOException e) {
            System.out.println("Error in closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
