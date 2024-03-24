package client.netclient;

import client.exceptions.SendingException;
import common.data.CommandName;
import common.data.LoginStatus;
import common.data.RegisterStatus;
import common.data.User;
import common.netentities.RequestEntity;
import common.netentities.ResponseEntity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ClientAuthorizationManager {
    private static ClientAuthorizationManager instance = new ClientAuthorizationManager();
    private ClientAuthorizationManager() {}

    public static ClientAuthorizationManager getInstance() {
        return instance;
    }

    private ClientManager clientManager = ClientManager.getInstance();

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    private String hashPassword(String password) {
        final String algorithm = "MD2";
        String hashedPassword;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] hashedPasswordBytes = messageDigest.digest(password.getBytes());
            BigInteger hashedPasswordBI = new BigInteger(1, hashedPasswordBytes);
            hashedPassword = hashedPasswordBI.toString(16);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            hashedPassword = password;
        }
        while (hashedPassword.length() < 32) {
            hashedPassword = "0" + hashedPassword;
        }
        return hashedPassword;
    }

    public LoginStatus login(String username, String password) throws SendingException {
        String passwordHash = hashPassword(password);
        User userToLogin = new User(username, passwordHash);
        RequestEntity loginRequest = new RequestEntity(CommandName.LOGIN, userToLogin);
        ResponseEntity<Boolean> loginResponse = clientManager.getResponseEntity(loginRequest);
        LoginStatus loginStatus = loginResponse.getLoginStatus();
        if (loginStatus == LoginStatus.SUCCESS) {
            RequestEntity createdUserRequest = new RequestEntity(CommandName.GET_USER, userToLogin);
            currentUser = (User) clientManager.getResponseEntity(createdUserRequest).getValue();
        }
        return loginStatus;
    }

    public RegisterStatus register(String username, String password) throws SendingException {
        String passwordHash = hashPassword(password);
        User userToRegister = new User(username, passwordHash);
        RequestEntity registerRequest = new RequestEntity(CommandName.REGISTER, userToRegister);
        ResponseEntity<RegisterStatus> registerResponse = clientManager.getResponseEntity(registerRequest);
        RegisterStatus registerStatus = registerResponse.getValue();
        return registerStatus;
    }
}
