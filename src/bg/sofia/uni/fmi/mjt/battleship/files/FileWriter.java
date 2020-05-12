package bg.sofia.uni.fmi.mjt.battleship.files;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter {

    private static final String FILE_SAVING_CANCEL = "The save file can't be created!";
    private static final String FILEPATH_PREFIX = "resources\\";
    private static final String SEPARATOR = "\\";
    private static final String FILEPATH_SUFFIX = ".bshf";

    /**
     * Write a battleship file with the given
     *
     * @code playerName - player directory,
     * @code gameName - file name,
     * @code playerOneLastTurn, @code playerTwoLastTurn,
     * @code hubOne and @code hubTwo.
     **/
    public static boolean writeToFile(
            String playerName, String gameName, String playerOneLastTurn, BattleShipHub hubOne,
            String playerTwoLastTurn, BattleShipHub hubTwo) throws InterruptFileSaveException {

        Path filepath = Paths.get(FILEPATH_PREFIX + playerName); // Directory path
        Path file = Paths.get(filepath + SEPARATOR + gameName + FILEPATH_SUFFIX); // File path
        try {
            checkInput(playerName, gameName, playerOneLastTurn, hubOne, playerTwoLastTurn, hubTwo);
            if (Files.notExists(filepath)) { // Create the directory if it doesn't exist
                Files.createDirectory(filepath);
            }

            if (Files.exists(file)) { // Delete the old file if it exists
                Files.delete(file);
            }
        } catch (IOException e) {
            throw new InterruptFileSaveException(FILE_SAVING_CANCEL);
        }

        BattleShipFile battleShipFile = new BattleShipFile(playerOneLastTurn, hubOne, playerTwoLastTurn, hubTwo);
        return writeDataToFile(String.valueOf(file), battleShipFile);
    }

    /**
     * Create a battleship file.
     * @throws InterruptFileSaveException if it fails.
     **/
    private static boolean writeDataToFile(String filepath, BattleShipFile battleShipFile)
            throws InterruptFileSaveException {

        try (FileOutputStream outputStream = new FileOutputStream(filepath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(battleShipFile);
            return true;
        } catch (IOException e) {
            throw new InterruptFileSaveException(FILE_SAVING_CANCEL);
        }
    }

    private static void checkInput(String playerName, String gameName, String playerOneLastTurn,
                                   BattleShipHub hubOne, String playerTwoLastTurn,
                                   BattleShipHub hubTwo) throws InterruptFileSaveException {
        if (playerName == null
                || gameName == null
                || hubOne == null
                || hubTwo == null
                || playerOneLastTurn == null
                || playerTwoLastTurn == null) {

            throw new InterruptFileSaveException(FILE_SAVING_CANCEL);
        }
    }
}
