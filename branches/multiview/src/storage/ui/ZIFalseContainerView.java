package storage.ui;

import zi.views.ZIContainerView;
import zi.ZIController;

import java.awt.*;

/**
 * View representing container, whose state could be not identical to saved one.
 *
 * @author www
 */
public class ZIFalseContainerView extends ZIContainerView {
    private ZIFalseElement element;
    private final static Color FALSE_CONTAINER_COLOR = new Color(255, 239, 175);

    public ZIFalseContainerView(ZIFalseContainer container, ZIController controller) {
        super(container, controller);
        element = container;
    }

    protected void paintComponent(Graphics graphics) {
        if (element.isMarked()) {
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(0.5f));

            g.setColor(FALSE_CONTAINER_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(color);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawLine(0, 0, getWidth(), getHeight());
            g.drawLine(0, getHeight(), getWidth(), 0);
            doLayout();
        } else {
            super.paintComponent(graphics);
        }
    }
}