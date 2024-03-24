package server.users;

import common.data.LoginStatus;
import common.data.RegisterStatus;
import common.data.User;
import server.database.DatabaseManager;

//import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserManager {
    private static UserManager instance = new UserManager();
    private UserManager() {}

    public static UserManager getInstance() {
        return instance;
    }

    private final DatabaseManager databaseManager = DatabaseManager.getInstance();
    private CopyOnWriteArrayList<User> users;
    private boolean isRunning = false;

    public boolean getIsRunning() {
        return isRunning;
    }

    public boolean run() {
        if (!isRunning) {
            users = getAllUsers();
            isRunning = true;
        }
        return isRunning;
    }

    public synchronized CopyOnWriteArrayList<User> getAllUsers() {
        return new CopyOnWriteArrayList<>(databaseManager.getAllUsers());
    }

    public LoginStatus getLoginStatusByUsernameAndPasswordHash(String username, String passwordHash) {
        Optional<User> optionalUser = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        if (optionalUser.isEmpty()) {
            return LoginStatus.NO_SUCH_USER;
        } else {
            User user = optionalUser.get();
            if (!user.getPasswordHash().equals(passwordHash)) {
                return LoginStatus.WRONG_PASSWORD;
            }
        }
        return LoginStatus.SUCCESS;
    }

    public User getUserByUsernameAndPasswordHash(String username, String passwordHash) {
        User findedUser = users.stream().filter(user -> user.getUsername().equals(username) && user.getPasswordHash().equals(passwordHash)).findFirst().orElse(null);
        return findedUser;
    }

    public synchronized RegisterStatus addNewUser(User user) {
        if (getLoginStatusByUsernameAndPasswordHash(user.getUsername(), user.getPasswordHash()) == LoginStatus.NO_SUCH_USER) {
            if (user.getUsername().length() < 3) {
                return RegisterStatus.BAD_USER;
            }
            boolean isAdded = databaseManager.addNewUser(user);
            if (isAdded) {
                users = getAllUsers();
                return RegisterStatus.SUCCESS;
            }
            return RegisterStatus.BAD_USER;
        } else {
            return RegisterStatus.USER_ALREADY_EXISTS;
        }
    }
}
