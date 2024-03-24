package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;

import java.util.HashMap;

public class SumOfNumberOfWheelsCommand extends AbstractCommand {
    public SumOfNumberOfWheelsCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<Long> response = collectionManager.sumOfNumberOfWheels();
        return new ExecutionMessage("Sum of number of wheels of all Vehicles is " + response.getValue() + ".");
    }
}
