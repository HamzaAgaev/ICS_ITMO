package client.commands;

import client.exceptions.SendingException;
import common.arguments.Parameter;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.LoginStatus;
import common.data.User;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;

import java.util.HashMap;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super(CommandName.HELP, CommandType.NO_ARG, ArgumentType.NONE);
    }
    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), user);
        ResponseEntity<String> response = clientManager.getResponseEntity(requestEntity);
        if (response.getValue() == null && response.getLoginStatus() != LoginStatus.SUCCESS) {
            return new ExecutionMessage("Access Denied: User wasn't authorized.");
        }
        return new ExecutionMessage(response.getValue());
    }
}
