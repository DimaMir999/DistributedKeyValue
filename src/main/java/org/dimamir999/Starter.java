package org.dimamir999;

import org.dimamir999.network.SocketServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Starter {

    private final static int port = setPort();

    public static void main(String[] args) throws IOException {
        SocketServer socketServer = new SocketServer(port);
        socketServer.start();
    }

    private static int setPort() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/distributed-key-value.properties");
            property.load(fis);
        }
        catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }

        String clientPort = property.getProperty("client.port");
        return Integer.parseInt(clientPort);
    }
}