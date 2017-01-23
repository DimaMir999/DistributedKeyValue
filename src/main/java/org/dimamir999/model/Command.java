package org.dimamir999.model;

import java.util.List;

public class Command {

    private CommandType commandType;
    private List<String> params;

    public Command(CommandType commandType, List<String> params) {
        this.commandType = commandType;
        this.params = params;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
