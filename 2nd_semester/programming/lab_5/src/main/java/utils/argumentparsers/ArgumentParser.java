package utils.argumentparsers;

import exceptions.ArgumentParserException;

public interface ArgumentParser<T> {
    T parseArgument(String argument) throws ArgumentParserException;
}
