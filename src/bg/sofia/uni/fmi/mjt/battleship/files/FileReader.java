package bg.sofia.uni.fmi.mjt.battleship.files;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.exception.NonExistBattleShipFileException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileReader {

    private static final String CORRUPT_SAVE_FILE = "The save file is corrupted!";
    private static final String NON_EXIST_FILE = "The battle ship file doesn't exist!";

    private static final String FILEPATH_PREFIX = "resources\\";
    private static final String SEPARATOR = "\\";
    private static final String FILEPATH_SUFFIX = ".bshf";

    /**
     * Get the battleship file with the given
     * @code playerName and @code gameName.
     **/
    public static BattleShipFile readFromFile(String playerName, String gameName)
            throws NonExistBattleShipFileException, InterruptFileSaveException {

        Path filepath = Paths.get(FILEPATH_PREFIX + playerName); // Directory path
        Path file = Paths.get(filepath + SEPARATOR + gameName + FILEPATH_SUFFIX); // File path

        if (Files.notExists(file)) {
            throw new NonExistBattleShipFileException(NON_EXIST_FILE);
        }

        BattleShipFile battleShipFile;
        try (FileInputStream inputStream = new FileInputStream(String.valueOf(file));
             ObjectInputStream reader = new ObjectInputStream(inputStream)) {

            battleShipFile = (BattleShipFile) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InterruptFileSaveException(CORRUPT_SAVE_FILE);
        }

        return battleShipFile;
    }

}
