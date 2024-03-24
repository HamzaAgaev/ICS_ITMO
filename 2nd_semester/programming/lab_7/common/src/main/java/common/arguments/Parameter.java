package common.arguments;

import java.io.Serializable;

public class Parameter<T extends Serializable> implements Serializable {
    private final T parameterValue;
    public Parameter(T parameterValue) {
        this.parameterValue = parameterValue;
    }
    public T getParameterValue() {
        return parameterValue;
    }
}
