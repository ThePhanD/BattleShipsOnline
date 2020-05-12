package bg.sofia.uni.fmi.mjt.battleship.online.hub.server;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptFileSaveException;
import bg.sofia.uni.fmi.mjt.battleship.files.FileWriter;
import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HubServerHandlerTest {

    private static final String SET_USERNAME = "username test\n";
    private static final String SERVER_HOST = "LocalHost";
    private static final int SERVER_PORT = 7656;

    private static HubServerHandler hubServerHandler;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    @BeforeClass
    public static void setUpServer() {
        hubServerHandler = new HubServerHandler();
        new Thread(() -> hubServerHandler.startServer()).start();
    }

    @Before
    public void setUpClient() throws IOException {
        socket = new Socket(SERVER_HOST, SERVER_PORT);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        writer.println(SET_USERNAME);
        reader.readLine();
    }

    @After
    public void closeClient() throws IOException {
        socket.close();
        reader.close();
        writer.close();
    }

    @Test
    public void testConstructedHubServerIsNotNull() {
        assertNotNull(hubServerHandler);
    }

    @Test
    public void testExecDisconnectCommand() throws IOException {
        final String disconnect = "disconnect";
        writer.println(disconnect);
        String actualResult = reader.readLine();

        final String message = "The request disconnect must return the reply disconnect!";
        assertEquals(message, disconnect, actualResult);
    }

    @Test
    public void testExecCreateCommand() throws IOException {
        final String createGame = "create game";
        final int expectLength = 5;
        writer.println(createGame);
        String actualResult = reader.readLine();

        final String message = "The request create must return port!";
        assertEquals(message, expectLength, actualResult.length());
    }

    @Test
    public void testExecJoinRandomCommand() throws IOException {
        final String createGame = "create bgame";
        writer.println(createGame);
        String expectedPort = reader.readLine();

        final String joinRandomGame = "join";
        writer.println(joinRandomGame);
        String actualResult = reader.readLine();

        final String message = "The request join radnom game must return port!";

        assertEquals(message, expectedPort, actualResult);
    }

    @Test
    public void testExecJoinCommand() throws IOException {
        final String createGame = "create agame";
        writer.println(createGame);
        String expectedPort = reader.readLine();

        final String joinGame = "join agame";
        writer.println(joinGame);
        String actualPort = reader.readLine();

        final String message = "The request join game must return port!";
        assertEquals(message, expectedPort, actualPort);
    }

    @Test
    public void testExecLoadCommand() throws IOException {
        final String loadGame = "load testGame";
        final int expectedLength = 5;
        writer.println(loadGame);
        String actualPort = reader.readLine();

        final String message = "The request load game must return port!";
        assertEquals(message, expectedLength, actualPort.length());
    }


    private void createSaveFile() throws InterruptFileSaveException {
        BattleShipHub hub = new BattleShipHub();
        String lastTurn = "A1";
        FileWriter.writeToFile("test", "testNoGame", lastTurn, hub, lastTurn, hub);
    }

    @Test
    public void testExecDeleteCommand() throws IOException, InterruptFileSaveException {
        createSaveFile();
        final String deleteGame = "delete testNoGame";
        final String expectedResult = "The save file with name <testNoGame> was deleted!";
        writer.println(deleteGame);
        String actualResult = reader.readLine();

        final String message = "The request delete game is incorrect";
        assertEquals(message, expectedResult, actualResult);
    }

    @Test
    public void testExecListCommand() throws IOException {
        final String listGame = "list";
        final String expectedResult = "resources\\" + SERVER_HOST + "\\" + "listGames";
        writer.println(listGame);
        String actualResult = reader.readLine();

        final String message = "The request list game must return a path!";
        assertEquals(message, expectedResult, actualResult);
    }

    @Test
    public void testExecListSaveCommand() throws IOException {
        final String listGame = "list-save";
        final int expectedNumber = 1;
        writer.println(listGame);
        int actualNumber = reader.readLine().split(" ").length;

        final String message = "The request list-save game number is incorrect!";
        assertEquals(message, expectedNumber, actualNumber);
    }

    @Test
    public void testExecQuitIncorrectGameCommand() throws IOException {
        final String quitGame = "quit";
        final String expectedResult = "The game was removed!";
        writer.println(quitGame);
        String actualResult = reader.readLine();

        final String message = "The request quit game must delete the game!";
        assertEquals(message, expectedResult, actualResult);
    }

}
