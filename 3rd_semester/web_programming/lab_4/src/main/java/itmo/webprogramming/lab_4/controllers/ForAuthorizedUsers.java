package itmo.webprogramming.lab_4.controllers;

import itmo.webprogramming.lab_4.entities.User;
import itmo.webprogramming.lab_4.services.UserService;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Base64;

abstract class ForAuthorizedUsers<T> {

    protected UserService userService;

    public ResponseEntity<?> forAuthorizedUsersMethod(String authorizationHeader, T... argument) {
        String[] usernameAndPasswordHash = this.getUsernameAndPasswordHash(authorizationHeader);
        if (usernameAndPasswordHash == null) {
            return new ResponseEntity<String>("Your Authorization header is incorrect.", HttpStatus.BAD_REQUEST);
        }

        String username = usernameAndPasswordHash[0];
        String passwordHash = usernameAndPasswordHash[1];
        User findedUser = this.userService.getUserByUsernameAndPasswordHash(username, passwordHash);
        if (findedUser != null) {
            if (argument.length == 0)
                return this.OKResponseMethod(findedUser);
            else
                return this.OKResponseMethod(findedUser, argument[0]);
        } else {
            if (this.userService.getUserByUsername(username) != null)
                return new ResponseEntity<String>("Wrong password for this user!", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<String>("This user doesn't exists.", HttpStatus.UNAUTHORIZED);
        }
    }

    protected abstract ResponseEntity<?> OKResponseMethod(User findedUser);
    protected abstract ResponseEntity<?> OKResponseMethod(User findedUser, T argument);

    @Nullable
    private String[] getUsernameAndPasswordHash(String authorizationHeader) {
        String username, passwordHash;
        try {
            String hashedUserData = authorizationHeader.split(" ", 2)[1];
            String[] userData = (new String(Base64.getDecoder().decode(hashedUserData))).split(":", 2);
            username = userData[0];
            passwordHash = userData[1];
        } catch (Exception exception) {
            return null;
        }
        return new String[] {username, passwordHash};
    }

    abstract void setUserService(UserService userService);
}
