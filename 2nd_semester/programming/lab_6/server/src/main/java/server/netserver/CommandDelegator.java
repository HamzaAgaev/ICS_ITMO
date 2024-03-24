package server.netserver;

import common.arguments.Parameter;
import common.data.CommandName;
import common.data.CommandParameter;
import common.data.Vehicle;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import server.collection.CollectionManager;

public class CommandDelegator {
    public ResponseEntity<?> runCommand(RequestEntity requestEntity) {
        CommandName commandName = requestEntity.getCommandName();
        CollectionManager collectionManager = CollectionManager.getInstance();
        Parameter<?> argumentParameter = requestEntity.getParametersHashMap().get(CommandParameter.ARGUMENT);
        Parameter<Vehicle> vehicleParameter = (Parameter<Vehicle>) requestEntity.getParametersHashMap().get(CommandParameter.VEHICLE);
        ResponseEntity<?> responseEntity;
        switch (commandName) {
            case HELP:
                responseEntity = collectionManager.help();
                break;
            case INFO:
                responseEntity = collectionManager.info();
                break;
            case SHOW:
                responseEntity = collectionManager.show();
                break;
            case ADD:
                responseEntity = collectionManager.add(vehicleParameter.getParameterValue());
                break;
            case UPDATE:
                responseEntity = collectionManager.update((Integer) argumentParameter.getParameterValue(), vehicleParameter.getParameterValue());
                break;
            case REMOVE_BY_ID:
                responseEntity = collectionManager.removeById((Integer) argumentParameter.getParameterValue());
                break;
            case CLEAR:
                responseEntity = collectionManager.clear();
                break;
            case REMOVE_AT:
                responseEntity = collectionManager.removeAt((int) argumentParameter.getParameterValue());
                break;
            case ADD_IF_MIN:
                responseEntity = collectionManager.addIfMin(vehicleParameter.getParameterValue());
                break;
            case SORT:
                responseEntity = collectionManager.sort();
                break;
            case SUM_OF_NUMBER_OF_WHEELS:
                responseEntity = collectionManager.sumOfNumberOfWheels();
                break;
            case MAX_BY_NAME:
                responseEntity = collectionManager.maxByName();
                break;
            case FILTER_CONTAINS_NAME:
                responseEntity = collectionManager.filterContainsName((String) argumentParameter.getParameterValue());
                break;
            default:
                responseEntity = null;
                break;
        }

        collectionManager.save();

        return responseEntity;
    }
}
