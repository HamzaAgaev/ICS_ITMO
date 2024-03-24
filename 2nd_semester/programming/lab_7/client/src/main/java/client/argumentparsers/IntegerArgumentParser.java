package client.argumentparsers;

import client.exceptions.ArgumentParserException;

public class IntegerArgumentParser implements ArgumentParser<Integer> {
    @Override
    public Integer parseArgument(String argument) throws ArgumentParserException {
        try {
            Integer parseResult = Integer.parseInt(argument);
            return parseResult;
        } catch (NumberFormatException numberFormatException) {
            if (argument.isEmpty()) return null;        // парсить как null если аргумент пустой
            throw new ArgumentParserException("Argument cannot be parsed as Integer.");
        }
    }
}
