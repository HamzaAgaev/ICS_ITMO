package client.argumentparsers;

import common.data.VehicleType;
import client.exceptions.ArgumentParserException;

public class VehicleTypeArgumentParser implements ArgumentParser<VehicleType> {
    @Override
    public VehicleType parseArgument(String argument) throws ArgumentParserException {
        if (argument.isEmpty()) return null;        // парсить как null если аргумент пустой
        switch (argument) {
            case "PLANE":
                return VehicleType.PLANE;
            case "BOAT":
                return VehicleType.BOAT;
            case "SPACESHIP":
                return VehicleType.SPACESHIP;
        }
        throw new ArgumentParserException("Argument cannot be parsed as VehicleType.");
    }
}
