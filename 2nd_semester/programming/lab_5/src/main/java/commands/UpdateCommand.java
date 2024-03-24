package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import data.validators.VehicleValidator;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import data.Vehicle;
import exceptions.BadArgumentValueException;
import exceptions.NoArgumentInCommandException;

import java.util.HashMap;

public class UpdateCommand extends AbstractCommand {
    public UpdateCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.HAS_ARG_AND_ELEMENT, ArgumentType.INTEGER);
    }
    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException {
        Parameter<Vehicle> element  = (Parameter<Vehicle>) parametersHashMap.get("vehicle");
        Parameter<Integer> argument = (Parameter<Integer>) parametersHashMap.get("argument");
        if (element != null && argument != null) {
            Vehicle vehicle = element.getParameterValue();
            Integer id = argument.getParameterValue();
            if (VehicleValidator.isValidVehicle(vehicle)) {
                if (VehicleValidator.isValidId(id)) {
                    ResponseEntity<Boolean> response = collectionManager.update(argument.getParameterValue(), element.getParameterValue());
                    if (response.getValue()) {
                        return new ExecutionMessage("Element with id = " + id + " was updated.");
                    } else {
                        return new ExecutionMessage("No element with id " + id + ".");
                    }
                } else {
                    throw new BadArgumentValueException("Bad id value: id is not greater than 0.");
                }
            } else {
                throw new BadArgumentValueException("Bad Vehicle value. Vehicle " + vehicle + " cannot replace other Vehicle with same id because it has invalid field values.");
            }
        } else {
            throw new NoArgumentInCommandException("Some of arguments are missing: Vehicle, or id, or both.");
        }
    }
}
