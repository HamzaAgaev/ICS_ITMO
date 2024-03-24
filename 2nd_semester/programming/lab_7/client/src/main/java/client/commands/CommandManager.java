package client.commands;

import client.exceptions.*;
import client.inputtools.NamedScanner;
import client.inputtools.ScriptScanner;
import client.inputtools.VehicleParser;
import client.netclient.ClientAuthorizationManager;
import common.arguments.ArgumentType;
import common.arguments.Parameter;
import common.data.CommandParameter;
import common.data.User;
import common.data.Vehicle;
import client.argumentparsers.IntegerArgumentParser;

import java.util.HashMap;

public class CommandManager {
    private static CommandManager instance = new CommandManager();
    private HashMap<String, AbstractCommand> CommandsMap;
    private User currentUser;

    private CommandManager() {
        CommandsMap = new HashMap<>();
        this.setCommands();
        currentUser = ClientAuthorizationManager.getInstance().getCurrentUser();
    }

    public static CommandManager getInstance() {
        return instance;
    }

    public ExecutionMessage runCommand(String commandString) throws ArgumentParserException, BadArgumentValueException, NoArgumentInCommandException, ExitException, SendingException {
        String[] commandAndArgument = commandString.trim().split("\\s+", 2);
        Boolean isValidCommandFormat = CommandFormatValidator.isValidCommandFormat(commandAndArgument, CommandsMap);
        if (!isValidCommandFormat)
            return new ExecutionMessage("Command has invalid format, check it.");
        AbstractCommand selectedCommand = CommandsMap.get(commandAndArgument[0]);
        HashMap<CommandParameter, Parameter<?>> parametersHashMap = new HashMap<>();
        if (selectedCommand.getCommandType() == CommandType.HAS_ARG || selectedCommand.getCommandType() == CommandType.HAS_ARG_AND_ELEMENT) {
            if (selectedCommand.getArgumentType() == ArgumentType.INTEGER || selectedCommand.getArgumentType() == ArgumentType.INT_PRIMITIVE) {
                Integer integerArgument = new IntegerArgumentParser().parseArgument(commandAndArgument[1]);
                parametersHashMap.put(CommandParameter.ARGUMENT, new Parameter<>(integerArgument));
            } else {
                String stringArgument = commandAndArgument[1];
                parametersHashMap.put(CommandParameter.ARGUMENT, new Parameter<>(stringArgument));
            }
        }

        if (selectedCommand.getCommandType() == CommandType.HAS_ELEMENT || selectedCommand.getCommandType() == CommandType.HAS_ARG_AND_ELEMENT) {
            NamedScanner namedScanner = ScriptScanner.getExecutingScriptsIS().getLast();
            Vehicle vehicle = new VehicleParser(namedScanner).parseFromScanner();
            parametersHashMap.put(CommandParameter.VEHICLE, new Parameter<>(vehicle));
        }
        return selectedCommand.execute(currentUser, parametersHashMap);
    }

    private void setCommands() {
        CommandsMap.put("help", new HelpCommand());
        CommandsMap.put("info", new InfoCommand());
        CommandsMap.put("show", new ShowCommand());
        CommandsMap.put("add", new AddCommand());
        CommandsMap.put("update", new UpdateCommand());
        CommandsMap.put("remove_by_id", new RemoveByIdCommand());
        CommandsMap.put("clear", new ClearCommand());
        CommandsMap.put("execute_script", new ExecuteScriptCommand());
        CommandsMap.put("exit", new ExitCommand());
        CommandsMap.put("remove_at", new RemoveAtCommand());
        CommandsMap.put("add_if_min", new AddIfMinCommand());
        CommandsMap.put("sort", new SortCommand());
        CommandsMap.put("sum_of_number_of_wheels", new SumOfNumberOfWheelsCommand());
        CommandsMap.put("max_by_name", new MaxByNameCommand());
        CommandsMap.put("filter_contains_name", new FilterContainsNameCommand());
    }
}
