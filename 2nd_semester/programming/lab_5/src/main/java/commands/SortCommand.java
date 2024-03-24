package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;

import java.util.HashMap;

public class SortCommand extends AbstractCommand {
    public SortCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<Boolean> response = collectionManager.sort();
        if (response.getValue()) {
            return new ExecutionMessage("Collection was sorted.");
        } else {
            return new ExecutionMessage("Collection wasn't sorted.");
        }
    }
}
