package bg.sofia.uni.fmi.mjt.battleship.online.hub.client;

import bg.sofia.uni.fmi.mjt.battleship.exception.InterruptPlayerConnectionException;

public class PlayerClient {

    private static final String SERVER_HOST = "LocalHost";
    private static final int SERVER_PORT = 7656;

    private ClientConnector clientConnector;

    public PlayerClient(String serverHost, int serverPort) {
        clientConnector = new ClientConnector(serverHost, serverPort);
    }

    public void startClient() throws InterruptPlayerConnectionException {
        clientConnector.connectToServer();
    }

    public static void main(String[] args) throws InterruptPlayerConnectionException {
        PlayerClient client = new PlayerClient(SERVER_HOST, SERVER_PORT);
        client.startClient();
    }
}
