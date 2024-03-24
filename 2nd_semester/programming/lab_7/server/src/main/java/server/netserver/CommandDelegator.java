package server.netserver;

import common.arguments.Parameter;
import common.data.*;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;
import server.collection.CollectionManager;
import server.users.UserManager;

public class CommandDelegator {
    public ResponseEntity<?> runCommand(RequestEntity requestEntity) {
        UserManager userManager = UserManager.getInstance();
        CommandName commandName = requestEntity.getCommandName();
        CollectionManager collectionManager = CollectionManager.getInstance();
        Parameter<?> argumentParameter = new Parameter<>(null);
        Parameter<Vehicle> vehicleParameter = new Parameter<>(null);
        if (requestEntity.getParametersHashMap() != null) {
            argumentParameter = requestEntity.getParametersHashMap().get(CommandParameter.ARGUMENT);
            vehicleParameter = (Parameter<Vehicle>) requestEntity.getParametersHashMap().get(CommandParameter.VEHICLE);
        }

        User requestEntityUser = requestEntity.getUser();
        if (requestEntityUser == null) {
            return new ResponseEntity<>(null, LoginStatus.NO_SUCH_USER);
        }
        if (commandName != CommandName.REGISTER) {
            LoginStatus loginStatus = userManager.getLoginStatusByUsernameAndPasswordHash(requestEntityUser.getUsername(), requestEntityUser.getPasswordHash());
            if (loginStatus != LoginStatus.SUCCESS) {
                return new ResponseEntity<>(null, loginStatus);
            }
        } else {
            RegisterStatus registerStatus = userManager.addNewUser(requestEntityUser);
            LoginStatus loginStatus = userManager.getLoginStatusByUsernameAndPasswordHash(requestEntityUser.getUsername(), requestEntityUser.getPasswordHash());
            return new ResponseEntity<>(registerStatus, loginStatus);
        }

        int userId = requestEntityUser.getId();
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
                responseEntity = collectionManager.add(vehicleParameter.getParameterValue(), userId);
                break;
            case UPDATE:
                responseEntity = collectionManager.update((Integer) argumentParameter.getParameterValue(), vehicleParameter.getParameterValue(), userId);
                break;
            case REMOVE_BY_ID:
                responseEntity = collectionManager.removeById((Integer) argumentParameter.getParameterValue(), userId);
                break;
            case CLEAR:
                responseEntity = collectionManager.clear(userId);
                break;
            case REMOVE_AT:
                responseEntity = collectionManager.removeAt((int) argumentParameter.getParameterValue(), userId);
                break;
            case ADD_IF_MIN:
                responseEntity = collectionManager.addIfMin(vehicleParameter.getParameterValue(), userId);
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
            case LOGIN:
                responseEntity = new ResponseEntity<>(true, LoginStatus.SUCCESS);
                break;
            case GET_USER:
                String username = requestEntityUser.getUsername();
                String passwordHash = requestEntityUser.getPasswordHash();
                User findedUser = userManager.getUserByUsernameAndPasswordHash(username, passwordHash);
                responseEntity = new ResponseEntity<>(findedUser, LoginStatus.SUCCESS);
                break;
            default:
                responseEntity = null;
                break;
        }

        return responseEntity;
    }
}
