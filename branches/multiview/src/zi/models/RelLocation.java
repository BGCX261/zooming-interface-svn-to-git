package zi.models;

import zi.views.ZIElementView;
import zi.views.ZIInformationPlaneView;

import javax.swing.border.Border;
import java.awt.*;

public class RelLocation {
    private double relX;
    private double relY;
    private double relWidth;
    private double absoluteProportion;// width/height

    public RelLocation(double relX, double relY, double relWidth, double absoluteProportion) {
        this.relWidth = relWidth;
        this.absoluteProportion = absoluteProportion;
        this.relY = relY;
        this.relX = relX;
    }

    public double getRelWidth() {
        return relWidth;
    }

    public double getRelY() {
        return relY;
    }

    public double getRelX() {
        return relX;
    }


    public boolean setRelBorder(double relX, double relY, double relWidth, boolean isItInfPane, double parentAbsoluteProp) {
        double relProportion = absoluteProportion/parentAbsoluteProp;
        if (((relX >= 0) && (relY >= 0) && ((relWidth + relX) <= 1) && ((relWidth/relProportion + relY) <= 1)) || isItInfPane) {//todo
            this.relWidth = relWidth;
            this.relY = relY;
            this.relX = relX;
            return true;
        }
        return false;
    }

    public void setRelBorder(Point p, Dimension size, ZIElementView parent) {
        if (parent.getOwner() == ZIInformationPlane.get()) {
            ZIInformationPlaneView view = (ZIInformationPlaneView) parent;
            relX = (p.x - view.getRelLocation().getX())/view.getRelLocation().getWidth();
            relY = (p.y - view.getRelLocation().getY())/view.getRelLocation().getHeight();
            relWidth = ((double)size.getWidth())/view.getRelLocation().getWidth();
            absoluteProportion = ((double)size.getWidth())/size.getHeight();
        } else {
            relX = ((double)p.x)/parent.getWidth();
            relY = ((double)p.y)/parent.getHeight();
            relWidth = ((double)size.getWidth())/parent.getWidth();
            absoluteProportion = ((double)size.getWidth())/size.getHeight();
        }
    }

    public double getAbsoluteProportion() {
        return absoluteProportion;
    }
}
