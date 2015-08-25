package zi;

import zi.baseElements.ZIItem;
import zi.baseElements.Location;
import zi.implementation.InformationPane;

import javax.swing.*;
import java.awt.*;
import java.awt.Container;

/**
 * Author: Olga Komaleva
 * Date: Feb 22, 2007
 */
public class InformationPaneLayout implements LayoutManager {

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
        ZIItem[] comps = InformationPane.get().getContainer().getContents();
        Location location = InformationPane.get().getRelLocation();

        for (ZIItem comp : comps) {
            int x = (int)(location.getX() + location.getWidth() * comp.getItem().getRelX());
            int y = (int)(location.getY() + location.getHeight() * comp.getItem().getRelY());
            int width = (int)(location.getWidth() *comp.getItem().getRelWidth());
            int height = (int)(location.getHeight() *comp.getItem().getRelHeight());

            if ((comp.getItem().getMinLength() == ZIItem.INFINITE_ZOOMING)
                    ||(width > comp.getItem().getMinLength())
                    && (height > comp.getItem().getMinLength())) {
                
                ((JComponent)comp).setLocation(x, y);

                if ((comp.getItem().getMaxLength() == ZIItem.INFINITE_ZOOMING)
                ||(width < comp.getItem().getMaxLength())
                && (height < comp.getItem().getMaxLength())) {

                    ((JComponent)comp).setSize(width, height);
                }
            } else {
                ((JComponent)comp).setSize(0, 0);
            }
        }
    }
}
