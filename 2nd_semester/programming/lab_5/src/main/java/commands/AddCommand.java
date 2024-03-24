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

public class AddCommand extends AbstractCommand {
    public AddCommand(CollectionManager collectionManager)
    {
        super(collectionManager, CommandType.HAS_ELEMENT, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException {
        Parameter<Vehicle> element = (Parameter<Vehicle>) parametersHashMap.get("vehicle");
        if (element != null) {
            Vehicle vehicle = element.getParameterValue();
            if (VehicleValidator.isValidVehicle(vehicle)) {
                ResponseEntity<Boolean> response = collectionManager.add(vehicle);
                if (response.getValue()) {
                    return new ExecutionMessage("New element " + vehicle + " was added.");
                } else {
                    return new ExecutionMessage("Element " + vehicle + " wasn't added.");
                }
            } else {
                throw new BadArgumentValueException("Bad Vehicle value. Vehicle " + vehicle + " cannot be added because it has invalid field values.");
            }
        } else {
            throw new NoArgumentInCommandException("Vehicle is missing.");
        }
    }
}
