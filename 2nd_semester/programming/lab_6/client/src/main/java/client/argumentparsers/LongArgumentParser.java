package client.argumentparsers;

import client.exceptions.ArgumentParserException;

public class LongArgumentParser implements ArgumentParser<Long> {
    @Override
    public Long parseArgument(String argument) throws ArgumentParserException {
        try {
            Long parseResult = Long.parseLong(argument);
            return parseResult;
        } catch (NumberFormatException numberFormatException) {
            if (argument.isEmpty()) return null;        // парсить как null если аргумент пустой
            throw new ArgumentParserException("Argument cannot be parsed as Long.");
        }
    }
}
