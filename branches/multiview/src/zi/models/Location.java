package zi.models;

/**
 * Author: Olga Komaleva
 * Date: Feb 22, 2007
 */

public class Location {
    private double x;
    private double y;
    private double width;
    private double height;

    public Location() {}

    public Location(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Location(Location location) {
        this.x = location.x;
        this.y = location.y;
        this.width = location.width;
        this.height = location.height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

}