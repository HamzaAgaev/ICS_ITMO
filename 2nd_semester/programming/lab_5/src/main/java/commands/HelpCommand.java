package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;

import java.util.HashMap;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }
    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<String> response = collectionManager.help();
        return new ExecutionMessage(response.getValue());
    }
}
