package client.commands;

import client.exceptions.NoArgumentInCommandException;
import client.exceptions.SendingException;
import common.data.*;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;
import common.data.validators.VehicleValidator;
import client.exceptions.BadArgumentValueException;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterContainsNameCommand extends AbstractCommand {
    public FilterContainsNameCommand() {
        super(CommandName.FILTER_CONTAINS_NAME, CommandType.HAS_ARG, ArgumentType.STRING);
    }

    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException, SendingException {
        Parameter<String> argument = (Parameter<String>) parametersHashMap.get(CommandParameter.ARGUMENT);
        if (argument != null) {
            String name = argument.getParameterValue();
            if (VehicleValidator.isValidName(name)) {
                RequestEntity requestEntity = new RequestEntity(getCommandName(), user, parametersHashMap);
                ResponseEntity<ArrayList<Vehicle>> response = clientManager.getResponseEntity(requestEntity);
                if (response.getValue() == null && response.getLoginStatus() != LoginStatus.SUCCESS) {
                    return new ExecutionMessage("Access Denied: User wasn't authorized.");
                }
                ArrayList<Vehicle> responseValue = response.getValue();
                String message = "";
                for (int i = 0; i < responseValue.size(); i++) {
                    if (i != responseValue.size() - 1) {
                        message += (i + 1) + ") " + responseValue.get(i) + "\n";
                    } else {
                        message += (i + 1) + ") " + responseValue.get(i);
                    }
                }
                if (message.isEmpty()) {
                    message = "There is no Vehicles with name " + name + ".";
                }
                return new ExecutionMessage(message);
            } else {
                throw new BadArgumentValueException("Bad name value: name is empty.");
            }
        } else {
            throw new NoArgumentInCommandException("Name is missing.");
        }
    }
}
