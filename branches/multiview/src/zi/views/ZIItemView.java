package zi.views;

import zi.models.ZIItem;
import zi.ZIController;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Olga Komaleva
 * Date: Mar 22, 2007
 */
public class ZIItemView extends ZIElementView {
    private final ZIItem owner;
    private final ZIController controller;

    public ZIItemView(ZIItem owner, ZIController controller) {
        this.owner = owner;
        this.controller = controller;
    }

    public ZIItem getOwner() {
        return owner;
    }

    public ZIController getController() {
        return controller;
    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);        
        g.drawString("Item", 3, 10);
    }
}
