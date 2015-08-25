package zi.views;

import java.awt.*;

import zi.ZIFocusable;
import zi.ZIListener;
import zi.ZIController;
import zi.models.ZIItem;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * Author: Olga Komaleva
 * Date: Mar 18, 2007
 */
public abstract class ZIElementView extends JComponent implements ZIFocusable {
    public abstract ZIItem getOwner();
    public abstract ZIController getController();

    public ZIElementView() {
        setFocusable(false);
    }
    
    public void addZIListener(ZIListener listener) {
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
        addKeyListener(listener);
    }

    /**
     * Removes the specified listener so that it no longer
     * receives mouse events from this component. This method performs
     * no function, nor does it throw an exception, if the listener
     * specified by the argument was not previously added to this component.
     * If listener <code>listener</code> is <code>null</code>,
     * no exception is thrown and no action is performed.
     *
     * @param listener {@link zi.ZIListener}'s instance.
     */
    public void removeZIListener(ZIListener listener) {
        removeMouseListener(listener);
        removeMouseMotionListener(listener);
        removeMouseWheelListener(listener);
        removeKeyListener(listener);
    }

    public void gotZIFocus() {
        setFocusable(true);
        requestFocus();
    }

    public void lostZIFocus() {
        setFocusable(false);
    }

    public boolean canTakeFocus() {
        return true;
    }
    
    private static final long MOUSE_EVENTS = AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK;

    public void giveUpMouse() {
        disableEvents(MOUSE_EVENTS);
    }

    public void takeBackMouse() {
        enableEvents(MOUSE_EVENTS);
    }
}
