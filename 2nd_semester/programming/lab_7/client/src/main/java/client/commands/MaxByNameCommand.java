package client.commands;

import client.exceptions.SendingException;
import common.data.*;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;

import java.util.HashMap;

public class MaxByNameCommand extends AbstractCommand {
    public MaxByNameCommand() {
        super(CommandName.MAX_BY_NAME, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), user);
        ResponseEntity<Vehicle> response = clientManager.getResponseEntity(requestEntity);
        if (response.getValue() == null && response.getLoginStatus() != LoginStatus.SUCCESS) {
            return new ExecutionMessage("Access Denied: User wasn't authorized.");
        }
        if (response.getValue() != null) {
            return new ExecutionMessage(response.getValue().toString());
        } else {
            return new ExecutionMessage("Collection is empty, there are no max element.");
        }
    }
}
