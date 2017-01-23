package org.dimamir999.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String read() throws IOException {
        return in.readLine();
    }

    public void write(String command) {
        this.out.write(command);
        this.out.flush();
    }
}
