package org.dimamir999.network;

import org.dimamir999.controller.CommandController;
import org.dimamir999.model.Command;
import org.dimamir999.service.CommandParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InboundMessageHandler {

    private List<Connection> connections = new ArrayList<>();
    private CommandController commandController = new CommandController();
    private CommandParser parser = new CommandParser();

    public void start(){}

    public void addNewConnection(Connection connection){
        connections.add(connection);
        new Thread(new ClientConnectionRunnable(connection)).start();
    }

    public void handleInput(String commandString){
        Command command = parser.parseCommand(commandString);
        String key, value;
        switch (command.getCommandType()){
            case CREATE:
                key = command.getParams().get(0);
                value = command.getParams().get(1);
                commandController.create(key, value);
                break;
            case READ:
                key = command.getParams().get(0);
                commandController.read(key);
                break;
            case UPDATE:
                key = command.getParams().get(0);
                value = command.getParams().get(1);
                commandController.update(key, value);
                break;
            case DELETE:
                key = command.getParams().get(0);
                commandController.delete(key);
                break;
        }
    }

    private class ClientConnectionRunnable implements Runnable{

        private Connection connection;

        public ClientConnectionRunnable(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            while (true)
                try {
                    String string = connection.read();
                    Command command = parser.parseCommand(string);
                    List<String> params = command.getParams();
                    switch (command.getCommandType()){
                        case CREATE:
                            commandController.create(params.get(0), params.get(1));
                            connection.write("OK");
                            break;
                        case READ:
                            String answer = commandController.read(params.get(0));
                            connection.write(answer);
                            break;
                        case DELETE:
                            commandController.delete(params.get(0));
                            connection.write("OK");
                            break;
                        case UPDATE:
                            commandController.update(params.get(0), params.get(1));
                            connection.write("OK\n" );
                            break;
                        default:
                            System.out.print("ERROR");
                    }
                } catch (Exception e) {
                    System.out.println("BAD ERROR");
                    e.printStackTrace();
                }
        }
    }
}
