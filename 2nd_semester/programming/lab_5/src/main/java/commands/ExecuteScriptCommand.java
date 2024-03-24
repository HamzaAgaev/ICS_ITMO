package commands;

import collection.CollectionManager;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import exceptions.BadArgumentValueException;
import exceptions.ExecutionProhibition;
import exceptions.NoArgumentInCommandException;
import utils.ScriptScanner;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.HAS_ARG, ArgumentType.STRING);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException {
        Parameter<String> argument = (Parameter<String>) parametersHashMap.get("argument");
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
