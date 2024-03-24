package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;

import java.util.HashMap;

public class ClearCommand extends AbstractCommand {
    public ClearCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<Boolean> response = collectionManager.clear();
        if (response.getValue()) {
            return new ExecutionMessage("Collection was cleared.");
        } else {
            return new ExecutionMessage("Collection wasn't cleared.");
        }
    }
}
