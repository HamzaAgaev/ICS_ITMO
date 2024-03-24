package client.commands;

import client.exceptions.SendingException;
import common.arguments.Parameter;
import client.exceptions.NoArgumentInCommandException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import client.exceptions.BadArgumentValueException;

import java.util.HashMap;

public class RemoveAtCommand extends AbstractCommand {
    public RemoveAtCommand() {
        super(CommandName.REMOVE_AT, CommandType.HAS_ARG, ArgumentType.INT_PRIMITIVE);
    }

    @Override
    public ExecutionMessage execute(HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException, SendingException {
        Parameter<Integer> argument = (Parameter<Integer>) parametersHashMap.get(CommandParameter.ARGUMENT);
        if (argument != null) {
            Integer index = argument.getParameterValue();
            if (index >= 0) {
                RequestEntity requestEntity = new RequestEntity(getCommandName(), parametersHashMap);
                ResponseEntity<Boolean> response = clientManager.getResponseEntity(requestEntity);
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
