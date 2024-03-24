package client.commands;

import client.exceptions.SendingException;
import common.arguments.Parameter;
import client.exceptions.NoArgumentInCommandException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.data.Vehicle;
import common.data.validators.VehicleValidator;
import client.exceptions.BadArgumentValueException;

import java.util.HashMap;

public class AddIfMinCommand extends AbstractCommand {
    public AddIfMinCommand() {
        super(CommandName.ADD_IF_MIN, CommandType.HAS_ELEMENT, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException, SendingException {
        Parameter<Vehicle> element = (Parameter<Vehicle>) parametersHashMap.get(CommandParameter.VEHICLE);
        if (element != null) {
            Vehicle vehicle = element.getParameterValue();
            if (VehicleValidator.isValidVehicle(vehicle)) {
                RequestEntity requestEntity = new RequestEntity(getCommandName(), parametersHashMap);
                ResponseEntity<Boolean> response = clientManager.getResponseEntity(requestEntity);
                if (response.getValue()) {
                    return new ExecutionMessage("New minimum element " + vehicle + " was added.");
                } else {
                    return new ExecutionMessage("Element " + vehicle + " wasn't added because it isn't minimum.");
                }
            } else {
                throw new BadArgumentValueException("Bad Vehicle value. Vehicle " + vehicle + " cannot be added because it has invalid field values.");
            }
        } else {
            throw new NoArgumentInCommandException("Vehicle is missing.");
        }
    }
}
