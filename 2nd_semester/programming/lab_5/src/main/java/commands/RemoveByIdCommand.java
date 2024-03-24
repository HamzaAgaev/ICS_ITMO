package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import data.validators.VehicleValidator;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import exceptions.BadArgumentValueException;
import exceptions.NoArgumentInCommandException;

import java.util.HashMap;

public class RemoveByIdCommand extends AbstractCommand {
    public RemoveByIdCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.HAS_ARG, ArgumentType.INTEGER);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException {
        Parameter<Integer> argument = (Parameter<Integer>) parametersHashMap.get("argument");
        if (argument != null) {
            Integer id = argument.getParameterValue();
            if (VehicleValidator.isValidId(id)) {
                ResponseEntity<Boolean> response = collectionManager.removeById(id);
                if (response.getValue()) {
                    return new ExecutionMessage("Removed Element from Collection by id = " + id + ".");
                } else {
                    return new ExecutionMessage("No Element with id " + id + ". Element wasn't removed.");
                }
            } else {
                throw new BadArgumentValueException("Bad id value: id is not greater than 0.");
            }
        } else {
            throw new NoArgumentInCommandException("Id is missing.");
        }
    }
}
