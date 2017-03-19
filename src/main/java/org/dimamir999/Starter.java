package org.dimamir999;

import org.dimamir999.network.SocketServer;
import org.dimamir999.service.FileMerger;
import org.dimamir999.service.PropertyReader;

import java.io.IOException;

public class Starter {
    public static void main(String[] args) throws IOException {
        PropertyReader propertyReader = new PropertyReader("distributed-key-value.properties");
        final int port = Integer.parseInt(propertyReader.getProperty("client.port"));

        SocketServer socketServer = new SocketServer(port);
        socketServer.start();
    }
}