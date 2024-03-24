package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import exceptions.BadArgumentValueException;
import exceptions.NoArgumentInCommandException;

import java.util.HashMap;

public class RemoveAtCommand extends AbstractCommand {
    public RemoveAtCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.HAS_ARG, ArgumentType.INT_PRIMITIVE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException {
        Parameter<Integer> argument = (Parameter<Integer>) parametersHashMap.get("argument");
        if (argument != null) {
            Integer index = argument.getParameterValue();
            if (index >= 0) {
                ResponseEntity<Boolean> response = collectionManager.removeAt(index);
                if (response.getValue()) {
                    return new ExecutionMessage("Removed Element from Collection by index = " + index + ".");
                } else {
                    return new ExecutionMessage("Index " + index + " is out of range. Element wasn't removed.");
                }
            } else {
                throw new BadArgumentValueException("Bad index value: index is less than 0.");
            }
        } else {
            throw new NoArgumentInCommandException("Index is missing.");
        }
    }
}
