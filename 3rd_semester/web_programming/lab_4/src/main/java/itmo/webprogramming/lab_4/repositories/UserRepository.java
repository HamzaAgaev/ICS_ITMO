package itmo.webprogramming.lab_4.repositories;

import itmo.webprogramming.lab_4.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);
    User findUserByUsername(String username);
    User findUserByUsernameAndPasswordHash(String username, String passwordHash);
}
