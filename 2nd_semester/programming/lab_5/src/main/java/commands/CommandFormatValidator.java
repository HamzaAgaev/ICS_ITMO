package commands;

import java.util.HashMap;

public class CommandFormatValidator {
    public static Boolean isValidCommandFormat(String[] commandAndArgument, HashMap<String, AbstractCommand> commandsMap) {
        if (commandAndArgument.length == 1 && commandAndArgument[0].isEmpty()) {
            return false;
        }
        AbstractCommand selectedCommand = commandsMap.get(commandAndArgument[0]);
        if (selectedCommand == null) {
            return false;
        }
        if (selectedCommand.getCommandType() == CommandType.NO_ARG || selectedCommand.getCommandType() == CommandType.HAS_ELEMENT) {
            if (commandAndArgument.length != 1) {
                return false;
            }
            return true;
        } else if (selectedCommand.getCommandType() == CommandType.HAS_ARG || selectedCommand.getCommandType() == CommandType.HAS_ARG_AND_ELEMENT) {
            if (commandAndArgument.length != 2) {
                return false;
            }
            return true;
        }
        return false;
    }
}