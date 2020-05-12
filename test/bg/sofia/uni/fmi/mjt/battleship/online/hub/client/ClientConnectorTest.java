package bg.sofia.uni.fmi.mjt.battleship.online.hub.client;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptPlayerConnectionException;
import bg.sofia.uni.fmi.mjt.battleship.online.command.ServerCommand;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClientConnectorTest {

    private static final String PLAYER_NAME = "test";
    private static final String SERVER_HOST = "LocalHost";
    private static final int SERVER_PORT = 7656;

    private ClientConnector clientConnector;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Scanner scanner;
    private ServerCommand serverCommand;
    private InputStream input;


    @Ignore
    private void setUpConnector() {
        scanner = new Scanner(System.in);
        clientConnector = new ClientConnector(SERVER_HOST, SERVER_PORT, socket, reader,
                writer, scanner, serverCommand);
    }

    @Ignore
    private void setInput(String message) {
        input = new ByteArrayInputStream((message).getBytes());
        System.setIn(input);
        setUpConnector();
    }

    @Before
    public void setUpGamePlayerConnector() {
        socket = mock(Socket.class);
        writer = mock(PrintWriter.class);
        reader = mock(BufferedReader.class);
        serverCommand = mock(ServerCommand.class);
    }

    @Test
    public void testConstructedConnectorIsNotNull() {
        clientConnector = new ClientConnector(SERVER_HOST, SERVER_PORT, socket, reader,
                writer, scanner, serverCommand);
        assertNotNull(clientConnector);
    }

    @Test
    public void testConstructedUtilsAreNotNull() {
        assertNotNull(socket);
        assertNotNull(writer);
        assertNotNull(reader);
        assertNotNull(serverCommand);
    }

    @Test
    public void testUsernameWhenGameStart() throws InterruptPlayerConnectionException, IOException {
        final String setUsername = "username test";
        setInput(setUsername);

        when(reader.readLine()).thenReturn(PLAYER_NAME);
        clientConnector.setUsername();

        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        verify(writer).println(valueCapture.capture());

        final String message = "The username is incorrect!";
        assertEquals(message, setUsername, valueCapture.getValue());
    }
}
