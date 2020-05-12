package bg.sofia.uni.fmi.mjt.battleship.online.hub.server;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class HubServerTest {

    @Test
    public void testConstructedGameServerIsNotNull() {
        HubServer hubServer = new HubServer();
        final String message = "The constructed hub server can't be null!";
        assertNotNull(message, hubServer);
    }

}
