package data.validators;

import data.Coordinates;

public class CoordinatesValidator {
    public static Boolean isValidCoordinates(Coordinates coordinates) {
        long x = coordinates.getX();
        Integer y = coordinates.getY();
        return isValidX(x) && isValidY(y);
    }

    public static Boolean isValidX(long x) {
        return x > -832;
    }

    public static Boolean isValidY(Integer y) {
        if (y == null)
            return false;
        return y.compareTo(802) <= 0;
    }
}
