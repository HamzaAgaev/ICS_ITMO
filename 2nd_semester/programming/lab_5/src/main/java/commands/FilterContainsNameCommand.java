package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import data.validators.VehicleValidator;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import data.Vehicle;
import exceptions.BadArgumentValueException;
import exceptions.NoArgumentInCommandException;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterContainsNameCommand extends AbstractCommand {
    public FilterContainsNameCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.HAS_ARG, ArgumentType.STRING);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) throws BadArgumentValueException, NoArgumentInCommandException {
        Parameter<String> argument = (Parameter<String>) parametersHashMap.get("argument");
        if (argument != null) {
            String name = argument.getParameterValue();
            if (VehicleValidator.isValidName(name)) {
                ResponseEntity<ArrayList<Vehicle>> response = collectionManager.filterContainsName(name);
                ArrayList<Vehicle> responseValue = response.getValue();
                String message = "";
                for (int i = 0; i < responseValue.size(); i++) {
                    if (i != responseValue.size() - 1) {
                        message += (i + 1) + ") " + responseValue.get(i) + "\n";
                    } else {
                        message += (i + 1) + ") " + responseValue.get(i);
                    }
                }
                if (message.isEmpty()) {
                    message = "There is no Vehicles with name " + name + ".";
                }
                return new ExecutionMessage(message);
            } else {
                throw new BadArgumentValueException("Bad name value: name is empty.");
            }
        } else {
            throw new NoArgumentInCommandException("Name is missing.");
        }
    }
}
