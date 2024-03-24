package client.exceptions;

public class BadArgumentValueException extends Exception {
    public BadArgumentValueException(String errorMessage) {
        super(errorMessage);
    }
}
