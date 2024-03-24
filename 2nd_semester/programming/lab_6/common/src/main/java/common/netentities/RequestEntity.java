package common.netentities;

import common.arguments.Parameter;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.Vehicle;

import java.io.Serializable;
import java.util.HashMap;

public class RequestEntity implements Serializable {
    private CommandName commandName;
    private HashMap<CommandParameter, Parameter<?>> parametersHashMap;

    public RequestEntity() {}

    public RequestEntity(CommandName commandName, HashMap<CommandParameter, Parameter<?>> parametersHashMap) {
        this.commandName = commandName;
        this.parametersHashMap = parametersHashMap;
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public void setCommandName(CommandName commandName) {
        this.commandName = commandName;
    }

    public HashMap<CommandParameter, Parameter<?>> getParametersHashMap() {
        return parametersHashMap;
    }

    public void setParametersHashMap(HashMap<CommandParameter, Parameter<?>> parametersHashMap) {
        this.parametersHashMap = parametersHashMap;
    }

    @Override
    public String toString() {
        Parameter<?> argumentParameter = parametersHashMap.get(CommandParameter.ARGUMENT);
        String argumentString = "null";
        if (argumentParameter != null) {
            argumentString = argumentParameter.getParameterValue().toString();
        }

        Parameter<Vehicle> vehicleParameter = (Parameter<Vehicle>) parametersHashMap.get(CommandParameter.VEHICLE);
        String vehicleString = "null";
        if (vehicleParameter != null) {
            vehicleString = vehicleParameter.getParameterValue().toString();
        }

        return "Request Entity: commandName = " + commandName +
                ", argument = " + argumentString +
                ", Vehicle = " + vehicleString;
    }
}
