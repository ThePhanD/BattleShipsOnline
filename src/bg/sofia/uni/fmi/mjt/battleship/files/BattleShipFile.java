package bg.sofia.uni.fmi.mjt.battleship.files;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The battleship file that contains the
 * last turn of the players and
 * their battleship hubs.
 **/
public class BattleShipFile implements Serializable {

    private static final long serialVersionUID = 12345678L;

    private String playerOneLastTurn;
    private String playerTwoLastTurn;
    private BattleShipHub playerOneBattleShipHub;
    private BattleShipHub playerTwoBattleShipHub;

    public BattleShipFile(String playerOneLastTurn, BattleShipHub playerOneBattleShipHub,
                          String playerTwoLastTurn, BattleShipHub playerTwoBattleShipHub) {

        this.playerOneLastTurn = playerOneLastTurn;
        this.playerTwoLastTurn = playerTwoLastTurn;
        this.playerOneBattleShipHub = new BattleShipHub(playerOneBattleShipHub);
        this.playerTwoBattleShipHub = new BattleShipHub(playerTwoBattleShipHub);
    }

    public String getPlayerOneLastTurn() {
        return playerOneLastTurn;
    }

    public String getPlayerTwoLastTurn() {
        return playerTwoLastTurn;
    }

    public BattleShipHub getPlayerOneBattleShipHub() {
        return playerOneBattleShipHub;
    }

    public BattleShipHub getPlayerTwoBattleShipHub() {
        return playerTwoBattleShipHub;
    }

    /**
     * Check if the file with give @code playerName
     * and @code gameName exist
     **/
    public static boolean isFileNameExist(String playerName, String gameName) {
        final String prefix = "resources\\";
        final String separator = "\\";
        final String suffix = ".bshf";
        Path filepath = Paths.get(prefix + playerName + separator + gameName + suffix);
        return Files.exists(filepath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BattleShipFile that = (BattleShipFile) o;
        return Objects.equals(playerOneLastTurn, that.playerOneLastTurn)
                && Objects.equals(playerTwoLastTurn, that.playerTwoLastTurn)
                && Objects.equals(playerOneBattleShipHub, that.playerOneBattleShipHub)
                && Objects.equals(playerTwoBattleShipHub, that.playerTwoBattleShipHub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerOneLastTurn, playerTwoLastTurn,
                playerOneBattleShipHub, playerTwoBattleShipHub);
    }
}
