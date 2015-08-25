package zi.views;

import zi.ZIListener;
import zi.ZIGlassPane;
import zi.ZIController;
import zi.models.ZIContainer;
import zi.models.ZIItem;

import javax.swing.*;
import java.awt.*;

public class ZIContainerView extends ZIElementView {
    protected Color color;
    private final ZIContainer owner;
    private final ZIController controller;

    public ZIContainerView(ZIContainer container, ZIController controller) {
        owner = container;
        this.controller = controller; 
        this.color = owner.getColor();

    }
  
    protected void paintChildren(Graphics g) {

        int i = getComponentCount() - 1;
        for (; i >= 0; i--) {
            JComponent comp = (JComponent) getComponent(i);
            Point p = comp.getLocation();

            int width = comp.getWidth();
            int height = comp.getHeight();

            if (g.hitClip(p.x, p.y, width, height))
            {
                Graphics cg = g.create(p.x, p.y, width, height);
                comp.paint(cg);
                cg.dispose();
            }
        }
    }


    public void setSize(int x, int y) {
        super.setSize(x, y);
        doLayout();
    }

    public void setSize(Dimension d) {
        super.setSize(d);  
        doLayout();
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(0.5f));

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(color);

        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.drawLine(0, 0, getWidth(), getHeight());
        g.drawLine(0, getHeight(), getWidth(), 0);
    }

    /**
     * Gets container's color.
     *
     * @return current color.
     */

    public Color getColor() {
        return color;
    }

    public ZIItem getOwner() {
        return owner;
    }

    public ZIController getController() {
        return controller;
    }

    @Override
    public boolean canTakeFocus() {
        return false;
    }
}
