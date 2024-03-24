package client.commands;

import common.arguments.Parameter;
import client.exceptions.ExecutionProhibition;
import client.exceptions.NoArgumentInCommandException;
import client.inputtools.ScriptScanner;
import common.arguments.ArgumentType;
import client.exceptions.BadArgumentValueException;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.User;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand() {
        super(CommandName.EXECUTE_SCRIPT, CommandType.HAS_ARG, ArgumentType.STRING);
    }

    @Override
    public ExecutionMessage execute(User user, HashMap<CommandParameter, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException {
        Parameter<String> argument = (Parameter<String>) parametersHashMap.get(CommandParameter.ARGUMENT);
        if (argument != null) {
            String filename = argument.getParameterValue();
            try {
                ScriptScanner scriptScanner = new ScriptScanner(filename);
                scriptScanner.run();
                return new ExecutionMessage("Script " + filename + " was executed.");
            } catch (FileNotFoundException fileNotFoundException) {
                throw new BadArgumentValueException("File with name " + filename + " doesn't exist or you don't have permission to read it.");
            } catch (ExecutionProhibition executionProhibition) {
                throw new BadArgumentValueException("File with name " + filename + " is already executing.");
            }
        } else {
            throw new NoArgumentInCommandException("Filename is missing.");
        }
    }
}
