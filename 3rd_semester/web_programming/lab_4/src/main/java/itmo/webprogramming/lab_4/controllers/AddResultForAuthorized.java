package itmo.webprogramming.lab_4.controllers;

import itmo.webprogramming.lab_4.entities.CoordinatesXYR;
import itmo.webprogramming.lab_4.entities.Result;
import itmo.webprogramming.lab_4.entities.User;
import itmo.webprogramming.lab_4.services.ResultService;
import itmo.webprogramming.lab_4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AddResultForAuthorized extends ForAuthorizedUsers<CoordinatesXYR> implements Serializable {

    private ResultService resultService;

    @Override
    protected ResponseEntity<String> OKResponseMethod(User findedUser) {
        return new ResponseEntity<String>("Coordinates are null.", HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<?> OKResponseMethod(User findedUser, CoordinatesXYR argument) {
        Result createdResult = this.resultService.createResult(argument, findedUser);
        if (createdResult == null)
            return new ResponseEntity<String>("Result adding error!", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<Result>(createdResult, HttpStatus.OK);
    }

    @Autowired
    public void setResultService(ResultService resultService) {
        this.resultService = resultService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
