package zi;

import zi.models.Location;
import zi.models.ZIContainer;
import zi.models.ZIInformationPlane;
import zi.models.ZIItem;
import zi.models.actions.MaximizingShortcutAction;
import zi.views.ZIContainerView;
import zi.views.ZIElementView;
import zi.views.ZIInformationPlaneView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * Author: Olga Komaleva
 * Date: Feb 25, 2007
 */
public class ZIListener implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {


    private ZIController controller;
    private static HashMap<ZIController, ZIListener> listeners = new HashMap<ZIController, ZIListener>();

    //TODO: move to another file?
    public static class ZIModel {
        private int lastX;
        private int lastY;
        private ModelState modelState;

        private void setModelState(ModelState ss) {
            modelState = ss;
        }

        public ZIModel() {
            reset();
        }

        private void reset() {
            modelState = new FocusOnIP(null);
        }

        private final static class Helper {
            static boolean isLMBPressed(InputEvent ie) {
                return (ie.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) != 0;
            }
            static boolean isRMBPressed(InputEvent ie) {
                return (ie.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0;
            }
            static boolean isAltPressed(InputEvent ie) {
                return (ie.getModifiersEx() & InputEvent.ALT_DOWN_MASK) != 0;
            }
            static boolean isCtrlPressed(InputEvent ie) {
                return (ie.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
            }
            static boolean isShiftPressed(InputEvent ie) {
                return (ie.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
            }
        }

        private abstract class ModelState {
            private final int BORDER_WIDTH = 3;
            protected final int MIN_LENGTH = 20;

            public ModelState() {
                 ZIGlassPane.get().setSelectedElement(null);
            }

            public void mousePressed(ZIController ctrlr, MouseEvent me) {}
            

            public void mouseReleased(ZIController ctrlr, MouseEvent me) {}

            public void mouseMoved(ZIController ctrlr, int byX, int byY, MouseEvent me) {
                if ((((ZIElementView)me.getSource()).getOwner() != ZIInformationPlane.get())
                        && (((ZIElementView)me.getSource()).getWidth() > MIN_LENGTH)
                        && (((ZIElementView)me.getSource()).getHeight() > MIN_LENGTH)) {
                    setCursor(me);
                } else {
                    ZIGlassPane.get().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }

            public void mouseDragged(ZIController ctrlr, int byX, int byY, MouseEvent me) {
                if (Helper.isLMBPressed(me)) {
                    if ((((ZIElementView) me.getSource()).getOwner() != ZIInformationPlane.get()) && ((ZIGlassPane.get().getCursor().getType() != Cursor.DEFAULT_CURSOR))) {
                        setModelState(new ElementResizing(getFlag(me), (ZIElementView) me.getSource(), this));
                    } else {
                        setModelState(new CoverRectangle((ZIElementView) me.getSource(), this));
                    }
                }
            }

            public void mouseEntered(ZIController ctrlr, MouseEvent me) {}

            public void mouseExited(ZIController ctrlr, MouseEvent me) {}

            public void mouseWheel(ZIController ctrlr, int count) {}

            public void leftMBClicked(ZIController ctrlr, MouseEvent me) {
                 if (!Helper.isAltPressed(me) && !Helper.isCtrlPressed(me)) {
                    ZIElementView source = (ZIElementView) me.getSource();
                    ZIItem item = source.getOwner();
                    if (item != ZIInformationPlane.get()) {
                        item.getParent().remove(item);
                        item.setParent(null);
                        ZIGlassPane.get().setAttached(source, -me.getX(), -me.getY());
                        setModelState(new ElementAttached(null, this));
                    }
                }
            }
            public void middleMBClicked(ZIController ctrlr, MouseEvent me) {}
            public void rightMBClicked(ZIController ctrlr, MouseEvent me) {}

            public void keyReleased(ZIController ctrlr, KeyEvent ke) {}
            public void keyPressed(ZIController ctrlr, KeyEvent ke) {}

            private int getFlag(MouseEvent me) {
                int i;
                i = (me.getX() <= BORDER_WIDTH) ? 1 : 0;
                i <<= 1;
                i += (me.getY() <= BORDER_WIDTH) ? 1 : 0;
                i <<= 1;
                i += (((ZIElementView)me.getSource()).getWidth() - me.getX() <= BORDER_WIDTH) ? 1 : 0;
                i <<= 1;
                i += (((ZIElementView)me.getSource()).getHeight()  - me.getY() <= BORDER_WIDTH) ? 1 : 0;
                return i;
            }

            private void setCursor(MouseEvent me) {
                switch (getFlag(me))
                {
                    case 8:
                    case 2:
                        ZIGlassPane.get().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                        break;
                    case 1:
                    case 4:
                        ZIGlassPane.get().setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                        break;
                    case 12:
                    case 3:
                        ZIGlassPane.get().setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                        break;
                    case 9:
                    case 6:
                        ZIGlassPane.get().setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                        break;
                    case 0:
                        ZIGlassPane.get().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        break;
                    default:
                        assert false;
                }
            }
        }

        private class ElementResizing extends ModelState {

            private ZIElementView resizedView;
            private int resizedFlag;
            private ModelState oldState;

            public ElementResizing(int resizedFlag, ZIElementView resizedView, ModelState oldState) {
                this.resizedFlag = resizedFlag;
                this.resizedView = resizedView;
                this.oldState = oldState;
            }

            public void mouseReleased(ZIController ctrlr, MouseEvent me) {
                setModelState(oldState);//todo delete null
            }

            public void mouseDragged(ZIController ctrlr, int byX, int byY, MouseEvent me) {
                if (Helper.isLMBPressed(me))
                {
                    resizeZIElement(me);
                }
            }

            private void resizeZIElement(MouseEvent me) {
                if (resizedView == me.getSource()) {

                    Point point = SwingUtilities.convertPoint(resizedView, me.getPoint(), resizedView.getParent());
                    if ((point.x < 0) || (point.y < 0)
                            || (point.x > resizedView.getParent().getWidth()) || (point.y > resizedView.getParent().getHeight())) {
                        return;
                    }

                    int width = resizedView.getWidth();
                    int height = resizedView.getHeight();
                    int x = resizedView.getX();
                    int y = resizedView.getY();

                    if (resizedFlag % 2 == 1) {
                        y = resizedView.getY();
                        height = point.y - y;
                        if (height <= MIN_LENGTH) {
                            y = resizedView.getY();
                            height = resizedView.getHeight();
                        }
                    }

                    if ((resizedFlag >> 1) % 2 == 1) {
                        x = resizedView.getX();
                        width = point.x - x;
                        if (width <= MIN_LENGTH) {
                            x = resizedView.getX();
                            width = resizedView.getWidth();
                        }
                    }

                    if ((resizedFlag >> 2) % 2 == 1) {
                        y = point.y;
                        height = resizedView.getY() - y + resizedView.getHeight();
                        if (height <= MIN_LENGTH) {
                            y = resizedView.getY();
                            height = resizedView.getHeight();
                        }
                    }

                    if ((resizedFlag >> 3) % 2 == 1) {
                        x = point.x;
                        width = resizedView.getX() - x  + resizedView.getWidth();
                        if (width <= MIN_LENGTH) {
                            x = resizedView.getX();
                            width = resizedView.getWidth();
                        }
                    }

                    resizedView.setSize(width, height);
                    resizedView.setLocation(x, y);
                    resizedView.getOwner().resize(resizedView);
                }
            }
        }

        private class CoverRectangle extends ModelState {
            private ZIElementView ownerView;
            private ModelState oldState;

            public CoverRectangle(ZIElementView ownerView, ModelState oldState) {
                this.ownerView = ownerView;
                this.oldState = oldState;
            }

            public void mouseReleased(ZIController ctrlr, MouseEvent me) {
                Point point = SwingUtilities.convertPoint((Component) me.getSource(), me.getPoint(), ownerView);
                ZIGlassPane.get().setCoverRectangleActions(ownerView, point);
                setModelState(new ElementAttached(null, oldState));
            }

            public void mouseDragged(ZIController ctrlr, int byX, int byY, MouseEvent me) {
                if (Helper.isLMBPressed(me))
                {
                    Point point = SwingUtilities.convertPoint((JComponent) me.getSource(),me.getPoint(), ownerView);
                    double x = point.getX();
                    double y = point.getY();

                    x = (x < 0) ? 0 : x;
                    y = (y < 0) ? 0 : y;
                    x = (x > ownerView.getWidth()) ? ownerView.getWidth() : x;
                    y = (y > ownerView.getHeight()) ? ownerView.getHeight() : y;

                    point.setLocation(x, y);
                    point = SwingUtilities.convertPoint(ownerView, point , ZIGlassPane.get());
                    ZIGlassPane.get().setCoverRectangle(point);
                }
            }
        }

        private class FocusOnIP extends ModelState {

            public FocusOnIP(ZIElementView view) {
                ZIGlassPane.get().setSelectedElement(view);
                ZIGlassPane.get().repaint();
            }

            @Override
            public void mouseWheel(ZIController ctrlr, int count) {
                if (count < 0) {
                    ctrlr.zoom(1);
                } else {
                    ctrlr.zoom(-1);
                }
            }

            @Override
            public void mouseDragged(ZIController ctrlr, int byX, int byY, MouseEvent me) {
                super.mouseDragged(ctrlr, byX, byY, me);

                ZIGlassPane.get().setSelectedElement(null);
                ZIGlassPane.get().repaint();
                if (Helper.isRMBPressed(me)) {
                    ctrlr.moveCenter(byX, byY);
                }
            }

            @Override
            public void rightMBClicked(ZIController ctrlr, MouseEvent me) {
                ZIElementView view = (ZIElementView) me.getSource();
                if (view.getOwner() != ZIInformationPlane.get()) {
                    ctrlr.zoom(view);
                }
            }

            @Override
            public void leftMBClicked(ZIController ctrlr, MouseEvent me) {
                super.leftMBClicked(ctrlr, me);
                if (!Helper.isAltPressed(me) && Helper.isCtrlPressed(me)) {
                    ZIElementView view = (ZIElementView) me.getSource();
                    if (view.getOwner() == ZIInformationPlane.get()) {
                        ctrlr.setFocusToIP();
                        setModelState(new FocusOnIP((ZIElementView) me.getSource()));
                    } else {
                        if (!ctrlr.isFocused(view) && view.canTakeFocus()) {
                            ZIGlassPane.get().setSelectedElement(null);
                            setModelState(new FocusOnChild());
                            ctrlr.setFocus(view);
                        }
                    }
                }

                if (!Helper.isCtrlPressed(me) && Helper.isAltPressed(me)) {
                    //TODO: create a link here
                    ZIElementView source = (ZIElementView) me.getSource();
                    ZIItem item = source.getOwner();
                    if (item != ZIInformationPlane.get()) {
                        new MaximizingShortcutAction(source, ctrlr);
                    }
                }
            }

            @Override
            public void mouseEntered(ZIController ctrlr, MouseEvent me) {
                ZIGlassPane.get().setSelectedElement((ZIElementView) me.getSource());
                ZIGlassPane.get().repaint();
            }
        }

        private class ElementAttached extends FocusOnIP {
            private ModelState oldState;

            public ElementAttached(ZIElementView view, ModelState oldState) {
                super(view);
                System.out.println("ZIListener$ZIModel$ElementAttached.ElementAttached");
                System.out.println(oldState);
                this.oldState = oldState;
            }

            @Override
            public void mouseMoved(ZIController ctrlr, int byX, int byY, MouseEvent me) {
                ZIGlassPane.get().repaint();
            }

            @Override
            public void leftMBClicked(ZIController ctrlr, MouseEvent me) {
                //TODO: use Shift modifier for attaching several elements

                if (!Helper.isCtrlPressed(me)) {
                    boolean thereIsEnoughtPlace = true;

                    ZIElementView[] attacheds = ZIGlassPane.get().getAttachedElements();//.getAttached();
                    Point[] points = ZIGlassPane.get().getAttDs();


                    ZIElementView target = (ZIElementView) me.getSource();
                    Point point = me.getPoint();

                    if (!(target instanceof ZIContainerView)) {
                        point = SwingUtilities.convertPoint(target, point, target.getParent());
                        target = (ZIElementView) target.getParent();
                    }
                    ZIContainer targetOwner = (ZIContainer) target.getOwner();

                    for (int i = 0; i < attacheds.length; i++) {

                        points[i].translate((int) point.getX(), (int) point.getY());

                        ZIItem attOwner = attacheds[i].getOwner();


                        double x, y, w;
                        if (ZIInformationPlane.get() == targetOwner) {
                            Location location = ((ZIInformationPlaneView) target).getRelLocation();
                            x = (points[i].getX() - location.getX()) / location.getWidth();
                            y = (points[i].getY() - location.getY()) / location.getHeight();
                            w = attacheds[i].getWidth() / location.getWidth();

                        } else {
                            x = points[i].getX() / target.getWidth();
                            y = points[i].getY() / target.getHeight();
                            w = ((double) attacheds[i].getWidth()) / target.getWidth();
                        }
                        if (attOwner.setRelBorder(x, y, w)) {

                        } else {
                            //attOwner.setParent(null);
                            thereIsEnoughtPlace = false;
                            break;
                        }
                    }

                    if (thereIsEnoughtPlace) {
                        System.out.println("ZIListener$ZIModel$ElementAttached.leftMBClicked");
                        ZIGlassPane.get().setAttached(null, 0, 0);

                        for (ZIElementView attached : attacheds) {
                             targetOwner.add(attached.getOwner());
                            //ZIGlassPane.get().setAttached(null, 0, 0);
                            //ctrlr.setFocusToIP();
                            //setModelState(oldState);
                        }

                        setModelState(oldState);
                    }
                }
            }
        }

        private class FocusBorrowed extends FocusOnIP {
            public FocusBorrowed(ZIElementView view) {
                super(view);
            }

            @Override
            public void mouseEntered(ZIController ctrlr, MouseEvent me) {
                super.mouseEntered(ctrlr, me);
                ZIElementView view = (ZIElementView) me.getSource();
                if (ctrlr.isFocused(view)) {
                    ZIGlassPane.get().setSelectedElement(null);
                    ctrlr.giveBackFocus();
                    setModelState(new FocusOnChild());
                }
            }
        }

        private class FocusOnChild extends ModelState {

            @Override
            public void mouseMoved(ZIController ctrlr, int byX, int byY, MouseEvent me) {
                if (!((Component)ZIController.getFocused()).contains(me.getPoint())) {//why we doesn't test equitatation focused view and source event instead of point test
                    ctrlr.borrowFocus();
                    setModelState(new FocusBorrowed((ZIElementView) me.getSource()));
                }
            }
            
            @Override
            public void leftMBClicked(ZIController ctrlr, MouseEvent me) {
                if (!Helper.isAltPressed(me) && Helper.isCtrlPressed(me)) {
                    assert(ctrlr.isFocused((ZIElementView) me.getSource()));
                    ctrlr.setFocusToIP();
                    setModelState(new FocusOnIP((ZIElementView) me.getSource()));
                }
            }
        }

        public int getLastX() {
            return lastX;
        }

        public int getLastY() {
            return lastY;
        }

    }

    public static final ZIModel zim = new ZIModel();

    public void mouseWheelMoved(MouseWheelEvent e) {
        zim.modelState.mouseWheel(controller, e.getWheelRotation());
    }

    public void mouseClicked(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                zim.modelState.leftMBClicked(controller, e);
                break;
            case MouseEvent.BUTTON2:
                zim.modelState.middleMBClicked(controller, e);
                break;
            case MouseEvent.BUTTON3:
                zim.modelState.rightMBClicked(controller, e);
                break;
        }
    }

    public void mousePressed(MouseEvent e) {
        zim.modelState.mousePressed(controller, e);
    }

    public void mouseReleased(MouseEvent e) {
        zim.modelState.mouseReleased(controller, e);
    }

    public void mouseEntered(MouseEvent e) {
        zim.modelState.mouseEntered(controller, e);
        Point p = SwingUtilities.convertPoint((JComponent) e.getSource(), e.getPoint(), ZIGlassPane.get());
        zim.lastX = p.x;
        zim.lastY = p.y;
    }

    public void mouseExited(MouseEvent e) {
        zim.modelState.mouseExited(controller, e);
    }

    public void mouseDragged(MouseEvent e) {
        Point p = SwingUtilities.convertPoint((JComponent) e.getSource(), e.getPoint(), ZIGlassPane.get());
        zim.modelState.mouseDragged(controller, p.x - zim.lastX, p.y - zim.lastY, e);
        zim.lastX = p.x;
        zim.lastY = p.y;
    }

    public void mouseMoved(MouseEvent e) {
        Point p = SwingUtilities.convertPoint((JComponent) e.getSource(), e.getPoint(), ZIGlassPane.get());
        zim.modelState.mouseMoved(controller, p.x - zim.lastX, p.y - zim.lastY, e);
        zim.lastX = p.x;
        zim.lastY = p.y;
    }

    public void keyPressed(KeyEvent ke) {
        zim.modelState.keyPressed(controller, ke);
    }

    public void keyReleased(KeyEvent ke) {
        zim.modelState.keyReleased(controller, ke);
    }

    public void keyTyped(KeyEvent e) {
    }

    public static ZIListener get(ZIController controller) {
        if (listeners.containsKey(controller)) {
            return listeners.get(controller);
        } else {
            ZIListener l = new ZIListener(controller);
            listeners.put(controller, l);
            return l;
        }
    }

    private ZIListener(ZIController controller) {
        this.controller = controller;
    }

    public static void reset() {
        zim.reset();
        ZIGlassPane.get().setAttached(null, -1, -1);
        if (listeners.size() != 0) {
            listeners.keySet().iterator().next().setFocusToIP();
        }
        listeners.clear();
    }
}
