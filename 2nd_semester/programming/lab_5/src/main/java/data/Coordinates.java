package data;

public class Coordinates {
    private long x; //Значение поля должно быть больше -832
    private Integer y; //Максимальное значение поля: 802, Поле не может быть null

    public Coordinates() {}

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{x: " + x + ", y: " + y + "}";
    }
}