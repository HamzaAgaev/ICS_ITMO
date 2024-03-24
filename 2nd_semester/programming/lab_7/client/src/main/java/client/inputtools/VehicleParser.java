package client.inputtools;

import client.argumentparsers.*;
import common.data.Coordinates;
import common.data.Vehicle;
import common.data.VehicleType;
import common.data.validators.CoordinatesValidator;
import common.data.validators.VehicleValidator;
import client.exceptions.BadArgumentValueException;

import java.util.HashMap;
import java.util.Scanner;

public class VehicleParser {
    private NamedScanner namedScanner;
    public VehicleParser(NamedScanner namedScanner) {
        this.namedScanner = namedScanner;
        this.setVehicleFieldClassHashMap();
    }

    private enum VehicleFieldName {
        NAME,
        X_COORDINATES,
        Y_COORDINATES,
        ENGINE_POWER,
        NUMBER_OF_WHEELS,
        CAPACITY,
        TYPE
    }

    private class VehicleFieldClass<T> {
        private ArgumentParser<T> argumentParser;
        private String interactiveMessage;
        private Boolean canBeNull;

        public VehicleFieldClass(ArgumentParser<T> argumentParser, String interactiveMessage, Boolean canBeNull) {
            this.argumentParser = argumentParser;
            this.interactiveMessage = interactiveMessage;
            this.canBeNull = canBeNull;
        }

        public ArgumentParser<T> getArgumentParser() {
            return argumentParser;
        }

        public String getInteractiveMessage() {
            return interactiveMessage;
        }

        public Boolean getCanBeNull() {
            return canBeNull;
        }
    }

    private HashMap<VehicleFieldName, VehicleFieldClass<?>> vehicleFieldClassHashMap = new HashMap<>();

    private <T> Boolean isValidValue(VehicleFieldName vehicleFieldName, T value) {
        switch (vehicleFieldName) {
            case NAME:
                return VehicleValidator.isValidName((String) value);
            case X_COORDINATES:
                return CoordinatesValidator.isValidX((long) value);
            case Y_COORDINATES:
                return CoordinatesValidator.isValidY((Integer) value);
            case ENGINE_POWER:
                return VehicleValidator.isValidEnginePower((int) value);
            case NUMBER_OF_WHEELS:
                return VehicleValidator.isValidNumberOfWheels((Long) value);
            case CAPACITY:
                return VehicleValidator.isValidCapacity((Double) value);
            case TYPE:
                return VehicleValidator.isValidVehicleType((VehicleType) value);
            default:
                return false;
        }
    }

    private void setVehicleFieldClassHashMap() {
        vehicleFieldClassHashMap.put(VehicleFieldName.NAME, new VehicleFieldClass<String>(new StringArgumentParser(), "Enter Vehicle Name (not empty string): ", false));
        vehicleFieldClassHashMap.put(VehicleFieldName.X_COORDINATES, new VehicleFieldClass<Long>(new LongArgumentParser(), "Enter Vehicle Coordinate X (not empty string, greater than -832): ", false));
        vehicleFieldClassHashMap.put(VehicleFieldName.Y_COORDINATES, new VehicleFieldClass<Integer>(new IntegerArgumentParser(), "Enter Vehicle Coordinate Y (not empty string, not greater than 802): ", false));
        vehicleFieldClassHashMap.put(VehicleFieldName.ENGINE_POWER, new VehicleFieldClass<Integer>(new IntegerArgumentParser(), "Enter Vehicle Engine Power (not empty string, greater than 0): ", false));
        vehicleFieldClassHashMap.put(VehicleFieldName.NUMBER_OF_WHEELS, new VehicleFieldClass<Long>(new LongArgumentParser(), "Enter Vehicle Number Of Wheels (not empty string, greater than 0): ", false));
        vehicleFieldClassHashMap.put(VehicleFieldName.CAPACITY, new VehicleFieldClass<Double>(new DoubleArgumentParser(), "Enter Vehicle Capacity (greater than 0, or empty): ", true));
        vehicleFieldClassHashMap.put(VehicleFieldName.TYPE, new VehicleFieldClass<VehicleType>(new VehicleTypeArgumentParser(), "Enter Vehicle Type (PLANE | BOAT | SPACESHIP, or empty): ", true));
    }

    private <T> T parseValue(VehicleFieldName vehicleFieldName) throws BadArgumentValueException {
        VehicleFieldClass<?> vehicleFieldClass = vehicleFieldClassHashMap.get(vehicleFieldName);
        ArgumentParser<?> argumentParser = vehicleFieldClass.getArgumentParser();
        String interactiveMessage = vehicleFieldClass.getInteractiveMessage();
        Boolean canBeNull = vehicleFieldClass.getCanBeNull();
        if (namedScanner.getIsInteractiveMode()) {
            System.out.print(interactiveMessage);
        }
        Scanner scanner = namedScanner.getScanner();
        do {
            try {
                Object resultObject = argumentParser.parseArgument(scanner.nextLine());
                if (resultObject == null && !canBeNull) {
                    throw new BadArgumentValueException("This field can't be Null.");
                }
                if (!isValidValue(vehicleFieldName, resultObject)) {
                    throw new BadArgumentValueException("Your Value is invalid. Try again.");
                }
                T result = (T) resultObject;
                return result;
            } catch (Exception exception) {
                if (namedScanner.getIsInteractiveMode()) {
                    System.out.println(exception);
                    System.out.print(interactiveMessage);
                } else {
                    throw new BadArgumentValueException("Script file has incorrect Vehicle values.");
                }
            }
        } while (scanner.hasNextLine() && namedScanner.getIsInteractiveMode());
        return null;
    }

    public Vehicle parseFromScanner() throws BadArgumentValueException {
        Vehicle vehicle = new Vehicle();
        String name = parseValue(VehicleFieldName.NAME);
        long xCoordinate = parseValue(VehicleFieldName.X_COORDINATES);
        Integer yCoordinate = parseValue(VehicleFieldName.Y_COORDINATES);
        int enginePower = parseValue(VehicleFieldName.ENGINE_POWER);
        Long numberOfWheels = parseValue(VehicleFieldName.NUMBER_OF_WHEELS);
        Double capacity = parseValue(VehicleFieldName.CAPACITY);
        VehicleType type = parseValue(VehicleFieldName.TYPE);

        vehicle.setName(name);
        Coordinates coordinates = new Coordinates();
        coordinates.setX(xCoordinate);
        coordinates.setY(yCoordinate);
        vehicle.setCoordinates(coordinates);
        vehicle.setEnginePower(enginePower);
        vehicle.setNumberOfWheels(numberOfWheels);
        vehicle.setCapacity(capacity);
        vehicle.setType(type);

        return vehicle;
    }
}
