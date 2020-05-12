package bg.sofia.uni.fmi.mjt.battleship.online.game.client;

import bg.sofia.uni.fmi.mjt.battleship.hub.BattleShipHub;
import bg.sofia.uni.fmi.mjt.battleship.online.command.ServerCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class GamePlayerConnectorTest {

    private static final String PLACE_CARRIER = "place carrier A1 A5\n";
    private static final String PLACE_CRUISER_ONE = "place cruiser B1 B4\n";
    private static final String PLACE_CRUISER_TWO = "place cruiser C1 C4\n";
    private static final String PLACE_DESTROYER_ONE = "place destroyer D1 D3\n";
    private static final String PLACE_DESTROYER_TWO = "place destroyer E1 E3\n";
    private static final String PLACE_DESTROYER_THREE = "place destroyer F1 F3\n";
    private static final String PLACE_SUBMARINE_ONE = "place submarine G1 G2\n";
    private static final String PLACE_SUBMARINE_TWO = "place submarine H1 H2\n";
    private static final String PLACE_SUBMARINE_THREE = "place submarine H3 H4\n";
    private static final String PLACE_SUBMARINE_FOUR = "place submarine J1 J2\n";

    private static final String QUIT = "quit";
    private static final String PLAYER_NAME = "test";
    private static final int PORT = 1111;
    private static final boolean LOAD_GAME = false;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Scanner scanner;
    private ObjectOutput objectOutput;
    private ObjectInput objectInput;
    private ServerCommand serverCommand;
    private GamePlayerConnector gamePlayerConnector;

    private InputStream input;

    @Before
    public void setUpGamePlayerConnector() {
        socket = mock(Socket.class);
        writer = mock(PrintWriter.class);
        reader = mock(BufferedReader.class);
        objectInput = mock(ObjectInput.class);
        objectOutput = mock(ObjectOutput.class);
        serverCommand = mock(ServerCommand.class);
    }

    @Test
    public void testConstructedConnectorIsNotNull() {
        gamePlayerConnector = new GamePlayerConnector(scanner, PLAYER_NAME, PORT, socket, writer,
                reader, objectInput, objectOutput, LOAD_GAME, serverCommand);
        assertNotNull(gamePlayerConnector);
    }

    @Test
    public void testConstructedUtilsAreNotNull() {
        assertNotNull(socket);
        assertNotNull(writer);
        assertNotNull(reader);
        assertNotNull(objectInput);
        assertNotNull(objectOutput);
        assertNotNull(serverCommand);
    }

    private void setUpConnector() {
        scanner = new Scanner(System.in);
        gamePlayerConnector = new GamePlayerConnector(scanner, PLAYER_NAME, PORT, socket, writer,
                reader, objectInput, objectOutput, LOAD_GAME, serverCommand);
    }

    private void setInput(String message) {
        input = new ByteArrayInputStream((message).getBytes());
        System.setIn(input);
        setUpConnector();
    }

    @Test
    public void testUsernameWhenGameStart() {
        final String setUsername = "start\n" + "username test\n";
        final String expectedUsername = "test";
        setInput(setUsername);

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        gamePlayerConnector.startGame();

        verify(writer).println(valueCapture.capture());
        final String message = "The username is incorrect!";

        assertEquals(message, expectedUsername, valueCapture.getValue());
    }

    private String getShipPosition() {
        return PLACE_CARRIER +
                PLACE_CRUISER_ONE + PLACE_CRUISER_TWO +
                PLACE_DESTROYER_ONE + PLACE_DESTROYER_TWO +
                PLACE_DESTROYER_THREE + PLACE_SUBMARINE_ONE +
                PLACE_SUBMARINE_TWO + PLACE_SUBMARINE_THREE +
                PLACE_SUBMARINE_FOUR;
    }

    @Test
    public void testSetUpBattleShipGame() throws IOException {
        setInput(getShipPosition());

        ArgumentCaptor<BattleShipHub> valueCapture = ArgumentCaptor.forClass(BattleShipHub.class);
        gamePlayerConnector.setUpBattleShipGame();

        verify(objectOutput).writeObject(valueCapture.capture());
        final String message = "The battleship hub can't be null!";

        assertNotNull(message, valueCapture.getValue());
    }

    private void sendLastTurn(String lastTurn, String reply) throws IOException {
        when(reader.readLine()).thenReturn(lastTurn).thenReturn(reply);
        when(reader.ready()).thenReturn(true);
    }

    @Test
    public void testGetOpponentLastTurnReplyFromServer() throws IOException {
        final String eof = "EOF";
        final String lastTurn = "A1";
        setUpConnector();
        sendLastTurn(lastTurn, eof);
        String actualResult = gamePlayerConnector.getReplyFromServer();
        String message = "The opponent last turn reply message is incorrect!";

        assertEquals(message, eof, actualResult);
    }

    @Test
    public void testGetWinnerReplyFromServer() throws IOException {
        final String winner = "You won!";
        final String lastTurn = "B1";
        setUpConnector();
        sendLastTurn(lastTurn, winner);
        String actualResult = gamePlayerConnector.getReplyFromServer();
        String message = "The player must receive winning reply!";

        assertEquals(message, QUIT, actualResult);
    }

    @Test
    public void testGetLoserReplyFromServer() throws IOException {
        final String loser = "You lost!";
        final String lastTurn = "A1";
        setUpConnector();
        sendLastTurn(lastTurn, loser);
        String actualResult = gamePlayerConnector.getReplyFromServer();
        String message = "The player must receive winning reply!";

        assertEquals(message, QUIT, actualResult);
    }

    @Test
    public void testGetQuitReplyFromServer() throws IOException {
        final String lastTurn = "A1";
        setInput(QUIT);
        sendLastTurn(lastTurn, QUIT);
        String actualResult = gamePlayerConnector.getReplyFromServer();
        String message = "The player must receive winning reply!";

        assertEquals(message, QUIT, actualResult);
    }

    @Test
    public void testProcessingPlayerQuitRequest() {
        setInput(QUIT);
        String actualResult = gamePlayerConnector.processingPlayerRequest();
        String message = "The player must quit the game after losing!";

        assertEquals(message, QUIT, actualResult);
    }

    @Test
    public void testProcessingPlayerSaveRequest() {
        final String saveRequest = "save myGame";
        setInput(saveRequest + "\n" + QUIT);
        when(serverCommand.isSaveCommand(saveRequest)).thenReturn(true);
        gamePlayerConnector.processingPlayerRequest();

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(writer).println(valueCapture.capture());
        String message = "The player save command doesn't work!";

        assertEquals(message, saveRequest, valueCapture.getValue());
    }

    @Test
    public void testProcessingPlayerInvalidRequest() throws IOException {
        final String invalidRequest = "Invalid command!";
        setInput(invalidRequest + "\n" + QUIT);

        when(reader.readLine()).thenReturn(invalidRequest);
        gamePlayerConnector.processingPlayerRequest();

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(writer).println(valueCapture.capture());
        String message = "The invalid player command can't be processed!";

        assertEquals(message, invalidRequest, valueCapture.getValue());
    }

    @Test
    public void testProcessingPlayerScanRequest() throws IOException, ClassNotFoundException {
        final String scanRequest = "scan";
        setInput(scanRequest);

        when(reader.readLine()).thenReturn(scanRequest).thenReturn(scanRequest);
        when(objectInput.readObject()).thenReturn(null);
        gamePlayerConnector.processingPlayerRequest();

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(writer).println(valueCapture.capture());
        String message = "The player scan command can't be processed!";

        assertEquals(message, scanRequest, valueCapture.getValue());
    }

    @Test
    public void testLastSaveRequest() {
        final String lastSaveRequest = "save myGame";
        setInput(lastSaveRequest);

        when(serverCommand.isSaveCommand(lastSaveRequest)).thenReturn(true);
        gamePlayerConnector.lastRequest();

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(writer).println(valueCapture.capture());
        String message = "The player last request to save can't be processed!";

        assertEquals(message, lastSaveRequest, valueCapture.getValue());
    }

    @Test
    public void testIncorrectLastSaveRequest() {
        final String lastSaveRequest = "save testGame";
        setInput(lastSaveRequest + "\n" + QUIT);

        when(serverCommand.isSaveCommand(lastSaveRequest)).thenReturn(true);
        gamePlayerConnector.lastRequest();

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(writer).println(valueCapture.capture());
        String message = "The incorrect player last request to save can't be processed!";

        assertEquals(message, QUIT, valueCapture.getValue());
    }

    @Test
    public void testCloseClientConnector() throws IOException {
        setUpConnector();
        gamePlayerConnector.closeConnection();

        final int one = 1;
        verify(socket, times(one)).close();
        verify(writer, times(one)).close();
        verify(reader, times(one)).close();
        verify(objectInput, times(one)).close();
        verify(objectOutput, times(one)).close();
    }
}
