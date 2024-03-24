package commands.arguments;

public class Parameter<T> {
    private final T parameterValue;
    public Parameter(T parameterValue) {
        this.parameterValue = parameterValue;
    }
    public T getParameterValue() {
        return parameterValue;
    }
}
