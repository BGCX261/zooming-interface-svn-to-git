package zi.implementation;

import zi.baseElements.ZIContainer;
import zi.baseElements.ZIItem;
import zi.baseElements.ZIItemAdapter;
import zi.baseElements.RelLocation;
import zi.ZIListener;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Olga Komaleva
 * Date: Feb 26, 2007
 */
public class ZIButton extends JButton implements ZIItem {
    private final ZIItemAdapter containee;

    public ZIButton(ZIContainer myparent, int minLength, int maxLength, RelLocation location) {
        containee = new ZIItemAdapter(myparent, minLength, maxLength, location);
    }

    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
    }

    public void setVisible(boolean flag) {
        
    }

    public ZIItemAdapter getItem() {
        return containee;
    }

}
