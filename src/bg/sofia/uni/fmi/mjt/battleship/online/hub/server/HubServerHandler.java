package bg.sofia.uni.fmi.mjt.battleship.online.hub.server;

import bg.sofia.uni.fmi.mjt.battleship.online.command.*;
import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class HubServerHandler {

    private static final String SERVER_HOST = "LocalHost";
    private static final int SERVER_PORT = 7656;

    private static final int BUFFER_SIZE = 2048;
    private static final boolean NO_BLOCKING = false;

    private static final String DISCONNECT = "disconnect";
    private static final String NEW_GAME = "0";
    private static final String SAVE_GAME = "1";
    private static final String JOIN = "join";
    private static final String QUIT = "quit";

    private static final int NAME_ARGV = 1;

    private static final String INVALID_COMMAND = "Invalid command!";

    private boolean isOnline = true;

    private Map<SocketChannel, String> clients;
    private List<GameRoom> activeGames;
    private Queue<GameRoom> pendingGames;
    private List<Integer> availableServerPorts;
    private ServerCommand serverCommand;
    private ServerSocketChannel serverSocketChannel;
    private ByteBuffer buffer;
    private Selector selector;

    public HubServerHandler() {
        initStorage();
        initServer();
    }

    private void initStorage() {
        this.clients = new HashMap<>();
        this.activeGames = new ArrayList<>();
        this.pendingGames = new LinkedList<>();
        this.serverCommand = new ServerCommand();
        int[] ports = {4020, 4101, 4202, 4303, 4404,
                4505, 4606, 4707, 4808, 4909};
        this.availableServerPorts = new ArrayList<>();
        for (int port : ports) {
            this.availableServerPorts.add(port);
        }
    }

    private void initServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
            serverSocketChannel.configureBlocking(NO_BLOCKING);
            buffer = ByteBuffer.allocate(BUFFER_SIZE);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void startServer() {
        System.out.println("The server is online!");

        try {
            while (isOnline) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        System.out.println("Starting reading!");
                        read(key);
                    } else if (key.isAcceptable()) {
                        System.out.println("Accept new connection!");
                        accept(key);
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void read(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            buffer.clear();
            int r = sc.read(buffer);
            if (r <= 0) {
                System.out.println("Nothing to read, will close channel!");
                remove(sc);
                sc.close();
                return;
            }

            String message = execCommand(buffer, sc) + System.lineSeparator();
            System.out.println("Server: " + message);

            buffer.clear();
            buffer.put((message).getBytes());
            buffer.flip();
            sc.write(buffer);

        } catch (IOException e) {
            stop();
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void close() {
        try {
            stop();
            serverSocketChannel.close();
            selector.close();
        } catch (IOException e) {
            System.out.println("Close Error: " + e.getMessage());
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = sockChannel.accept();
        sc.configureBlocking(NO_BLOCKING);
        sc.register(selector, SelectionKey.OP_READ);
    }

    private void stop() {
        isOnline = false;
    }

    private String execCommand(ByteBuffer byteBuffer, SocketChannel sc) {
        String input = getCommand(byteBuffer);
        String[] tokens = getTokens(input);
        String name = null;
        if (tokens.length > 1) {
            name = tokens[NAME_ARGV];
        }

        if (input.trim().equalsIgnoreCase(DISCONNECT)) {
            return DISCONNECT;
        } else if (serverCommand.isUsernameCommand(input)) {
            if (setPlayerUsername(name, sc)) {
                return "The username " + name + " is set.";
            }
        } else if (serverCommand.isCreateCommand(input)) {
            return createGame(name, sc, false);
        } else if (input.trim().equalsIgnoreCase(JOIN)) {
            return joinRandomGame();
        } else if (serverCommand.isJoinCommand(input)) {
            return joinGame(name);
        } else if (serverCommand.isLoadCommand(input)) {
            return loadGame(sc, name);
        } else if (serverCommand.isDeleteCommand(input)) {
            return deleteGame(sc, name);
        } else if (serverCommand.isListCommand(input)) {
            return listGame();
        } else if (serverCommand.isListSaveCommand(input)) {
            return listSaveGame(sc);
        } else if (input.trim().equals(QUIT)) {
            return removeGameRoom(sc);
        }
        return INVALID_COMMAND;
    }

    private boolean setPlayerUsername(String username, SocketChannel sc) {
        SetUsername setUsername = new SetUsername(serverCommand, username, sc, clients);
        return setUsername.execute();
    }

    private synchronized int getAvailableServerPort() {
        if (availableServerPorts.isEmpty()) {
            return -1;
        }

        Iterator<Integer> iterator = availableServerPorts.iterator();
        int port = iterator.next();
        System.out.println(port);
        iterator.remove();
        return port;
    }

    private String createServerPort(int serverPort, boolean isLoadGame) {
        String port = Integer.toString(serverPort);
        if (isLoadGame) {
            return SAVE_GAME + port;
        }
        return NEW_GAME + port;
    }

    private String createGame(String gameRoomName, SocketChannel sc, boolean isLoadGame) {
        int serverPort = getAvailableServerPort();
        if (serverPort == -1) {
            return "There are no more free game room slot!";
        }

        String playerName = getClientName(sc);
        GameRoom gameRoom = createGameRoom(gameRoomName, playerName, serverPort, isLoadGame);
        CreateGame create = new CreateGame(serverCommand, gameRoom, activeGames, pendingGames);
        if (create.execute()) {
            return createServerPort(serverPort, isLoadGame);
        }

        availableServerPorts.add(serverPort);
        return "The game with name <" + gameRoomName + "> wasn't created!";

    }

    private GameRoom createGameRoom(String gameRoomName, String playerName, int serverPort, boolean iiLoadGame) {
        return new GameRoom(gameRoomName, playerName, serverPort, iiLoadGame);
    }

    private String joinRandomGame() {
        if (pendingGames.isEmpty()) {
            return "There is no longer any games to join!";
        }

        JoinGame joinGame = new JoinGame(serverCommand, pendingGames);
        GameRoom gameRoom = joinGame.execute();
        if(gameRoom == null) {
            return "There is no game room with that name!";
        }

        gameRoom.increasePlayerNumber();
        gameRoom.startGame();
        activeGames.add(gameRoom);
        int port = gameRoom.getServerPort();
        boolean isLoadGame = gameRoom.isLoadGame();

        return createServerPort(port, isLoadGame);
    }

    private String joinGame(String gameRoomName) {
        JoinGame joinGame = new JoinGame(serverCommand, pendingGames, gameRoomName);
        GameRoom gameRoom = joinGame.execute();
        if(gameRoom == null) {
            return "There is no game room with that name!";
        }

        gameRoom.increasePlayerNumber();
        gameRoom.startGame();
        activeGames.add(gameRoom);
        int port = gameRoom.getServerPort();
        boolean isLoadGame = gameRoom.isLoadGame();

        return createServerPort(port, isLoadGame);
    }

    private String loadGame(SocketChannel sc, String gameName) {
        String playerName = getClientName(sc);
        LoadGame loadGame = new LoadGame(serverCommand, playerName, gameName);
        if (!loadGame.execute()) {
            return "The save game doesn't exist";
        }

        return createGame(gameName, sc, true);
    }

    private String deleteGame(SocketChannel sc, String gameName) {
        String playerName = getClientName(sc);
        DeleteGame deleteGame = new DeleteGame(serverCommand, playerName, gameName);
        if (deleteGame.execute()) {
            return "The save file with name <" + gameName + "> was deleted!";
        }

        return "The save file can't be deleted!";
    }

    private String listGame() {
        ListGame listGame = new ListGame(serverCommand, activeGames, pendingGames);
        List<GameRoom> gameRooms = listGame.execute();
        return writeListOfRoomsToFile(gameRooms);
    }

    private String writeListOfRoomsToFile(List<GameRoom> gameRooms) {
        Path filepath = Paths.get("resources\\" + SERVER_HOST);
        Path file = Paths.get(filepath + "\\" + "listGames");
        try {
            if (Files.notExists(filepath)) {
                Files.createDirectory(filepath);
            }

            if (Files.exists(file)) {
                Files.delete(file);
            }

            writeDataToFile(file.toString(), gameRooms);
            return file.toString();
        } catch (IOException e) {
            System.out.println("The list of game rooms can't be save into file!");
            return file.toString();
        }
    }

    private void writeDataToFile(String filepath, List<GameRoom> gameRooms) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filepath))) {
            objectOutputStream.writeObject(gameRooms);
        } catch (IOException e) {
            System.out.println("The file can't be saved!");
        }
    }

    private String listSaveGame(SocketChannel sc) {
        String playerName = getClientName(sc);
        ListSaveGame listSaveGame = new ListSaveGame(serverCommand, playerName);
        List<String> saveGames = listSaveGame.execute();

        if (saveGames == null) {
            return "No save games!";
        }

        String delim = ", ";
        return saveGames.stream()
                .map(String::toString)
                .collect(Collectors.joining(delim));
    }

    private String removeGameRoom(SocketChannel sc) {
        String playerName = getClientName(sc);
        for (GameRoom gameRoom : activeGames) {
            if (gameRoom.getCreatorName().equals(playerName)) {
                activeGames.remove(gameRoom);
                availableServerPorts.add(gameRoom.getServerPort());
                return "The game was removed!";
            }
        }

        return "The game can't be removed!";
    }

    private String getCommand(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        return StandardCharsets.UTF_8.decode(byteBuffer).toString();
    }

    private String[] getTokens(String input) {
        final String separator = " ";
        return input.trim().split(separator);
    }

    private String getClientName(SocketChannel sc) {
        for (Map.Entry<SocketChannel, String> e : clients.entrySet()) {
            if (e.getKey().equals(sc)) {
                return e.getValue();
            }
        }
        return null;
    }

    private void remove(SocketChannel userAddress) {
        clients.remove(userAddress);
    }

}
