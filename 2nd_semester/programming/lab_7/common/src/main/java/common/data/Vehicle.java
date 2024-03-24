package common.data;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Vehicle implements Comparable<Vehicle>, Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int enginePower; //Значение поля должно быть больше 0
    private Long numberOfWheels; //Поле не может быть null, Значение поля должно быть больше 0
    private Double capacity; //Поле может быть null, Значение поля должно быть больше 0
    private VehicleType type; //Поле может быть null
    private int ownerUserId; //Поле не null, генерируется автоматически

    public Vehicle() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public Long getNumberOfWheels() {
        return numberOfWheels;
    }

    public void setNumberOfWheels(Long numberOfWheels) {
        this.numberOfWheels = numberOfWheels;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    @Override
    public String toString() {
        return "{id: " + id + ", name: " + name + ", coordinates: " + coordinates +
                ", creationDate: " + creationDate + ", enginePower: " + enginePower +
                ", numberOfWheels: " + numberOfWheels + ", capacity: " + capacity +
                ", type: " + type + ", ownerUserId: " + ownerUserId + "}";
    }

    @Override
    public int compareTo(Vehicle vehicle) {
        return this.name.compareTo(vehicle.getName());
    }
}