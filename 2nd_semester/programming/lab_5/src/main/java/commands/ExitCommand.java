package commands;

import collection.CollectionManager;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import exceptions.ExitException;

import java.util.HashMap;

public class ExitCommand extends AbstractCommand {
    public ExitCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws ExitException {
        throw new ExitException("Exit. Goodbye!");
    }
}
