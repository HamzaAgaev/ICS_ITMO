package server.collection;

import common.data.Vehicle;
import common.netentities.ResponseEntity;
import server.database.DatabaseManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CollectionManager {
    private final static CollectionManager instance = new CollectionManager();
    private CopyOnWriteArrayList<Vehicle> vehicles;
    private LocalDateTime collectionInitializationTime;
    private boolean isRunning = false;
    private DatabaseManager databaseManager = DatabaseManager.getInstance();

    private CollectionManager() {}

    public static CollectionManager getInstance() {
        return instance;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    private int getIndexById(Integer id) {
        int resultIndex = IntStream.range(0, vehicles.size()).filter(integer -> vehicles.get(integer).getId().equals(id)).findFirst().orElse(-1);
        return resultIndex;
    }

    private Integer getIdByIndex(int index) {
        return vehicles.get(index).getId();
    }

    public boolean run() {
        if (!isRunning) {
            collectionInitializationTime = LocalDateTime.now();
            vehicles = getAllVehicles();
            isRunning = true;
        }
        return isRunning;
    }

    public synchronized CopyOnWriteArrayList<Vehicle> getAllVehicles() {
        return new CopyOnWriteArrayList<>(databaseManager.getAllVehicles());
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
            "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.",
            "exit : завершить программу (без сохранения в файл)",
            "remove_at index : удалить элемент, находящийся в заданной позиции коллекции (index)",
            "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции",
            "sort : отсортировать коллекцию в естественном порядке",
            "sum_of_number_of_wheels : вывести сумму значений поля numberOfWheels для всех элементов коллекции",
            "max_by_name : вывести любой объект из коллекции, значение поля name которого является максимальным",
            "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку"
        };

        String helpString = String.join("\n", commandsInfo);

        return new ResponseEntity<>(helpString);
    }

    public ResponseEntity<String> info() {
        String collectionType = vehicles.getClass().getName();
        String creationTime = collectionInitializationTime.toString();
        String collectionLength = Integer.toString(vehicles.size());
        String[] infoStringArray = {"Collection type: " + collectionType, "Collection initialization time: " + creationTime, "Collection length: " + collectionLength};
        String infoString = String.join("\n", infoStringArray);
        return new ResponseEntity<>(infoString);
    }

    public ResponseEntity<ArrayList<Vehicle>> show() {
        ArrayList<Vehicle> vehiclesToShow = vehicles.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
        return new ResponseEntity<>(vehiclesToShow);
    }

    public ResponseEntity<Boolean> add(Vehicle vehicle, int ownerUserId) {
        vehicle.setCreationDate(LocalDateTime.now());
        vehicle.setOwnerUserId(ownerUserId);
        int vehicleWithSameIdIndex = getIndexById(vehicle.getId());
        boolean isAdded = false;
        if (vehicleWithSameIdIndex == -1) {
            isAdded = databaseManager.addNewVehicle(vehicle);
            vehicles = getAllVehicles();
        }
        return new ResponseEntity<>(isAdded);
    }

    public ResponseEntity<Boolean> update(Integer id, Vehicle vehicle, int ownerUserId) {
        int vehicleWithSameIdIndex = getIndexById(id);
        boolean isUpdated = false;
        if (vehicleWithSameIdIndex != -1) {
            if (vehicles.get(vehicleWithSameIdIndex).getOwnerUserId() == ownerUserId) {
                vehicle.setCreationDate(LocalDateTime.now());
                isUpdated = databaseManager.updateVehicles(id, vehicle);
                vehicles = getAllVehicles();
            }
        }
        return new ResponseEntity<>(isUpdated);
    }

    public ResponseEntity<Boolean> removeById(Integer id, int ownerUserId) {
        Optional<Vehicle> optionalVehicle = vehicles.stream().filter(vehicle -> vehicle.getId().equals(id)).findFirst();
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            boolean isRemoved = false;
            if (vehicle.getOwnerUserId() == ownerUserId) {
                isRemoved = databaseManager.removeVehiclesById(id);
                vehicles = getAllVehicles();
            }
            return new ResponseEntity<>(isRemoved);
        }
        return new ResponseEntity<>(false);
    }

    public ResponseEntity<Boolean> clear(int ownerUserId) {
        boolean isCleared = databaseManager.clearVehiclesByOwnerUserId(ownerUserId);
        vehicles = getAllVehicles();
        return new ResponseEntity<>(isCleared);
    }

//    public ResponseEntity<Boolean> save() {
//        return new ResponseEntity<>(false);
//    }

    public ResponseEntity<Boolean> removeAt(int index, int ownerUserId) {
        boolean isRemoved = false;
        if (index >= 0 && index < vehicles.size()) {
            ArrayList<Vehicle> sortedVehicles = vehicles.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
            Vehicle vehicleToRemove = sortedVehicles.get(index);
            if (vehicleToRemove.getOwnerUserId() == ownerUserId) {
                isRemoved = databaseManager.removeVehiclesById(vehicleToRemove.getId());
                vehicles = getAllVehicles();
            }
        }
        return new ResponseEntity<>(isRemoved);
    }

    public ResponseEntity<Boolean> addIfMin(Vehicle vehicle, int ownerUserId) {
        Optional<Vehicle> optionalMinVehicle = vehicles.stream().min(Vehicle::compareTo);
        if (optionalMinVehicle.isPresent()) {
            if (vehicle.compareTo(optionalMinVehicle.get()) >= 0) {
                return new ResponseEntity<>(false);
            }
        }
        return add(vehicle, ownerUserId);
    }

    public ResponseEntity<Boolean> sort() {
        if (!vehicles.isEmpty()) {
            vehicles = vehicles.stream().sorted().collect(Collectors.toCollection(CopyOnWriteArrayList::new));
            return new ResponseEntity<>(true);
        }
        return new ResponseEntity<>(false);
    }

    public ResponseEntity<Long> sumOfNumberOfWheels() {
        Long resultSum = vehicles.stream().mapToLong(Vehicle::getNumberOfWheels).sum();
        return new ResponseEntity<>(resultSum);
    }

    public ResponseEntity<Vehicle> maxByName() {
        Optional<Vehicle> optionalVehicle = vehicles.stream().max(Vehicle::compareTo);
        if (optionalVehicle.isPresent()) {
            return new ResponseEntity<>(optionalVehicle.get());
        }
        return new ResponseEntity<>(null);
    }

    public ResponseEntity<ArrayList<Vehicle>> filterContainsName(String name) {
        ArrayList<Vehicle> vehiclesContainsName = vehicles.stream().filter(vehicle -> vehicle.getName().contains(name)).sorted().collect(Collectors.toCollection(ArrayList::new));
        return new ResponseEntity<>(vehiclesContainsName);
    }
}
