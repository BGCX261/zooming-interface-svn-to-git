package zi;

import zi.views.ZIElementView;
import zi.models.ZIContainer;
import zi.models.ZIItem;

import java.awt.*;
import java.awt.Container;

/**
 * Author: Olga Komaleva
 * Date: Feb 10, 2007
 */
public class ZIContainerLayout implements LayoutManager {
    private final ZIController controller;

    public ZIContainerLayout(ZIController controller) {
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
        ZIItem[] comps = ((ZIContainer)((ZIElementView)parent).getOwner()).getItems();

        for (ZIItem comp : comps) {
            int x = (int)(parent.getWidth()*comp.getRelX());
            int y = (int)(parent.getHeight()*comp.getRelY());
            int width = (int)(parent.getWidth()*comp.getRelWidth());
            int height = (int)(parent.getWidth()*comp.getRelWidth()/comp.getAbsoluteProportion());

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
