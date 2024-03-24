package itmo.webprogramming.lab_4.repositories;

import itmo.webprogramming.lab_4.entities.Result;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Result findResultById(Long id);
    ArrayList<Result> findResultsByUserId(Long userId);

    @Transactional
    void deleteAllByUserId(Long userId);
}