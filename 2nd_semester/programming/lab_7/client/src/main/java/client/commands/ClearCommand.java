package client.commands;

import client.exceptions.SendingException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.LoginStatus;
import common.data.User;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;

import java.util.HashMap;

public class ClearCommand extends AbstractCommand {
    public ClearCommand() {
        super(CommandName.CLEAR, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), user);
        ResponseEntity<Boolean> response = clientManager.getResponseEntity(requestEntity);
        if (response.getValue() == null && response.getLoginStatus() != LoginStatus.SUCCESS) {
            return new ExecutionMessage("Access Denied: User wasn't authorized.");
        }
        if (response.getValue()) {
            return new ExecutionMessage("Collection was cleared.");
        } else {
            return new ExecutionMessage("Collection wasn't cleared.");
        }
    }
}
