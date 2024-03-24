package common.data;

public enum VehicleType {
    PLANE,
    BOAT,
    SPACESHIP;

    public static VehicleType parseVehicleType(String s) {
        switch (s) {
            case "PLANE":
                return VehicleType.PLANE;
            case "BOAT":
                return VehicleType.BOAT;
            case "SPACESHIP":
                return VehicleType.SPACESHIP;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case PLANE:
                return "PLANE";
            case BOAT:
                return "BOAT";
            case SPACESHIP:
                return "SPACESHIP";
            default:
                return "null";
        }
    }
}