package zi.baseElements;

import javax.swing.*;
import java.awt.*;

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

    public Location(Component container) {
        x = container.getX();
        y = container.getY();
        width = container.getWidth();
        height = container.getHeight();
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

    public void out() {
        System.out.println("--------------------------------");
        System.out.println("X= " + x);
        System.out.println("Y= " + y);
        System.out.println("Width= " + width);
        System.out.println("Height= " + height);
        System.out.println("--------------------------------");
    }
}
