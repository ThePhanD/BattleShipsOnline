package bg.sofia.uni.fmi.mjt.battleship.online.command;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.files.BattleShipFile;
import bg.sofia.uni.fmi.mjt.battleship.files.FileWriter;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.online.room.GameRoom;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Queue;

public class ServerCommand {

    private static final String FILEPATH_PREFIX = "resources\\";
    private static final String SEPARATOR = "\\";
    private static final String FILEPATH_SUFFIX = ".bshf";

    private static final String USERNAME = "username";
    private static final String CREATE = "create";
    private static final String DELETE = "delete";
    private static final String SAVE = "save";
    private static final String JOIN = "join";
    private static final String LOAD = "load";
    private static final String LIST = "list";
    private static final String LIST_SAVE = "list-save";

    private static final int USERNAME_ARG_COUNT = 2;
    private static final int CREATE_ARG_COUNT = 2;
    private static final int DELETE_ARG_COUNT = 2;
    private static final int SAVE_ARG_COUNT = 2;
    private static final int JOIN_ARG_COUNT = 2;
    private static final int LOAD_ARG_COUNT = 2;
    private static final int LIST_ARG_COUNT = 1;
    private static final int LIST_SAVE_ARG_COUNT = 1;

    private static final int COMMAND_NAME = 0;

    private boolean isCommandCorrect(String command, String commandName, int commandArg) {
        String[] tokens = getTokens(command);
        return tokens[COMMAND_NAME].equalsIgnoreCase(commandName) && tokens.length == commandArg;
    }

    public boolean isSaveCommand(String command) {
        return isCommandCorrect(command, SAVE, SAVE_ARG_COUNT);
    }

    public boolean isCreateCommand(String command) {
        return isCommandCorrect(command, CREATE, CREATE_ARG_COUNT);
    }

    public boolean isJoinCommand(String command) {
        return isCommandCorrect(command, JOIN, JOIN_ARG_COUNT);
    }

    public boolean isLoadCommand(String command) {
        return isCommandCorrect(command, LOAD, LOAD_ARG_COUNT);
    }

    public boolean isDeleteCommand(String command) {
        return isCommandCorrect(command, DELETE, DELETE_ARG_COUNT);
    }

    public boolean isListCommand(String command) {
        return isCommandCorrect(command, LIST, LIST_ARG_COUNT);
    }

    public boolean isListSaveCommand(String command) {
        return isCommandCorrect(command, LIST_SAVE, LIST_SAVE_ARG_COUNT);
    }

    public boolean isUsernameCommand(String command) {
        String[] tokens = getTokens(command);
        if (tokens[COMMAND_NAME].equalsIgnoreCase(USERNAME)) {
            if (tokens.length == USERNAME_ARG_COUNT) {
                return true;
            }
        }

        System.out.println("The command <username> must be: \"username <username>\"!");
        return false;
    }

    /**
     * Return true if the file is saved.
     **/
    public boolean saveGame(String playerName, String gameName, String playerOneLastTurn, BattleShipHub playerOneHub,
                            String playerTwoLastTurn, BattleShipHub playerTwoHub) throws InterruptFileSaveException {
        FileWriter.writeToFile(playerName, gameName, playerOneLastTurn, playerOneHub, playerTwoLastTurn, playerTwoHub);
        return true;
    }

    /**
     * Return true if the game room was created and added to pending games.
     **/
    public boolean createGame(GameRoom gameRoom, List<GameRoom> activeGames, Queue<GameRoom> pendingGames) {
        String gameRoomName = gameRoom.getGameRoomName();
        boolean isNameExistInActive = isNameExist(activeGames, gameRoomName);
        List<GameRoom> pendingGamesList = new ArrayList<>(pendingGames);
        boolean isNameExistInPending = isNameExist(pendingGamesList, gameRoomName);

        if (isNameExistInPending || isNameExistInActive) {
            return false;
        }

        return pendingGames.offer(gameRoom);
    }

    private boolean isNameExist(List<GameRoom> rooms, String gameRoomName) {
        for (GameRoom room : rooms) {
            String existingRoomName = room.getGameRoomName();
            if (existingRoomName.equalsIgnoreCase(gameRoomName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Return a gameroom.
     **/
    public GameRoom joinGame(String gameName, Queue<GameRoom> pendingGames) {
        if (gameName == null) { // Return the first pending game if gameName is null
            return pendingGames.poll();
        }

        // Return a pending game with gameName
        Iterator<GameRoom> iterator = pendingGames.iterator();
        while (iterator.hasNext()) {
            GameRoom game = iterator.next();
            String gameRoomName = game.getGameRoomName();
            if (gameName.equalsIgnoreCase(gameRoomName)) {
                iterator.remove();
                return game;
            }
        }

        return null;
    }

    public List<GameRoom> listGames(List<GameRoom> activeGames, List<GameRoom> pendingGames) {
        List<GameRoom> allRooms = new ArrayList<>();
        allRooms.addAll(activeGames);
        allRooms.addAll(pendingGames);
        return allRooms;
    }

    public List<String> listSaveGames(String playerName) {
        Path filepath = Paths.get(FILEPATH_PREFIX + playerName);
        if (Files.notExists(filepath)) { // return null if the player doesn't have game folder
            return null;
        }

        List<String> saveGames = new ArrayList<>(); // Get every files' name
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(filepath)) {
            for (Path file : stream) {
                String saveGame = getSaveGameName(file);
                saveGames.add(saveGame);
            }
        } catch (IOException | DirectoryIteratorException e) {
            return null;
        }

        return saveGames;
    }

    public boolean deleteGame(String playerName, String gameName) {
        try {
            Path filepath = Paths.get(FILEPATH_PREFIX + playerName + SEPARATOR + gameName + FILEPATH_SUFFIX);
            return Files.deleteIfExists(filepath);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Return true if game with @gameName exists.
     **/
    public boolean loadGame(String playerName, String gameName) {
        return BattleShipFile.isFileNameExist(playerName, gameName);
    }

    private String getSaveGameName(Path filepath) {
        String file = filepath.toString().split("\\\\")[2];
        return file.split("\\.")[0];
    }

    public boolean setUsername(String nickname, SocketChannel sc, Map<SocketChannel, String> clients) {
        if (!clients.containsKey(sc) && !clients.containsValue(nickname)) {
            clients.put(sc, nickname);
            return true;
        }

        return false;
    }

    private String[] getTokens(String input) {
        final String separator = " ";
        return input.trim().split(separator);
    }
}
