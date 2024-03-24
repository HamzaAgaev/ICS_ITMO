package collection;

import data.Vehicle;
import utils.jsonhandlers.JSONFileHandler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class CollectionManager {
    // pодумать над уникальностью ID (наверное в runCollection)
    private final static CollectionManager instance = new CollectionManager();
    private ArrayList<Vehicle> vehicles;
    private String collectionFilename;
    private LocalDateTime collectionInitializationTime;
    private boolean isRunning = false;

    private CollectionManager() {}

    public static CollectionManager getInstance() {
        return instance;
    }

    public ResponseEntity<Boolean> tryToSetCollectionFilename(String filename) {
        try {
            File file = new File(filename);
            if (file.exists()) {
                collectionFilename = filename;
                return new ResponseEntity<>(true);
            }
            return new ResponseEntity<>(false);
        } catch (NullPointerException nullPointerException) {
            return new ResponseEntity<>(false);
        }
    }

    private int getIndexById(Integer id) {
        int resultIndex = -1;
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getId().equals(id)) {
                resultIndex = i;
                break;
            }
        }
        return resultIndex;
    }

    private Integer getFreeId() {
        Integer maxId = 0;
        for (Vehicle vehicle: vehicles) {
            if (vehicle.getId().compareTo(maxId) > 0) {
                maxId = vehicle.getId();
            }
        }
        maxId++;
        return maxId;
    }

    private void setAutomaticValues(Vehicle vehicle, Integer id) {
        vehicle.setId(id);
        vehicle.setCreationDate(LocalDateTime.now());
    }

    public ResponseEntity<Boolean> run() {
        if (!isRunning) {
            collectionInitializationTime = LocalDateTime.now();
            try {
                vehicles = JSONFileHandler.readVehiclesFromJSONFile(collectionFilename);
                if (vehicles == null) {
                    vehicles = new ArrayList<>();
                }
                isRunning = true;
            } catch (IOException ioException) {
                return new ResponseEntity<>(false);
            }
        }
        return new ResponseEntity<>(true);
    }

    public ResponseEntity<String> help() {
        String[] commandsInfo = {
            "help : вывести справку по доступным командам",
            "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)",
            "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении",
            "add {element} : добавить новый элемент в коллекцию",
            "update id {element} : обновить значение элемента коллекции, id которого равен заданному",
            "remove_by_id id : удалить элемент из коллекции по его id",
            "clear : очистить коллекцию",
            "save : сохранить коллекцию в файл",
            "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.",
            "exit : завершить программу (без сохранения в файл)",
            "remove_at index : удалить элемент, находящийся в заданной позиции коллекции (index)",
            "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
            "sort : отсортировать коллекцию в естественном порядке",
            "sum_of_number_of_wheels : вывести сумму значений поля numberOfWheels для всех элементов коллекции",
            "max_by_name : вывести любой объект из коллекции, значение поля name которого является максимальным",
            "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку"
        };

        String helpString = "";

        for (int i = 0; i < commandsInfo.length; i++) {
            if (i != commandsInfo.length - 1) {
                helpString += commandsInfo[i] + "\n";
            } else {
                helpString += commandsInfo[i];
            }
        }

        return new ResponseEntity<>(helpString);
    }

    public ResponseEntity<String> info() {
        String infoString = "";
        String collectionType = vehicles.getClass().getName();
        String creationTime = collectionInitializationTime.toString();
        String collectionLength = Integer.toString(vehicles.size());
        infoString += "Collection type: " + collectionType + "\n";
        infoString += "Collection creation time: " + creationTime + "\n";
        infoString += "Collection length: " + collectionLength;
        return new ResponseEntity<>(infoString);
    }

    public ResponseEntity<ArrayList<Vehicle>> show() {
        return new ResponseEntity<>(vehicles);
    }

    public ResponseEntity<Boolean> add(Vehicle vehicle) {
        setAutomaticValues(vehicle, getFreeId());
        int vehicleWithSameIdIndex = getIndexById(vehicle.getId());
        if (vehicleWithSameIdIndex == -1) {
            vehicles.add(vehicle);
            return new ResponseEntity<>(true);
        }
        return new ResponseEntity<>(false);
    }

    public ResponseEntity<Boolean> update(Integer id, Vehicle vehicle) {
        int vehicleWithSameIdIndex = getIndexById(id);
        if (vehicleWithSameIdIndex != -1) {
            setAutomaticValues(vehicle, id);
            vehicles.set(vehicleWithSameIdIndex, vehicle);
            return new ResponseEntity<>(true);
        }
        return new ResponseEntity<>(false);
    }

    public ResponseEntity<Boolean> removeById(Integer id) {
        int vehicleWithSameIdIndex = getIndexById(id);
        return removeAt(vehicleWithSameIdIndex);    // даже если не найден все отработает правильно
    }

    public ResponseEntity<Boolean> clear() {
        vehicles.clear();
        return new ResponseEntity<>(true);
    }

    public ResponseEntity<Boolean> save() {
        try {
            JSONFileHandler.writeVehiclesToJSONFile(vehicles, collectionFilename);
            return new ResponseEntity<>(true);
        } catch (IOException ioException) {
            return new ResponseEntity<>(false);
        }
    }

//    public void executeScript(String filename) { // метод клиента а не server
//        //????
//    }

//    public void exit() { // клиент
//        //?????
//    }

    public ResponseEntity<Boolean> removeAt(int index) {
        if (index >= 0 && index < vehicles.size()) {
            vehicles.remove(index);
            return new ResponseEntity<>(true);
        }
        return new ResponseEntity<>(false);
    }

    public ResponseEntity<Boolean> addIfMin(Vehicle vehicle) { // непонятно как реализовать
        if (!vehicles.isEmpty()) {
            Vehicle minVehicle = Collections.min(vehicles);
            if (vehicle.compareTo(minVehicle) >= 0) {
                return new ResponseEntity<>(false);
            }
        }
        setAutomaticValues(vehicle, getFreeId());
        return add(vehicle);
    }

    public ResponseEntity<Boolean> sort() {
        if (!vehicles.isEmpty()) {
            Collections.sort(vehicles);
            return new ResponseEntity<>(true);
        }
        return new ResponseEntity<>(false);
    }

    public ResponseEntity<Long> sumOfNumberOfWheels() {
        Long resultSum = 0L;
        for (Vehicle v: vehicles) {
            resultSum += v.getNumberOfWheels();
        }
        return new ResponseEntity<>(resultSum);
    }

    public ResponseEntity<Vehicle> maxByName() {
        if (!vehicles.isEmpty()) {
            return new ResponseEntity<>(Collections.max(vehicles));
        }
        return new ResponseEntity<>(null);
    }

    public ResponseEntity<ArrayList<Vehicle>> filterContainsName(String name) {
        ArrayList<Vehicle> vehiclesContainsName = new ArrayList<>();
        for (Vehicle v: vehicles) {
            String vehicleName = v.getName();
            if (vehicleName.contains(name)) {
                vehiclesContainsName.add(v);
            }
        }
        return new ResponseEntity<>(vehiclesContainsName);
    }
}
