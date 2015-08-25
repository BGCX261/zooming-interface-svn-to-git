package zi;

import zi.models.Location;
import zi.models.ZIInformationPlane;
import zi.models.ZIContainer;
import zi.models.ZIItem;
import zi.views.ZIInformationPlaneView;
import zi.views.ZIItemView;
import zi.views.ZIElementView;

import java.awt.*;
import java.awt.Container;

/**
 * Author: Olga Komaleva
 * Date: Feb 22, 2007
 */
public class ZIInformationPlaneLayout implements LayoutManager {
    private final ZIController controller;
    private final ZIInformationPlaneView view;

    public ZIInformationPlaneLayout(ZIController controller, ZIInformationPlaneView view) {
        this.view = view;
        this.controller = controller;
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }
        
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    public void layoutContainer(Container parent) {
        ZIItem[] comps = ZIInformationPlane.get().getItems();
        Location location = view.getRelLocation();
        if (location != null) {
            for (ZIItem comp : comps) {
                int x = (int)(location.getX() + location.getWidth() * comp.getRelX());
                int y = (int)(location.getY() + location.getHeight() * comp.getRelY());
                int width = (int)(location.getWidth() *comp.getRelWidth());
                int height = (int)(location.getWidth() *comp.getRelWidth()/comp.getAbsoluteProportion());

                if ((comp.getMinLength() == ZIItem.INFINITE_ZOOMING)
                        ||(width > comp.getMinLength())
                        && (height > comp.getMinLength())) {

                    comp.getView(controller).setLocation(x, y);

                    if ((comp.getMaxLength() == ZIItem.INFINITE_ZOOMING)
                    ||(width < comp.getMaxLength())
                    && (height < comp.getMaxLength())) {
                        ZIElementView v = comp.getView(controller);

                        v.setSize(width, height);
                    }
                } else {
                    comp.getView(controller).setSize(0, 0);
                }
            }
        }

    }
}
