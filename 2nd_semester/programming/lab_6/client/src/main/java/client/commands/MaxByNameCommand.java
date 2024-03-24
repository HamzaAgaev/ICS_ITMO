package client.commands;

import client.exceptions.SendingException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;
import common.data.Vehicle;

import java.util.HashMap;

public class MaxByNameCommand extends AbstractCommand {
    public MaxByNameCommand() {
        super(CommandName.MAX_BY_NAME, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), parametersHashMap);
        ResponseEntity<Vehicle> response = clientManager.getResponseEntity(requestEntity);
        if (response.getValue() != null) {
            return new ExecutionMessage(response.getValue().toString());
        } else {
            return new ExecutionMessage("Collection is empty, there are no max element.");
        }
    }
}
