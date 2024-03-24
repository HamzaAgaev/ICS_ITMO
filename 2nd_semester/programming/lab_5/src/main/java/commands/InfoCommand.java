package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;

import java.util.HashMap;

public class InfoCommand extends AbstractCommand {
    public InfoCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<String> response = collectionManager.info();
        return new ExecutionMessage(response.getValue());
    }
}
