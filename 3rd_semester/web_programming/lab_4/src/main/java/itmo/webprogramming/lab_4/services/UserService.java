package itmo.webprogramming.lab_4.services;

import itmo.webprogramming.lab_4.entities.User;
import itmo.webprogramming.lab_4.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // may return Null
    public User getUserById(Long id) {
        return this.userRepository.findUserById(id);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    // may return Null
    public User getUserByUsernameAndPasswordHash(String username, String passwordHash) {
        return this.userRepository.findUserByUsernameAndPasswordHash(username, passwordHash);
    }

    // may return Null
    public User createUser(String username, String passwordHash) {
        User newUser = new User();
        newUser.setId(0L);      // automatic
        newUser.setUsername(username);
        newUser.setPasswordHash(passwordHash);
        User savedUser = this.userRepository.save(newUser);
        return savedUser;
    }
}
