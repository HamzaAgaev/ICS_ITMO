package itmo.webprogramming.lab_4.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="Results", schema="s367786")
public class Result implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User user;

    @Column(name = "X", nullable = false)
    private double x;

    @Column(name = "Y", nullable = false)
    private double y;

    @Column(name = "R", nullable = false)
    private double r;

    @Column(name = "HIT", nullable = false)
    private boolean isHit;

    @Column(name = "DATETIME", nullable = false)
    private String dateAndTime;

    public Result() {}

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getX() {
        return this.x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return this.r;
    }
    public void setR(double r) {
        this.r = r;
    }

    public boolean getIsHit() {
        return this.isHit;
    }
    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
    }

    public String getDateAndTime() {
        return this.dateAndTime;
    }
    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setHitResult() {
        this.isHit = this.calcHit();
    }

    private boolean calcHit() {
        if (this.r >= 0) {
            if (this.x > 0 && this.y >= 0) {
                if (this.x <= this.r && this.y <= this.x * -0.5 + this.r * 0.5) return true;
            } else if (this.x > 0 && this.y < 0) {
                return false;
            } else if (this.x <= 0 && this.y >= 0) {
                if (this.x >= this.r * -1 && this.y <= this.r) return true;
            } else if (this.x <= 0 && this.y < 0) {
                if (Math.pow(this.x, 2) + Math.pow(this.y, 2) <= Math.pow(this.r, 2)) return true;
            }
        } else {
            if (this.x < 0 && this.y <= 0) {
                if (this.x >= this.r && this.y >= this.x * -0.5 + this.r * 0.5) return true;
            } else if (this.x < 0 && this.y > 0) {
                return false;
            } else if (this.x >= 0 && this.y <= 0) {
                if (this.x <= this.r * -1 && this.y >= this.r) return true;
            } else if (this.x >= 0 && this.y > 0) {
                if (Math.pow(this.x, 2) + Math.pow(this.y, 2) <= Math.pow(this.r, 2)) return true;
            }
        }
        return false;
    }

    public void setCurrentDateAndTime() {
        this.dateAndTime = this.getCurrentDateAndTime();
    }

    private String getCurrentDateAndTime() {
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return currentDateAndTime.format(formatter);
    }

    public void setNewHitValues() {
        this.isHit = this.calcHit();
        this.dateAndTime = this.getCurrentDateAndTime();
    }

    @Override
    public String toString() {
        return "{" +
                "\"x\": " + this.x + ", " +
                "\"y\": " + this.y + ", " +
                "\"r\": " + this.r + ", " +
                "\"isHit\": " + this.isHit + ", " +
                "\"dateAndTime\": \"" + this.dateAndTime +
                "\"userId\": \"" + this.user.getId() +
                "\"}";
    }
}
