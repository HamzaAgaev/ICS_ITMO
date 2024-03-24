package client.exceptions;

public class SendingException extends Exception {
    public SendingException(String errorMessage) {
        super(errorMessage);
    }
}
