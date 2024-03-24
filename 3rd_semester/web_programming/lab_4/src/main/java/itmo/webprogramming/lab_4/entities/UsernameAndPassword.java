package itmo.webprogramming.lab_4.entities;

import org.springframework.stereotype.Component;

@Component
public class UsernameAndPassword {
    private String username;
    private String passwordHash;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
