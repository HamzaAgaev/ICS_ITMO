package client.commands;

import client.exceptions.SendingException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;

import java.util.HashMap;

public class SortCommand extends AbstractCommand {
    public SortCommand() {
        super(CommandName.SORT, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), parametersHashMap);
        ResponseEntity<Boolean> response = clientManager.getResponseEntity(requestEntity);
        if (response.getValue()) {
            return new ExecutionMessage("Collection was sorted.");
        } else {
            return new ExecutionMessage("Collection wasn't sorted.");
        }
    }
}
