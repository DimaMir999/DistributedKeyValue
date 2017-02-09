package org.dimamir999;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.network.SocketServer;
import org.dimamir999.service.PropertyReader;

import java.io.IOException;

public class Starter {
    private static final Logger LOG = LogManager.getLogger(Starter.class);

    public static void main(String[] args) throws IOException {
        PropertyReader propertyReader = new PropertyReader("distributed-key-value.properties");
        final int port = Integer.parseInt(propertyReader.getProperty("client.port"));
        LOG.info("Port is set to " + port);

        SocketServer socketServer = new SocketServer(port);
        socketServer.start();
    }
}