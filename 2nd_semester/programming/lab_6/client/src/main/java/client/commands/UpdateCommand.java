package client.commands;

import client.exceptions.NoArgumentInCommandException;
import client.exceptions.SendingException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;
import common.data.Vehicle;
import common.data.validators.VehicleValidator;
import client.exceptions.BadArgumentValueException;

import java.util.HashMap;

public class UpdateCommand extends AbstractCommand {
    public UpdateCommand() {
        super(CommandName.UPDATE, CommandType.HAS_ARG_AND_ELEMENT, ArgumentType.INTEGER);
    }
    @Override
    public ExecutionMessage execute(HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException, SendingException {
        Parameter<Vehicle> element  = (Parameter<Vehicle>) parametersHashMap.get(CommandParameter.VEHICLE);
        Parameter<Integer> argument = (Parameter<Integer>) parametersHashMap.get(CommandParameter.ARGUMENT);
        if (element != null && argument != null) {
            Vehicle vehicle = element.getParameterValue();
            Integer id = argument.getParameterValue();
            if (VehicleValidator.isValidVehicle(vehicle)) {
                if (VehicleValidator.isValidId(id)) {
                    RequestEntity requestEntity = new RequestEntity(getCommandName(), parametersHashMap);
                    ResponseEntity<Boolean> response = clientManager.getResponseEntity(requestEntity);
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
