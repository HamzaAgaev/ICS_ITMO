package common.data.validators;

import common.data.Vehicle;
import common.data.Coordinates;
import common.data.VehicleType;

import java.time.LocalDateTime;

public class VehicleValidator {
    public static Boolean isValidVehicle(Vehicle vehicle) {
        if (vehicle == null)
            return false;
//        Integer id = vehicle.getId();
        String name = vehicle.getName();
        Coordinates coordinates = vehicle.getCoordinates();
//        LocalDateTime creationDate = vehicle.getCreationDate();
        int enginePower = vehicle.getEnginePower();
        Long numberOfWheels = vehicle.getNumberOfWheels();
        Double capacity = vehicle.getCapacity();
        VehicleType type = vehicle.getType();
        
//        return isValidId(id) && isValidName(name) && isValidCoordinates(coordinates) &&
//                isValidCreationTime(creationDate) && isValidEnginePower(enginePower) &&
//                isValidNumberOfWheels(numberOfWheels) && isValidCapacity(capacity) &&
//                isValidVehicleType(type);

        return isValidName(name) && isValidCoordinates(coordinates) && isValidEnginePower(enginePower) &&
                isValidNumberOfWheels(numberOfWheels) && isValidCapacity(capacity) &&
                isValidVehicleType(type);
    }

    public static Boolean isValidId(Integer id) {
        if (id == null)
            return false;
        return id > 0;
    }

    public static Boolean isValidName(String name) {
        if (name == null)
            return false;
        return !name.isEmpty();
    }

    public static Boolean isValidCoordinates(Coordinates coordinates) {
        return CoordinatesValidator.isValidCoordinates(coordinates);
    }

    public static Boolean isValidCreationTime(LocalDateTime creationDate) {
        return creationDate != null;
    }

    public static Boolean isValidEnginePower(int enginePower) {
        return enginePower > 0;
    }

    public static Boolean isValidNumberOfWheels(Long numberOfWheels) {
        if (numberOfWheels == null)
            return false;
        return numberOfWheels.compareTo(0L) > 0;
    }

    public static Boolean isValidCapacity(Double capacity) {
        if (capacity == null)
            return true;
        return capacity.compareTo(0D) > 0;
    }

    public static Boolean isValidVehicleType(VehicleType type) {
        if (type == null)
            return true;
        return type == VehicleType.PLANE || type == VehicleType.SPACESHIP || type == VehicleType.BOAT;
    }
}
