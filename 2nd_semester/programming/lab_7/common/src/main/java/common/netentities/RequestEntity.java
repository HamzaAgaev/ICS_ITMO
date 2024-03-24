package common.netentities;

import common.arguments.Parameter;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.User;
import common.data.Vehicle;

import java.io.Serializable;
import java.util.HashMap;

public class RequestEntity implements Serializable {
    private CommandName commandName;
    private User user;
    private HashMap<CommandParameter, Parameter<?>> parametersHashMap;

    public RequestEntity() {}

    public RequestEntity(CommandName commandName, User user) {
        this.commandName = commandName;
        this.user = user;
    }

    public RequestEntity(CommandName commandName, User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) {
        this.commandName = commandName;
        this.user = user;
        this.parametersHashMap = parametersHashMap;
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public void setCommandName(CommandName commandName) {
        this.commandName = commandName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HashMap<CommandParameter, Parameter<?>> getParametersHashMap() {
        return parametersHashMap;
    }

    public void setParametersHashMap(HashMap<CommandParameter, Parameter<?>> parametersHashMap) {
        this.parametersHashMap = parametersHashMap;
    }

    @Override
    public String toString() {
        return "Request Entity: commandName = " + commandName + ", user: " + user.getUsername();
    }
}
