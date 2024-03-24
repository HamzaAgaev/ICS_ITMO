package itmo.webprogramming.lab_4.services;

import itmo.webprogramming.lab_4.entities.CoordinatesXYR;
import itmo.webprogramming.lab_4.entities.Result;
import itmo.webprogramming.lab_4.entities.User;
import itmo.webprogramming.lab_4.repositories.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ResultService {
    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    // may return Null
    public Result getResultById(Long id) {
        return this.resultRepository.findResultById(id);
    }

    // may return Null
    public ArrayList<Result> getResultsByUserId(Long userId) {
        return this.resultRepository.findResultsByUserId(userId);
    }

    // may do nothing
    public void deleteAllResultsByUserId(Long userId) {
        this.resultRepository.deleteAllByUserId(userId);
    }

    // may return Null
    public Result createResult(CoordinatesXYR coordinatesXYR, User user) {
        Result newResult = new Result();
        newResult.setId(0L);        // automatic
        newResult.setUser(user);
        newResult.setX(coordinatesXYR.getX());
        newResult.setY(coordinatesXYR.getY());
        newResult.setR(coordinatesXYR.getR());
        newResult.setNewHitValues();
        Result savedResult = this.resultRepository.save(newResult);
        return savedResult;
    }
}
