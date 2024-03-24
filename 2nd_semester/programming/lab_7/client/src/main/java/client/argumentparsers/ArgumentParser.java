package client.argumentparsers;

import client.exceptions.ArgumentParserException;

public interface ArgumentParser<T> {
    T parseArgument(String argument) throws ArgumentParserException;
}
