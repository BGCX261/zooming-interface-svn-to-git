package zi.implementation;

import zi.InformationPaneLayout;
import zi.ZIListener;
import zi.baseElements.Location;
import zi.baseElements.ZIContainer;
import zi.baseElements.ZIContainerAdapter;
import zi.baseElements.ZIItemAdapter;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Olga Komaleva
 * Date: Feb 22, 2007
 */

//singleton
public class InformationPane extends JComponent implements ZIContainer {
    private static InformationPane instance = null;
    private Location relLocation;
    private final ZIContainerAdapter container;

    private InformationPane() {
        super.setLocation(0, 0);
        super.setSize(600, 600);
        relLocation = new Location(this);
        container = new ZIContainerAdapter(this);

        setLayout(new InformationPaneLayout());
    }

    public static InformationPane get() {
        if (instance == null) {
            instance = new InformationPane();
        }
        return instance;
    }

    public void setLocation(int x, int y) {
    }

    public void setLocation(Point p) {
    }

    public void setSize(int width, int height) {
    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.pink);
        g.fillRect(0, 0, getWidth(), getHeight());
        doLayout();
    }

    public Location getRelLocation() {
        return relLocation;
    }

    public void setRelLocation(Location location) {
        relLocation = location;
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
        //TODO: InforamtinPane cannot be activated?
    }

    /*public boolean isFocusable() {
        return true;       
    }*/
}
