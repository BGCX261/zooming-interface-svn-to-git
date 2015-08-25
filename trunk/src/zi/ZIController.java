package zi;

import zi.baseElements.*;
import zi.implementation.InformationPane;

import javax.swing.*;
import java.awt.*;

public class ZIController {
    private static final double ZOOM = 0.1;
    private static final int INDENT = 10;
    private final InformationPane infPane = InformationPane.get();

    private ZIContainer currentContainer = infPane;
    private ZIContainer oldContainer = infPane;

    private Location cc;
    private Container panel;
    private boolean ccChanged = false;

    public ZIController(Container panel) {
        this.panel = panel;
        cc = new Location(panel);
        panel.add(infPane);
    }

    public void moveCenter(int x, int y) {
        cc.setX(cc.getX() + x);
        cc.setY(cc.getY() + y);

        findCurrent();
        repaint();
    }

    private void repaint() {
        if (currentContainer == infPane) {
            infPane.setRelLocation(cc);
        }

        if (ccChanged) {
            panel.remove((JComponent)oldContainer);
            if (oldContainer != infPane) {
                ZICompoundItem container = (ZICompoundItem)oldContainer;
                ZIContainer parent = container.getItem().getMyParent();
                parent.getContainer().add(container);
            }
            oldContainer = currentContainer;
            panel.add((JComponent)currentContainer);
            ccChanged = false;
        }

        ((JComponent)currentContainer).setLocation((int)cc.getX(), (int)cc.getY());
        ((JComponent)currentContainer).setSize((int)cc.getWidth(), (int)cc.getHeight());
        panel.repaint();
    }

    public void zoom(Point point) {
        currentContainer = infPane;
        ccChanged = true;

        Location location = infPane.getRelLocation();
        location.setX(location.getX() - point.x + panel.getWidth() * 0.5);
        location.setY(location.getY() - point.y + panel.getHeight() * 0.5);
        cc = location;
        findCurrent();
        repaint();
    }
    
    public void zoom(ZICompoundItem c) {

        currentContainer = c;
        ccChanged = true;
        JComponent container = (JComponent)c;
        if ((((double)container.getWidth()) / container.getHeight() )
                > (panel.getWidth() / panel.getHeight())) {
            cc.setHeight(((double)(panel.getWidth() - 2*INDENT)) * c.getItem().getRelHeight() /c.getItem().getRelWidth());
            cc.setWidth(panel.getWidth() - 2*INDENT);
            cc.setX(INDENT);
            cc.setY(((panel.getHeight() - cc.getHeight()))/2);

        } else {
            cc.setWidth(((double)(panel.getHeight() - 2*INDENT)) * c.getItem().getRelWidth() / c.getItem().getRelHeight());
            cc.setHeight(panel.getHeight() - 2*INDENT);
            cc.setX((panel.getWidth() - cc.getWidth())/2);
            cc.setY(INDENT);
        }

        findCurrent();
        repaint();
    }

    private void findCurrent() {
        //--- from parent
        while (((cc.getX() > 0) || (cc.getY() > 0)
                || (cc.getX() + cc.getWidth() < panel.getWidth() )
                || (cc.getY() + cc.getHeight() < panel.getHeight() ))
                && (currentContainer != infPane)) {
            ZIItem container = (ZIItem)currentContainer;
            cc.setWidth((cc.getWidth() / container.getItem().getRelWidth()));
            cc.setHeight((cc.getHeight() / container.getItem().getRelHeight()));
            cc.setX(cc.getX() - (container.getItem().getRelX() * cc.getWidth()));
            cc.setY(cc.getY() - (container.getItem().getRelY() * cc.getHeight()));
            currentContainer = container.getItem().getMyParent();
            ccChanged = true;
        }

        //--- from child
        for (ZICompoundItem container : currentContainer.getContainer().getContainers()) {
            double childX = cc.getX() + container.getItem().getRelX() * cc.getWidth();
            double childY = cc.getY() + container.getItem().getRelY() * cc.getHeight();
            double childXe = childX + container.getItem().getRelWidth() * cc.getWidth();
            double childYe = childY + container.getItem().getRelHeight() * cc.getHeight();

            if ((childX <= 0) && (childY <= 0) && (childXe >= panel.getWidth() )
                    && (childYe >= panel.getHeight() )) {
                currentContainer = container;
                ccChanged = true;

                cc.setX(childX);
                cc.setY(childY);
                cc.setWidth(container.getItem().getRelWidth() * cc.getWidth());
                cc.setHeight(container.getItem().getRelHeight() * cc.getHeight());
                break;
            }
        }  
    }

    private Location zooming(Location old, int i) {
        Location location = new Location();
        double o2 = (old.getWidth() - panel.getWidth() * 0.5 + old.getX()) / (panel.getWidth() * 0.5 - old.getX());
        double o3 = (old.getHeight() - panel.getHeight() * 0.5 + old.getY()) / (panel.getHeight() * 0.5 - old.getY());

        location.setWidth(old.getWidth() * (1 - i * ZOOM));
        location.setHeight(old.getHeight() * (1 - i * ZOOM));
        location.setX(panel.getWidth() * 0.5 - location.getWidth() / (1 + o2));
        location.setY(panel.getHeight() * 0.5 - location.getHeight() / (1 + o3));
        return location;
    }

    public void zoom(int i) {
        if (currentContainer != infPane) {
            ZIItem container = (ZIItem)currentContainer;
            Location location = zooming(cc, i);
            ZIItemAdapter containee = container.getItem();
            if (((containee.getMinLength() == ZIItem.INFINITE_ZOOMING)
                    || ((location.getWidth() > containee.getMinLength()) && (location.getHeight() > containee.getMinLength())))
                    && ((containee.getMaxLength() == ZIItem.INFINITE_ZOOMING)
                    || ((location.getWidth() < containee.getMaxLength()) && (location.getHeight() < containee.getMaxLength())))) {
                cc = location;
                findCurrent();
                repaint();
            }
        } else {
            cc = zooming(infPane.getRelLocation(), i);
            findCurrent();
            repaint();
        }
    }

    public void zoomToParent() {
        if (currentContainer != infPane) {
            ZIContainer container = ((ZIItem)currentContainer).getItem().getMyParent();
            if (container != infPane) {
                zoom((ZICompoundItem) container);
            }
        }
    }

    protected ZIContainer getCurrentContainer() {
        return currentContainer;
    }
}
