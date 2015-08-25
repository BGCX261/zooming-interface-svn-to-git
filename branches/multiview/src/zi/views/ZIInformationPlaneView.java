package zi.views;

import zi.ZIController;
import zi.ZIGlassPane;
import zi.models.Arrow;
import zi.models.Location;
import zi.models.ZIInformationPlane;

import javax.swing.*;
import java.awt.*;

public class ZIInformationPlaneView extends ZIContainerView {
    private Location myLocation = new Location(0, 0, 500, 500);

    public ZIInformationPlaneView(ZIController controller) {
        super(ZIInformationPlane.get(), controller);
        super.setLocation(0, 0);
        super.setSize(500, 500);
        setFocusable(true);
        doLayout();
    }

    /*
     * compute point for //todo
     */
    private Point computePoint(Point p1, Point p2, ZIElementView view) {
        double dx;
        double dy;
        if (p2.y == p1.y) {
            dy = 0;
            dx = ((double)view.getWidth())/2;
        } else if (p2.x == p1.x) {
            dx = 0;
            dy = ((double)view.getHeight())/2;
        } else {
            double p1Tg = -((double)(p2.x - p1.x))/(p2.y - p1.y);
            double parentTg = ((double)view.getWidth())/view.getHeight();

            if ((Math.abs(p1Tg) < parentTg)) {
                dy = ((double)view.getHeight())/2;
                dx = dy*Math.abs(p1Tg);
            } else {
                dx = ((double)view.getWidth())/2.0;
                dy = dx/Math.abs(p1Tg);
            }
        }
        if (p1.x < p2.x) {
            p1.x += dx;
        } else {
            p1.x -= dx;
        }

        if (p1.y < p2.y) {
            p1.y += dy;
        } else {
            p1.y -= dy;
        }

        return p1;

    }

    private void drawArrow(Graphics graphics, Arrow arrow){
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(3.0f));

        g.setColor(new Color(255, 181, 0, 150));

        ZIElementView parentView = arrow.parent.getView(getController());
        ZIElementView childView = arrow.child.getView(getController());

        Point p1 = new Point(parentView.getWidth()/2, parentView.getHeight()/2);
        Point p2 = new Point(childView.getWidth()/2, childView.getHeight()/2);

        Point beginPoint = SwingUtilities.convertPoint(parentView, p1, this);
        Point endPoint = SwingUtilities.convertPoint(childView, p2, this);

        beginPoint = computePoint(beginPoint, endPoint, parentView);
        endPoint = computePoint(endPoint, beginPoint, childView);
        
        g.drawLine(beginPoint.x, beginPoint.y,  endPoint.x, endPoint.y);

        // Arrow head in the beginning by Fedor Tsarev
        double vx = beginPoint.x - endPoint.x;
        double vy = beginPoint.y - endPoint.y;
        double d = Math.hypot(vx, vy);
        vx /= d;
        vy /= d;

        double vpx = -vy;
        double vpy = vx;

        double len = Math.min(10, d / 10);

        double px = beginPoint.x - len * vx;
        double py = beginPoint.y - len * vy;

        g.drawLine((int) (px - len / 2 * vpx), (int) (py - len / 2 * vpy), beginPoint.x, beginPoint.y);
        g.drawLine((int) (px + len / 2 * vpx), (int) (py + len / 2 * vpy), beginPoint.x, beginPoint.y);
    }

    private Component getRootPane(ZIElementView view) {//todo redo with project model
        Component a = view;
        Component c = a;

        while (c instanceof ZIElementView) {
            a = c;
            c = c.getParent();
        }
        return a;
    }

    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        for (Arrow arrow : ZIInformationPlane.get().getArrows()) {
            ZIElementView parentView = arrow.parent.getView(getController());
            ZIElementView childView = arrow.child.getView(getController());

            // following 3 ifs added by Fedor Tsarev
            if (parentView == null || childView == null) {
                continue;
            }
            if (parentView.getSize().width == 0 || parentView.getSize().height == 0) {
                continue;
            }
            if (childView.getSize().width == 0 || childView.getSize().height == 0) {
                continue;
            }


            if (!(getRootPane(parentView) instanceof ZIInformationPlaneView)
                    || !(getRootPane(childView) instanceof ZIInformationPlaneView)) {
                continue;
            }

            drawArrow(g, arrow);//beginPoint, endPoint);
        }
    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.pink);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);
        g.drawRect((int)myLocation.getX(), (int)myLocation.getY(), (int)myLocation.getWidth(), (int)myLocation.getHeight());
    }

    public void setLocation(int x, int y) {
    }

    public void setLocation(Point p) {
    }

    public Location getRelLocation() {
        return myLocation;
    }

    public void setRelLocation(Location location) {
        myLocation = location;
        doLayout();
    }

}
