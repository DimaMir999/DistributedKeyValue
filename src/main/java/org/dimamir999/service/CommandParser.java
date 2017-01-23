package org.dimamir999.service;

import org.dimamir999.model.Command;
import org.dimamir999.model.CommandType;

import java.util.ArrayList;

public class CommandParser {

    public Command parseCommand(String input){
        CommandType commandType = null;
        String[] tokens = input.split(" ");
        for(CommandType possibleType : CommandType.values()){
            if(possibleType.getKeyWord().equals(tokens[0])){
                commandType = possibleType;
                break;
            }
        }

        if(commandType == null){
            throw new IllegalArgumentException("No such command");
        }

        if(tokens.length == 1){
            throw new IllegalArgumentException("No key specified");
        }

        ArrayList<String> params = new ArrayList<String>();
        for(int i = 1; i < tokens.length; i++){
            params.add(tokens[i]);
        }

        return new Command(commandType, params);
    }
}
