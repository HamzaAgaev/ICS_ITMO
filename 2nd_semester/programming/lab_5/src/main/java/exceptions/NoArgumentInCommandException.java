package exceptions;

public class NoArgumentInCommandException extends Exception {
    public NoArgumentInCommandException(String errorMessage) {
        super(errorMessage);
    }
}
