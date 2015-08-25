package zi.implementation;

import zi.ZIListener;
import zi.baseElements.*;

import javax.swing.*;

/**
 * Author: Olga Komaleva
 * Date: Feb 26, 2007
 */
public class ZIToolBar extends JToolBar implements ZICompoundItem {
    private final ZIItemAdapter containee;
    private final ZIContainerAdapter container;

    ZIToolBar(ZIContainer parent, int minlength, int maxLength, RelLocation location) {
        super();
        containee = new ZIItemAdapter(parent, minlength, maxLength, location);
        container = new ZIContainerAdapter(this);
    }

    public ZIItemAdapter getItem() {
        return containee;
    }

    public ZIContainerAdapter getContainer() {
        return container;
    }

    public void addZIListener(ZIListener listener) {
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
    }

    public void changeActivityState() {
        //TODO: ZIToolBar cannot be activated?
    }
}

