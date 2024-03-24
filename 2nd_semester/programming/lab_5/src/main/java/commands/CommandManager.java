package commands;

import collection.CollectionManager;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import data.Vehicle;
import exceptions.ArgumentParserException;
import exceptions.BadArgumentValueException;
import exceptions.ExitException;
import exceptions.NoArgumentInCommandException;
import utils.NamedScanner;
import utils.ScriptScanner;
import utils.VehicleParser;
import utils.argumentparsers.IntegerArgumentParser;

import java.util.HashMap;

public class CommandManager {
    private HashMap<String, AbstractCommand> CommandsMap;
    private final CollectionManager collectionManager = CollectionManager.getInstance();

    public CommandManager() {
        CommandsMap = new HashMap<>();
        this.setCommands();
    }

    public ExecutionMessage runCommand(String commandString) throws ArgumentParserException, BadArgumentValueException, NoArgumentInCommandException, ExitException {
        String[] commandAndArgument = commandString.trim().split("\\s+", 2);
        Boolean isValidCommandFormat = CommandFormatValidator.isValidCommandFormat(commandAndArgument, CommandsMap);
        if (!isValidCommandFormat)
            return new ExecutionMessage("Command has invalid format, check it.");
        AbstractCommand selectedCommand = CommandsMap.get(commandAndArgument[0]);
        HashMap<String, Parameter<?>> parametersHashMap = new HashMap<>();
        if (selectedCommand.getCommandType() == CommandType.HAS_ARG || selectedCommand.getCommandType() == CommandType.HAS_ARG_AND_ELEMENT) {
            if (selectedCommand.getArgumentType() == ArgumentType.INTEGER || selectedCommand.getArgumentType() == ArgumentType.INT_PRIMITIVE) {
                Integer integerArgument = new IntegerArgumentParser().parseArgument(commandAndArgument[1]);
                parametersHashMap.put("argument", new Parameter<>(integerArgument));
            } else {
                String stringArgument = commandAndArgument[1];
                parametersHashMap.put("argument", new Parameter<>(stringArgument));
            }
        }

        if (selectedCommand.getCommandType() == CommandType.HAS_ELEMENT || selectedCommand.getCommandType() == CommandType.HAS_ARG_AND_ELEMENT) {
            NamedScanner namedScanner = ScriptScanner.getExecutingScriptsIS().getLast();
            Vehicle vehicle = new VehicleParser(namedScanner).parseFromScanner(); // определить как его получить, сделать так, чтобы и при скрипте, и без него все было ок
            parametersHashMap.put("vehicle", new Parameter<>(vehicle));
        }
        //написать метод, который проверяет правильность поданной на вход комманды (если в команде есть элемент, спарсить его из входного формата в Vehicle проверить и его на правильность при помощи валидатора)
        return selectedCommand.execute(parametersHashMap);
    }

    private void setCommands() {
        CommandsMap.put("help", new HelpCommand(collectionManager));
        CommandsMap.put("info", new InfoCommand(collectionManager));
        CommandsMap.put("show", new ShowCommand(collectionManager));
        CommandsMap.put("add", new AddCommand(collectionManager));
        CommandsMap.put("update", new UpdateCommand(collectionManager));
        CommandsMap.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        CommandsMap.put("clear", new ClearCommand(collectionManager));
        CommandsMap.put("save", new SaveCommand(collectionManager));
        CommandsMap.put("execute_script", new ExecuteScriptCommand(collectionManager));
        CommandsMap.put("exit", new ExitCommand(collectionManager));
        CommandsMap.put("remove_at", new RemoveAtCommand(collectionManager));
        CommandsMap.put("add_if_min", new AddIfMinCommand(collectionManager));
        CommandsMap.put("sort", new SortCommand(collectionManager));
        CommandsMap.put("sum_of_number_of_wheels", new SumOfNumberOfWheelsCommand(collectionManager));
        CommandsMap.put("max_by_name", new MaxByNameCommand(collectionManager));
        CommandsMap.put("filter_contains_name", new FilterContainsNameCommand(collectionManager));
    }
}
