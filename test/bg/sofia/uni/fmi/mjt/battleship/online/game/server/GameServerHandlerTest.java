package bg.sofia.uni.fmi.mjt.battleship.online.game.server;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.junit.Assert.assertNotNull;

public class GameServerHandlerTest {

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
    private static final String FIRST_MOVE = "attack A1";

    private static final String QUIT = "quit";
    private static final String START = "start\n";
    private static final String PLAYER_ONE_NAME = "test";
    private static final String PLAYER_TWO_NAME = "tester";
    private static final String SERVER_HOST = "LocalHost";
    private static final int SERVER_PORT = 1111;

    private static GameServerHandler gameServerHandler;
    private static Scanner scanner;
    private static InputStream input;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;


    private static String getShipPosition() {
        return  START + PLACE_CARRIER + PLACE_CRUISER_ONE
                + PLACE_CRUISER_TWO + PLACE_DESTROYER_ONE
                + PLACE_DESTROYER_TWO + PLACE_DESTROYER_THREE
                + PLACE_SUBMARINE_ONE + PLACE_SUBMARINE_TWO
                + PLACE_SUBMARINE_THREE + PLACE_SUBMARINE_FOUR
                + FIRST_MOVE;
    }

    @BeforeClass
    public static void setUpServer() {
        input = new ByteArrayInputStream((getShipPosition()).getBytes());
        System.setIn(input);
        scanner = new Scanner(System.in);
        gameServerHandler = new GameServerHandler(scanner, SERVER_PORT, PLAYER_ONE_NAME);
        assertNotNull(gameServerHandler);
        new Thread(() -> gameServerHandler.run()).start();
    }


    @Before
    public void setUpClient() throws IOException {
        socket = new Socket(SERVER_HOST, SERVER_PORT);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

    }

    @After
    public void closeClient() throws IOException {
        socket.close();
        reader.close();
        writer.close();
        objectInputStream.close();
        objectOutputStream.close();
    }



    @Test
    public void testConstructedClientIsNotNull() {
        assertNotNull(socket);
        assertNotNull(reader);
        assertNotNull(writer);
        assertNotNull(objectOutputStream);
        assertNotNull(objectOutputStream);
    }
}
