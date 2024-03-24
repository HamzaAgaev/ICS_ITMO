package client.commands;

import client.exceptions.SendingException;
import common.arguments.Parameter;
import common.data.CommandName;
import common.data.CommandParameter;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import common.arguments.ArgumentType;

import java.util.HashMap;

public class HelpCommand extends AbstractCommand {
    public HelpCommand() {
        super(CommandName.HELP, CommandType.NO_ARG, ArgumentType.NONE);
    }
    @Override
    public ExecutionMessage execute(HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws SendingException {
        RequestEntity requestEntity = new RequestEntity(getCommandName(), parametersHashMap);
        ResponseEntity<String> response = clientManager.getResponseEntity(requestEntity);
        return new ExecutionMessage(response.getValue());
    }
}
