package org.dimamir999.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private final Logger LOG = LogManager.getLogger(SocketServer.class);
    private int port;
    private InboundMessageHandler handler = new InboundMessageHandler();

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);
        LOG.info("Server started. Port: " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            handler.addNewConnection(new Connection(socket));
            LOG.info("New connection was made successful");
        }
    }
}
