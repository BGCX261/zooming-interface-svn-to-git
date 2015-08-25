package zi.implementation;

import storage.ISerializable;
import storage.ISerializer;
import zi.ZILayout;
import zi.ZIListener;
import zi.baseElements.*;

import javax.swing.*;
import java.awt.*;

public class ZIContainerImpl extends JComponent implements ZICompoundItem, ISerializable {
    protected Color color;
    private final ZIItemAdapter containee;
    private final ZIContainerAdapter container;

    public ZIContainerImpl(ZIContainer parent, int minLength, int maxLength, Color color, RelLocation location) {
        containee = new ZIItemAdapter(parent, minLength, maxLength, location);
        container = new ZIContainerAdapter(this);
        this.color = color;
        setLayout(new ZILayout());
    }

    protected void paintComponent(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(color);

        g.drawLine(0, 0, 0, getHeight());
        g.drawLine(0, 0, getWidth(), 0);

        g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1);
        g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight() - 1);

        g.drawLine(0, 0, getWidth(), getHeight());
        g.drawLine(0, getHeight(), getWidth(), 0);

        doLayout();
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
        //TODO: Container cannot be activated?
    }

    /**
     * Calls back the {@link storage.ISerializer}'s visit() method for corresponding class so that
     * separate concrete visitor classes can then be written that perform some particular operations.
     *
     * @param serializer visitor instance.
     */
    public void accept(ISerializer serializer) {
        serializer.visit(this);
    }

    /**
     * Gets container's color.
     *
     * @return current color.
     */
    public Color getColor() {
        return color;
    }
}
