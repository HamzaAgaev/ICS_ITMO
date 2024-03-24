package commands;

import collection.CollectionManager;
import collection.ResponseEntity;
import commands.arguments.Parameter;
import commands.arguments.ArgumentType;
import data.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowCommand extends AbstractCommand {
    public ShowCommand(CollectionManager collectionManager) {
        super(collectionManager, CommandType.NO_ARG, ArgumentType.NONE);
    }

    @Override
    public ExecutionMessage execute(HashMap<String, Parameter<?>> parametersHashMap) {
        ResponseEntity<ArrayList<Vehicle>> response = collectionManager.show();
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
            message = "Collection is empty.";
        }
        return new ExecutionMessage(message);
    }
}
