package client.commands;

import client.exceptions.SendingException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;
import common.arguments.Parameter;
import common.data.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowCommand extends AbstractCommand {
    public ShowCommand() {
        super(CommandName.SHOW, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), parametersHashMap);
        ResponseEntity<ArrayList<Vehicle>> response = clientManager.getResponseEntity(requestEntity);
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
