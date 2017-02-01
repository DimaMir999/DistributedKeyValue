package org.dimamir999.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private int port;
    private InboundMessageHandler handler = new InboundMessageHandler();

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            handler.addNewConnection(new Connection(socket));
        }
    }
}
