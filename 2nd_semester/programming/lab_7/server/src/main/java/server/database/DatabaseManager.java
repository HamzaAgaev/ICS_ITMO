package server.database;

import common.data.Coordinates;
import common.data.User;
import common.data.Vehicle;
import common.data.VehicleType;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseManager {
    private final static DatabaseManager instance = new DatabaseManager();
    private DatabaseManager() {}

    public static DatabaseManager getInstance() {
        return instance;
    }

    private Connection connection;
    private boolean isAllTablesCreated = false;
    private boolean isRunning = false;

    public boolean getIsRunning() {
        return isRunning;
    }

    public boolean getIsAllTablesCreated() {
        return isAllTablesCreated;
    }

    public boolean run() throws IOException, SQLException {
        if (!isRunning) {
            Properties properties = new Properties();
            properties.load(new FileInputStream("db.cfg"));
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/studs", properties);
            createAllTables();
            isRunning = true;
        }
        return isRunning;
    }

    private void createAllTables() throws SQLException {
        try {
            connection.setAutoCommit(false);
            Statement creatorStatement = connection.createStatement();
            String createUsers = """
            CREATE TABLE IF NOT EXISTS VEHICLES_USERS (
                ID SERIAL PRIMARY KEY,
                USERNAME VARCHAR(255) NOT NULL UNIQUE, CHECK (LENGTH(USERNAME) >= 3),
                PASSWORD_HASH CHAR(32) NOT NULL
            )""";
            String createVehicles = """
            CREATE TABLE IF NOT EXISTS VEHICLES (
                ID SERIAL PRIMARY KEY,
                NAME VARCHAR(255) NOT NULL, CHECK (LENGTH(NAME) > 0),
                COORDINATE_X BIGINT NOT NULL, CHECK (COORDINATE_X > -832),
                COORDINATE_Y INT NOT NULL, CHECK (COORDINATE_Y <= 802),
                CREATION_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                ENGINE_POWER INT NOT NULL, CHECK (ENGINE_POWER > 0),
                NUMBER_OF_WHEELS BIGINT NOT NULL, CHECK (NUMBER_OF_WHEELS > 0),
                CAPACITY DOUBLE PRECISION NOT NULL, CHECK (CAPACITY > 0),
                TYPE VARCHAR(255),
                OWNER_USER_ID INT NOT NULL REFERENCES VEHICLES_USERS(ID)
            )""";
            creatorStatement.executeUpdate(createUsers);
            creatorStatement.executeUpdate(createVehicles);
            connection.commit();
            connection.setAutoCommit(true);
            isAllTablesCreated = true;
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
            connection.rollback();
        }
    }

    public synchronized ArrayList<User> getAllUsers() {
        try {
            String getUsers = """
            SELECT * FROM VEHICLES_USERS
            """;
            PreparedStatement getAllUsersStatement = connection.prepareStatement(getUsers);
            ResultSet allUsersSet = getAllUsersStatement.executeQuery();
            ArrayList<User> users = new ArrayList<>();
            while (allUsersSet.next()) {
                User user = new User();
                user.setId(allUsersSet.getInt("ID"));
                user.setUsername(allUsersSet.getString("USERNAME"));
                user.setPasswordHash(allUsersSet.getString("PASSWORD_HASH"));
                users.add(user);
            }
            return users;
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
            return null;
        }
    }

    public synchronized boolean addNewUser(User user) {
        try {
            String addUser = """
            INSERT INTO VEHICLES_USERS (USERNAME, PASSWORD_HASH) VALUES (?, ?)
            """;
            PreparedStatement addUserStatement = connection.prepareStatement(addUser);
            addUserStatement.setString(1, user.getUsername());
            addUserStatement.setString(2, user.getPasswordHash());
            int addedUsersCount = addUserStatement.executeUpdate();
            return addedUsersCount == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    public synchronized ArrayList<Vehicle> getAllVehicles() {
        try {
            String getVehicles = """
            SELECT * FROM VEHICLES
            """;
            PreparedStatement getAllVehiclesStatement = connection.prepareStatement(getVehicles);
            ResultSet allVehiclesSet = getAllVehiclesStatement.executeQuery();
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            while (allVehiclesSet.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(allVehiclesSet.getInt("ID"));
                vehicle.setName(allVehiclesSet.getString("NAME"));
                Coordinates coordinates = new Coordinates();
                coordinates.setX(allVehiclesSet.getLong("COORDINATE_X"));
                coordinates.setY(allVehiclesSet.getInt("COORDINATE_Y"));
                vehicle.setCoordinates(coordinates);
                vehicle.setCreationDate(allVehiclesSet.getTimestamp("CREATION_DATE").toLocalDateTime());
                vehicle.setEnginePower(allVehiclesSet.getInt("ENGINE_POWER"));
                vehicle.setNumberOfWheels(allVehiclesSet.getLong("NUMBER_OF_WHEELS"));
                vehicle.setCapacity(allVehiclesSet.getDouble("CAPACITY"));
                vehicle.setType(VehicleType.parseVehicleType(allVehiclesSet.getString("TYPE")));
                vehicle.setOwnerUserId(allVehiclesSet.getInt("OWNER_USER_ID"));
                vehicles.add(vehicle);
            }
            return vehicles;
        } catch (SQLException sqlException) {
            return null;
        }
    }

    public synchronized boolean addNewVehicle(Vehicle vehicle) {
        try {
            String addVehicle = """
            INSERT INTO VEHICLES (NAME, COORDINATE_X, COORDINATE_Y, CREATION_DATE, ENGINE_POWER, NUMBER_OF_WHEELS, CAPACITY, TYPE, OWNER_USER_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement addVehicleStatement = connection.prepareStatement(addVehicle);
            setValuesToVehicleStatement(addVehicleStatement, vehicle);
            int addedVehiclesCount = addVehicleStatement.executeUpdate();
            return addedVehiclesCount == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    public synchronized boolean updateVehicles(Integer id, Vehicle vehicle) {
        try {
            String updateVehicle = """
            UPDATE VEHICLES
            SET NAME = ?, COORDINATE_X = ?, COORDINATE_Y = ?, CREATION_DATE = ?, ENGINE_POWER = ?, NUMBER_OF_WHEELS = ?, CAPACITY = ?, TYPE = ?, OWNER_USER_ID = ?
            WHERE ID = ?
            """;
            PreparedStatement updateVehicleStatement = connection.prepareStatement(updateVehicle);
            setValuesToVehicleStatement(updateVehicleStatement, vehicle);
            updateVehicleStatement.setInt(10, id);
            int updatedVehiclesCount = updateVehicleStatement.executeUpdate();
            return updatedVehiclesCount == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    public synchronized boolean removeVehiclesById(Integer id) {
        try {
            String removeVehicle = """
            DELETE FROM VEHICLES WHERE ID = ?
            """;
            PreparedStatement removeVehicleStatement = connection.prepareStatement(removeVehicle);
            removeVehicleStatement.setInt(1, id);
            int removedVehiclesCount = removeVehicleStatement.executeUpdate();
            return removedVehiclesCount == 1;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    public synchronized boolean clearVehiclesByOwnerUserId(int ownerUserId) {
        try {
            String clearVehicles = """
            DELETE FROM VEHICLES WHERE OWNER_USER_ID = ?
            """;
            PreparedStatement clearVehiclesStatement = connection.prepareStatement(clearVehicles);
            clearVehiclesStatement.setInt(1, ownerUserId);
            int clearVehiclesCount = clearVehiclesStatement.executeUpdate();
            return clearVehiclesCount > 0;
        } catch (SQLException sqlException) {
            return false;
        }
    }

    private void setValuesToVehicleStatement(PreparedStatement vehicleStatement, Vehicle vehicle) throws SQLException {
        vehicleStatement.setString(1, vehicle.getName());
        vehicleStatement.setLong(2, vehicle.getCoordinates().getX());
        vehicleStatement.setInt(3, vehicle.getCoordinates().getY());
        vehicleStatement.setTimestamp(4, Timestamp.valueOf(vehicle.getCreationDate()));
        vehicleStatement.setInt(5, vehicle.getEnginePower());
        vehicleStatement.setLong(6, vehicle.getNumberOfWheels());
        vehicleStatement.setDouble(7, vehicle.getCapacity());
        String vehicleTypeString = vehicle.getType() == null ? "null" : vehicle.getType().toString();
        vehicleStatement.setString(8, vehicleTypeString);
        vehicleStatement.setInt(9, vehicle.getOwnerUserId());
    }
}
