package client.commands;

import client.exceptions.NoArgumentInCommandException;
import client.exceptions.SendingException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.LoginStatus;
import common.data.User;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;
import common.data.validators.VehicleValidator;
import client.exceptions.BadArgumentValueException;

import java.util.HashMap;

public class RemoveByIdCommand extends AbstractCommand {
    public RemoveByIdCommand() {
        super(CommandName.REMOVE_BY_ID, CommandType.HAS_ARG, ArgumentType.INTEGER);
    }

    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException, SendingException {
        Parameter<Integer> argument = (Parameter<Integer>) parametersHashMap.get(CommandParameter.ARGUMENT);
        if (argument != null) {
            Integer id = argument.getParameterValue();
            if (VehicleValidator.isValidId(id)) {
                RequestEntity requestEntity = new RequestEntity(getCommandName(), user, parametersHashMap);
                ResponseEntity<Boolean> response = clientManager.getResponseEntity(requestEntity);
                if (response.getValue() == null && response.getLoginStatus() != LoginStatus.SUCCESS) {
                    return new ExecutionMessage("Access Denied: User wasn't authorized.");
                }
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
