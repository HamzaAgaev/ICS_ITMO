package itmo.webprogramming.lab_4.controllers;

import itmo.webprogramming.lab_4.entities.User;
import itmo.webprogramming.lab_4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class LoginForAuthorized extends ForAuthorizedUsers<String> implements Serializable {

    @Override
    protected ResponseEntity<String> OKResponseMethod(User findedUser) {
        return new ResponseEntity<String>("Hello, " + findedUser.getUsername(), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<String> OKResponseMethod(User findedUser, String argument) {
        return new ResponseEntity<String>("This method doesn't provide any args.", HttpStatus.BAD_REQUEST);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}