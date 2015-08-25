package zi;

import zi.models.*;
import zi.views.ZIInformationPlaneView;
import zi.views.ZIContainerView;
import zi.views.ZIElementView;

import java.awt.*;

import javax.swing.JComponent;

public class ZIController {
    private static final double ZOOM = 0.1;
    private static final int INDENT = 10;

    private ZIInformationPlaneView infPlaneView;
    private Location cc;
    private final Container panel;

    public ZIController(Container panel) {
        this.panel = panel;
    }

    public void init() {
        infPlaneView = (ZIInformationPlaneView) ZIInformationPlane.get().getView(this);
        cc = new Location(infPlaneView.getRelLocation()) ;
        panel.add(infPlaneView);
    }

    public void moveCenter(int x, int y) {
        cc.setX(cc.getX() + x);
        cc.setY(cc.getY() + y);
        repaint();
    }

    private void repaint() {
        infPlaneView.setRelLocation(cc);
        infPlaneView.invalidate();
        panel.repaint();
    }

    public void zoom(ZIElementView c) {

        c.getParent().setComponentZOrder(c, 0);
        ZIItem container = c.getOwner();
        if (container.getAbsoluteProportion()
                > (((double)panel.getWidth()) / panel.getHeight())) {

            cc.setHeight(((double)(panel.getWidth() - 2*INDENT)) / container.getAbsoluteProportion());
            cc.setWidth(panel.getWidth() - 2*INDENT);
            cc.setX(INDENT);
            cc.setY(((panel.getHeight() - cc.getHeight()))/2);
        } else {
            cc.setWidth(((double)(panel.getHeight() - 2*INDENT)) * container.getAbsoluteProportion());
            cc.setHeight(panel.getHeight() - 2*INDENT);
            cc.setX((panel.getWidth() - cc.getWidth())/2);
            cc.setY(INDENT);
        }

        ZIContainerView cView = (ZIContainerView) c.getParent();

        cc.setWidth((cc.getWidth() / container.getRelWidth())); //todo undo

        double x = container.getRelX();
        double y = container.getRelY();
        container = cView.getOwner();
        cc.setHeight(cc.getWidth() / container.getAbsoluteProportion());
        cc.setX(cc.getX() - (x * cc.getWidth()));
        cc.setY(cc.getY() - (y * cc.getHeight()));

        toInformationPlane(cView);
        repaint();
    }

    private void toInformationPlane(ZIContainerView currentContainer) {
        ZIContainer container = (ZIContainer) currentContainer.getOwner();

        while (container != ZIInformationPlane.get()) {
            cc.setWidth(cc.getWidth() / container.getRelWidth());

            double x = container.getRelX();
            double y = container.getRelY();
            container = container.getParent();
            cc.setHeight(cc.getWidth() / container.getAbsoluteProportion());
            cc.setX(cc.getX() - (x * cc.getWidth()));
            cc.setY(cc.getY() - (y * cc.getHeight()));
        }
    }

    public void zoom(int i) {
        double o2 = (cc.getWidth() - panel.getWidth() * 0.5 + cc.getX()) / (panel.getWidth() * 0.5 - cc.getX());
        double o3 = (cc.getHeight() - panel.getHeight() * 0.5 + cc.getY()) / (panel.getHeight() * 0.5 - cc.getY());

        cc.setWidth(cc.getWidth() * (1 - i * ZOOM));
        cc.setHeight(cc.getHeight() * (1 - i * ZOOM));
        cc.setX(panel.getWidth() * 0.5 - cc.getWidth() / (1 + o2));
        cc.setY(panel.getHeight() * 0.5 - cc.getHeight() / (1 + o3));
        repaint();
    }
    
//added for focus management 
    //TODO: move to ZIListener?
    
    private static ZIFocusable focused = null;
    
    public static ZIFocusable getFocused() {
        return focused;
    }
    
    public void setFocus(ZIFocusable newFocused) {
        if (newFocused != null) {
            newFocused.gotZIFocus();
        }
        if (focused != null) {
            focused.lostZIFocus();
        }
        focused = newFocused;
    }

    public void setFocusToIP() {
        setFocus(infPlaneView);
        focused = null;
    }

    public boolean isFocused(ZIFocusable view) {
        return view == focused;
    }
    
    public void borrowFocus() {
        assert (focused != null);
        focused.giveUpMouse();
        //infPlaneView.requestFocus();
    }

    public void giveBackFocus() {
        assert (focused != null);
        focused.takeBackMouse();
        //((JComponent)focused).requestFocus();
    }
    
}
