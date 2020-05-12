package bg.sofia.uni.fmi.mjt.battleship.online.command;

import java.nio.channels.SocketChannel;
import java.util.Map;

public class SetUsername implements Command {

    private ServerCommand serverCommand;
    private String username;
    private SocketChannel socketChannel;
    private Map<SocketChannel, String> clients;

    public SetUsername(ServerCommand serverCommand,String username,
                       SocketChannel socketChannel, Map<SocketChannel, String> clients) {
        this.serverCommand = serverCommand;
        this.username = username;
        this.socketChannel = socketChannel;
        this.clients = clients;
    }

    @Override
    public boolean execute() {
        return serverCommand.setUsername(username, socketChannel, clients);
    }
}
