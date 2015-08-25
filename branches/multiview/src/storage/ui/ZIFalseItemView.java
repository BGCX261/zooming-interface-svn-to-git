package storage.ui;

import zi.views.ZIItemView;
import zi.ZIController;

import java.awt.*;

/**
 * View representing item, whose state could be not identical to saved one.
 *
 * @author www
 */
public class ZIFalseItemView extends ZIItemView {
    private final ZIFalseElement element;
    private final static Color FALSE_ITEM_COLOR = new Color(175, 208, 255);

    public ZIFalseItemView(ZIFalseItem owner, ZIController controller) {
        super(owner, controller);
        element = owner;
    }

    protected void paintComponent(Graphics g) {
        if (element.isMarked()) {
            g.setColor(FALSE_ITEM_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("Item", 3, 10);
        } else {
            super.paintComponent(g);
        }
    }
}