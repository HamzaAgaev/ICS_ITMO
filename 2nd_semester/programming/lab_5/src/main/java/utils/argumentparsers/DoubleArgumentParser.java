package utils.argumentparsers;

import exceptions.ArgumentParserException;

public class DoubleArgumentParser implements ArgumentParser<Double> {
    @Override
    public Double parseArgument(String argument) throws ArgumentParserException {
        try {
            Double parseResult = Double.parseDouble(argument);
            return parseResult;
        } catch (NumberFormatException numberFormatException) {
            if (argument.isEmpty()) return null;        // парсить как null если аргумент пустой
            throw new ArgumentParserException("Argument cannot be parsed as Double.");
        }
    }
}
