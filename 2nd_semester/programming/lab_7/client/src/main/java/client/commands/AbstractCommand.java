package client.commands;

import client.exceptions.NoArgumentInCommandException;
import client.exceptions.SendingException;
import client.netclient.ClientManager;
import common.arguments.ArgumentType;
import common.arguments.Parameter;
import client.exceptions.BadArgumentValueException;
import client.exceptions.ExitException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.User;

import java.util.HashMap;

public abstract class AbstractCommand {
    protected ClientManager clientManager = ClientManager.getInstance();
    private final CommandName commandName;
    private final CommandType commandType;
    private final ArgumentType argumentType;

    public AbstractCommand(CommandName commandName, CommandType commandType, ArgumentType argumentType) {
        this.commandName = commandName;
        this.commandType = commandType;
        this.argumentType = argumentType;
    }

    abstract ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException, ExitException, SendingException;

    public CommandName getCommandName() {
        return commandName;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public ArgumentType getArgumentType() {
        return argumentType;
    }
}
