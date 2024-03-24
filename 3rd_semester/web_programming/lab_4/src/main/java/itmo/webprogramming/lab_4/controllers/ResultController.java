package itmo.webprogramming.lab_4.controllers;

import itmo.webprogramming.lab_4.entities.CoordinatesXYR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = {"http://localhost:3000/"})
@RestController
public class ResultController {
    private AddResultForAuthorized addResultForAuthorized;
    private GetAllResultsForAuthorized getAllResultsForAuthorized;
    private DeleteAllResultsForAuthorized deleteAllResultsForAuthorized;

    @PostMapping(value = "/addNewResult")
    public ResponseEntity<?> addNewResult(
            @RequestBody CoordinatesXYR coordinatesXYR,
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return this.addResultForAuthorized.forAuthorizedUsersMethod(authorizationHeader, coordinatesXYR);
    }

    @GetMapping(value = "/getAllUserResults")
    public ResponseEntity<?> getAllUserResults(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return this.getAllResultsForAuthorized.forAuthorizedUsersMethod(authorizationHeader);
    }

    @GetMapping(value = "/deleteAllUserResults")
    public ResponseEntity<?> deleteAllUserResults(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        return this.deleteAllResultsForAuthorized.forAuthorizedUsersMethod(authorizationHeader);
    }

    @Autowired
    public void setAddResultForAuthorized(AddResultForAuthorized addResultForAuthorized) {
        this.addResultForAuthorized = addResultForAuthorized;
    }

    @Autowired
    public void setGetAllResultsForAuthorized(GetAllResultsForAuthorized getAllResultsForAuthorized) {
        this.getAllResultsForAuthorized = getAllResultsForAuthorized;
    }

    @Autowired
    public void setDeleteAllResultsForAuthorized(DeleteAllResultsForAuthorized deleteAllResultsForAuthorized) {
        this.deleteAllResultsForAuthorized = deleteAllResultsForAuthorized;
    }
}
