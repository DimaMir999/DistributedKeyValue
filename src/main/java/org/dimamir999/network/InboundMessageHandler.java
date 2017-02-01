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
        connection.startHandleInputMessages(commandController, parser);
        connections.add(connection);
    }
}
