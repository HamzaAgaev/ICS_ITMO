package client.commands;

import common.arguments.Parameter;
import common.arguments.ArgumentType;
import client.exceptions.ExitException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.User;

import java.util.HashMap;

public class ExitCommand extends AbstractCommand {
    public ExitCommand() {
        super(CommandName.EXIT, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws ExitException {
        throw new ExitException("Exit. Goodbye!");
    }
}
