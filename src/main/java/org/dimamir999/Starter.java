package org.dimamir999;

import org.dimamir999.network.SocketServer;

import java.io.IOException;



public class Starter {

    private final static int port = 9999;

    public static void main(String[] args) throws IOException {
        SocketServer socketServer = new SocketServer(port);
        socketServer.start();
    }
}
