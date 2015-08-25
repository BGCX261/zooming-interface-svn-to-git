package zi.baseElements;

public class RelLocation {
    private double relX;
    private double relY;
    private double relWidth;
    private double relHeight;

    public RelLocation(double relX, double relY, double relWidth, double relHeight) {
        this.relHeight = relHeight;
        this.relWidth = relWidth;
        this.relY = relY;
        this.relX = relX;
    }

    public double getRelHeight() {
        return relHeight;
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

/*    public boolean setRelSize(double relWidth, double relHeight) {
        if (((relWidth + relX) <= 1) && ((relHeight + relY) <= 1)) {
            this.relWidth = relWidth;
            this.relHeight = relHeight;
            return true;
        }
        return false;
    }

    public boolean setRelLocation(double relX, double relY) {
        if (((relWidth + relX) <= 1) && ((relHeight + relY) <= 1)) {
            this.relX = relX;
            this.relY = relY;
            return true;
        }
        return false;
    }*/

    public boolean setRelBorder(double relX, double relY, double relWidth, double relHeight, boolean isItInfPane) {
        if ((((relWidth + relX) <= 1) && ((relHeight + relY) <= 1)) || isItInfPane) {//todo
            this.relHeight = relHeight;
            this.relWidth = relWidth;
            this.relY = relY;
            this.relX = relX;
            return true;
        }
        return false;
    }
}
