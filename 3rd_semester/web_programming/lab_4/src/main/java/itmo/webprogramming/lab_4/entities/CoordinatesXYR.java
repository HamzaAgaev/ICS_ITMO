package itmo.webprogramming.lab_4.entities;

import org.springframework.stereotype.Component;

@Component
public class CoordinatesXYR {
    private double x;
    private double y;
    private double r;

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
}
