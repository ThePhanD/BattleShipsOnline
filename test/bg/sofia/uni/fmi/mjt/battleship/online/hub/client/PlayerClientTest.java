package bg.sofia.uni.fmi.mjt.battleship.online.hub.client;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PlayerClientTest {

    private static final String SERVER_HOST = "LocalHost";
    private static final int SERVER_PORT = 7656;

    @Test
    public void testConstructedPlayerClientIsNotNull() {
        PlayerClient gamePlayer = new PlayerClient(SERVER_HOST, SERVER_PORT);
        final String message = "The constructed game player client can't be null!";
        assertNotNull(message, gamePlayer);
    }

}
