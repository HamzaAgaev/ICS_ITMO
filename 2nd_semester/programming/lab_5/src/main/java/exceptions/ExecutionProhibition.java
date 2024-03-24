package exceptions;

public class ExecutionProhibition extends Exception {
    public ExecutionProhibition(String errorMessage) {
        super(errorMessage);
    }
}
