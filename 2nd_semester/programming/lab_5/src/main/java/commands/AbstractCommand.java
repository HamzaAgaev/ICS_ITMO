package commands;

import collection.CollectionManager;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import exceptions.BadArgumentValueException;
import exceptions.ExitException;
import exceptions.NoArgumentInCommandException;

import java.util.HashMap;

public abstract class AbstractCommand { // <T>
    protected CollectionManager collectionManager;
    private final CommandType commandType;
    private final ArgumentType argumentType;

    public AbstractCommand(CollectionManager collectionManager, CommandType commandType, ArgumentType argumentType) {
        this.collectionManager = collectionManager;
        this.commandType = commandType;
        this.argumentType = argumentType;
    }

    abstract ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException, ExitException;

    public CommandType getCommandType() {
        return commandType;
    }

    public ArgumentType getArgumentType() {
        return argumentType;
    }
}
