package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;

import java.util.HashMap;

public class SaveCommand extends AbstractCommand {
    public SaveCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<Boolean> response = collectionManager.save();
        if (response.getValue()) {
            return new ExecutionMessage("Collection was saved to file.");
        } else {
            return new ExecutionMessage("Collection wasn't saved to file.");
        }
    }
}
