package zi.baseElements;

import zi.baseElements.RelLocation;
import zi.baseElements.ZIContainer;
import zi.implementation.InformationPane;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 22.02.2007
 * Time: 21:32:00
 */
public class ZIItemAdapter {

    private RelLocation location;
    private ZIContainer parent;
    private final int minLength;
    private final int maxLength;

    private double initZoom;
    private Dimension initDimension;


    public ZIItemAdapter(ZIContainer parent, int minLength, int maxLength, RelLocation location) {
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.location = location;
        this.parent = parent;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public double getRelX() {
        return location.getRelX();
    }

    public double getRelY() {
        return location.getRelY();
    }

    public double getRelWidth() {
        return location.getRelWidth();
    }

    public double getRelHeight() {
        return location.getRelHeight();
    }

    public ZIContainer getMyParent() {
        return parent;
    }

    /*public void setRelLocation(double relX, double relY) {
        location.setRelLocation(relX, relY);
    }

    public void setRelSize(double relWidth, double relHeight) {
        location.setRelSize(relWidth, relHeight);
    }*/

    public boolean setRelBorder(double relX, double relY, double relWidth, double relHeight) {
        return location.setRelBorder(relX, relY, relWidth, relHeight, (parent == InformationPane.get()));    
    }

    public void setMyParent(ZIContainer parent) {
        this.parent = parent;
    }

    public double getInitZoom() {
        return initZoom;
    }

    public Dimension getInitDimension() {
        return initDimension;
    }
}
