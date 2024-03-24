package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import data.Vehicle;

import java.util.HashMap;

public class MaxByNameCommand extends AbstractCommand {
    public MaxByNameCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<Vehicle> response = collectionManager.maxByName();
        if (response.getValue() != null) {
            return new ExecutionMessage(response.getValue().toString());
        } else {
            return new ExecutionMessage("Collection is empty, there are no max element.");
        }
    }
}
