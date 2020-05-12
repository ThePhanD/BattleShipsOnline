package bg.sofia.uni.fmi.mjt.battleship.online.hub.server;

public class HubServer {

    private HubServerHandler hubServerHandler;

    public HubServer() {
        this.hubServerHandler = new HubServerHandler();
    }

    public void startServer() {
        this.hubServerHandler.startServer();
    }

    public static void main(String[] args) {
        HubServer hs = new HubServer();
        hs.startServer();
    }
}
