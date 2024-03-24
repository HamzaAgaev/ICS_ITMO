package itmo.webprogramming.lab_4.controllers;

import itmo.webprogramming.lab_4.entities.User;
import itmo.webprogramming.lab_4.entities.UsernameAndPassword;
import itmo.webprogramming.lab_4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = {"http://localhost:3000/"})
@RestController
public class AuthController {
    private UserService userService;
    private LoginForAuthorized loginForAuthorized;

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(
            @RequestBody UsernameAndPassword usernameAndPassword
            ) {
        String username = usernameAndPassword.getUsername();
        String passwordHash = usernameAndPassword.getPasswordHash();
        if (username.length() < 3)
            return new ResponseEntity<String>("Too short username.", HttpStatus.BAD_REQUEST);
        if (this.userService.getUserByUsername(username) != null)
            return new ResponseEntity<String>("This user already exists.", HttpStatus.CONFLICT);
        User createdUser = this.userService.createUser(username, passwordHash);
        return new ResponseEntity<String>("User created! Hello, " + createdUser.getUsername(), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return this.loginForAuthorized.forAuthorizedUsersMethod(authorizationHeader);
    }

    @Autowired
    public void setLoginForAuthorized(LoginForAuthorized loginForAuthorized) {
        this.loginForAuthorized = loginForAuthorized;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
