package org.dimamir999.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimamir999.controller.CommandController;
import org.dimamir999.model.Command;
import org.dimamir999.service.CommandParser;
import org.dimamir999.service.OperationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Connection {
    private static final Logger log = LogManager.getLogger(Connection.class);
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private Thread connectionThread;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void startHandleInputMessages(CommandController controller, CommandParser parser) {
        connectionThread = new Thread(new ClientConnectionRunnable(this, controller, parser));
        connectionThread.start();
    }

    public String read() throws IOException {
        return in.readLine();
    }

    public void write(String command) {
        this.out.write(command);
        this.out.flush();
    }

    public void interupt() throws IOException {
        socket.close();
    }

    private class ClientConnectionRunnable implements Runnable {
        private final Logger log = LogManager.getLogger(Connection.ClientConnectionRunnable.class);
        private Connection connection;
        private CommandController commandController;
        private CommandParser parser;

        public ClientConnectionRunnable(Connection connection, CommandController controller, CommandParser parser) {
            this.connection = connection;
            this.commandController = controller;
            this.parser = parser;
        }

        public void run() {
            while(true) {
                try {
                    String string = connection.read();
                    Command command = parser.parseCommand(string);
                    List<String> params = command.getParams();
                    String key, value;
                    switch(command.getCommandType()) {
                        case CREATE:
                            key = command.getParams().get(0);
                            value = command.getParams().get(1);
                            commandController.create(key, value);
                            connection.write("OK\n");
                            break;
                        case READ:
                            key = command.getParams().get(0);
                            String answer = commandController.read(key);
                            connection.write(answer + "\n");
                            break;
                        case UPDATE:
                            key = command.getParams().get(0);
                            value = command.getParams().get(1);
                            commandController.update(key, value);
                            connection.write("OK\n");
                            break;
                        case DELETE:
                            key = command.getParams().get(0);
                            commandController.delete(key);
                            connection.write("OK\n");
                            break;
                        default:
                            this.log.error("No command type match found");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
