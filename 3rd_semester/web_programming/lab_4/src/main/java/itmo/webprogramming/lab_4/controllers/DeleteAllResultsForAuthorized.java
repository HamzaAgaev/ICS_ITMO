package itmo.webprogramming.lab_4.controllers;

import itmo.webprogramming.lab_4.entities.User;
import itmo.webprogramming.lab_4.services.ResultService;
import itmo.webprogramming.lab_4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class DeleteAllResultsForAuthorized extends ForAuthorizedUsers<String> implements Serializable {
    private ResultService resultService;

    @Override
    protected ResponseEntity<String> OKResponseMethod(User findedUser) {
        this.resultService.deleteAllResultsByUserId(findedUser.getId());
        return new ResponseEntity<String>("All Results deleted.", HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<String> OKResponseMethod(User findedUser, String argument) {
        return new ResponseEntity<String>("This method doesn't provide any args.", HttpStatus.BAD_REQUEST);
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
