package client.commands;

import client.exceptions.SendingException;
import common.data.*;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowCommand extends AbstractCommand {
    public ShowCommand() {
        super(CommandName.SHOW, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), user);
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
            message = "Collection is empty.";
        }
        return new ExecutionMessage(message);
    }
}
