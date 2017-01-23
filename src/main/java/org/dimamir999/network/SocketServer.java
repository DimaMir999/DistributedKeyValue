package org.dimamir999.network;

import jdk.internal.dynalink.linker.LinkerServices;
import org.dimamir999.controller.CommandController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

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
