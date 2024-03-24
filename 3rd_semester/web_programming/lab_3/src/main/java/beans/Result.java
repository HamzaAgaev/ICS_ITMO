package beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.*;

@Entity
@Table(name="Results", schema="s367786")
public class Result implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private int id;

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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return this.x;
    }
    public void setX(double x) {
        this.x = x;
        this.isHit = this.calcHit();
        this.dateAndTime = this.getCurrentDateAndTime();
    }

    public double getY() {
        return this.y;
    }
    public void setY(double y) {
        this.y = y;
        this.isHit = this.calcHit();
        this.dateAndTime = this.getCurrentDateAndTime();
    }

    public double getR() {
        return this.r;
    }
    public void setR(double r) {
        this.r = r;
        this.isHit = this.calcHit();
        this.dateAndTime = this.getCurrentDateAndTime();
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
        if (this.x > 0 && this.y > 0) return false;
        else if (this.x >= 0 && this.y <= 0) {
            if (this.x <= this.r && this.y >= -1 * this.r) return true;
        }
        else if (this.x <= 0 && this.y >= 0) {
            if (this.x >= -0.5 * this.r && this.y <= 2 * this.x + this.r) return true;
        }
        else if (this.x < 0 && this.y < 0) {
            if (Math.pow(this.x, 2) + Math.pow(this.y, 2) <= Math.pow(this.r, 2) / 4) return true;
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

    public String toString() {
        return "{" +
                "\"x\": " + this.x + ", " +
                "\"y\": " + this.y + ", " +
                "\"r\": " + this.r + ", " +
                "\"isHit\": " + this.isHit + ", " +
                "\"dateAndTime\": \"" + this.dateAndTime +
                "\"}";
    }
}
