package bg.sofia.uni.fmi.mjt.battleship.online.room;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class GameRoom implements Serializable {

    private static final long serialVersionUID = 143545678L;

    private static final String LINE_SEPARATOR = "-------------------------------------------------------------";
    private static final String FORMAT = "| %-12s | %-12s | %-12s | %-12s |\n";
    private static final String MAX_PLAYERS = "/2";

    private static final GameStatus PENDING = GameStatus.PENDING;
    private static final GameStatus IN_PROGRESS = GameStatus.IN_PROGRESS;

    private String gameRoomName;
    private String creatorName;
    private GameStatus gameStatus;
    private int playerNumbers;
    private int serverPort;
    private boolean isLoadGame;

    public GameRoom(String gameRoomName, String creatorName, int serverPort, boolean isLoadGame) {
        this.gameRoomName = gameRoomName;
        this.creatorName = creatorName;
        this.gameStatus = PENDING;
        this.playerNumbers = 1;
        this.serverPort = serverPort;
        this.isLoadGame = isLoadGame;
    }

    public String getGameRoomName() {
        return gameRoomName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getPlayerNumbers() {
        return playerNumbers;
    }

    public int getServerPort() {
        return serverPort;
    }

    public boolean isLoadGame() {
        return isLoadGame;
    }

    public void increasePlayerNumber() {
        this.playerNumbers++;
    }

    public void startGame() {
        this.gameStatus = IN_PROGRESS;
    }

    private static void printInfo() {
        System.out.println(LINE_SEPARATOR);
        System.out.printf(FORMAT, "NAME", "CREATOR", "STATUS", "PLAYERS");
        System.out.println(LINE_SEPARATOR);
    }

    public static void printRoomsInfo(List<GameRoom> rooms) {
        printInfo();
        for (GameRoom room : rooms) {
            System.out.printf(FORMAT,
                    room.gameRoomName,
                    room.getCreatorName(),
                    room.getGameStatus(),
                    room.getPlayerNumbers() + MAX_PLAYERS);
        }

        System.out.println(LINE_SEPARATOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameRoom gameRoom = (GameRoom) o;
        return playerNumbers == gameRoom.playerNumbers &&
                serverPort == gameRoom.serverPort &&
                isLoadGame == gameRoom.isLoadGame &&
                Objects.equals(gameRoomName, gameRoom.gameRoomName) &&
                Objects.equals(creatorName, gameRoom.creatorName) &&
                gameStatus == gameRoom.gameStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameRoomName, creatorName, gameStatus, playerNumbers, serverPort, isLoadGame);
    }
}
