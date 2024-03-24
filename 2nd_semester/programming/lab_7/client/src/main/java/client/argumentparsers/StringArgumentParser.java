package client.argumentparsers;

public class StringArgumentParser implements ArgumentParser<String> {
    @Override
    public String parseArgument(String argument) {
        if (argument.isEmpty()) return null;        // парсить как null если аргумент пустой
        return argument;
    }
}
