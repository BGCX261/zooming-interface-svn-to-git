package zi.models;

import storage.ISerializable;
import storage.ISerializer;
import zi.ZIController;
import zi.ZIListener;
import zi.models.actions.DeleteItemAction;
import zi.views.ZIElementView;
import zi.views.ZIItemView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Olga Komaleva
 * Date: Mar 22, 2007
 */
public class ZIItem implements ISerializable {
    public static final int INFINITE_ZOOMING = -1;
    private RelLocation location;
    private ZIContainer parent;
    private final int minLength;
    private final int maxLength;
    private final Color color;
    protected final ArrayList<Action> actions = new ArrayList<Action>();

    protected final Map<ZIController, ZIElementView> views = new HashMap<ZIController, ZIElementView>();

    public ZIItem(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color) {
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.location = location;
        this.parent = parent;
        this.color = color;
        //actions.add(new DeleteItemAction(this));
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public double getRelX() {
        return location.getRelX();
    }

    public double getRelY() {
        return location.getRelY();
    }

    public double getRelWidth() {
        return location.getRelWidth();
    }

    public double getAbsoluteProportion() {
        return location.getAbsoluteProportion(); 
    }

    public ZIContainer getParent() {
        return parent;
    }

    public boolean setRelBorder(double relX, double relY, double relWidth) {
        return location.setRelBorder(relX, relY, relWidth, (parent == ZIInformationPlane.get()),
                (parent != null) ? parent.getAbsoluteProportion() : 1);
    }

    public void setParent(ZIContainer parent) {
        this.parent = parent;
    }

    public Color getColor() {
        return color;
    }

    public final void addView(ZIController controller) {
        ZIElementView view = generateView(controller);
        view.addZIListener(ZIListener.get(controller));
        views.put(controller, view);
        if (getParent() != null) {
            ZIElementView parent = getParent().getView(controller);
            if (parent != null) {
                parent.add(view);
                parent.setComponentZOrder(view, 0);
                parent.doLayout();
            }
        }
    }

    protected ZIElementView generateView(ZIController controller) {
        return new ZIItemView(this, controller);
    }

    /**
     * Detaches view associated with specified <code>controller</code> from this item.
     *
     * @param controller controller.
     */
    public final void removeView(ZIController controller) {
        if (views.containsKey(controller)) {
            ZIElementView view = views.get(controller);
            view.removeZIListener(ZIListener.get(controller));
            if (getParent() != null) {
                ZIElementView parent = getParent().getView(controller);
                if (parent != null) {
                    parent.remove(view);
                }
            }
            views.remove(controller);
        }
    }

    public final ZIElementView getView(ZIController controller) {
        if (views.containsKey(controller)) {
            return views.get(controller);
        } else {
            return null;
        }
    }

   
    /**
     * Calls back the {@link storage.ISerializer}'s <code>visit()</code> method for corresponding class so that
     * separate concrete visitor classes can then be written that perform some particular operations.
     *
     * @param serializer visitor instance.
     */
    public void accept(ISerializer serializer) {
        serializer.visit(this);
    }

    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    public void resize(ZIElementView view) {
        setRelBorder(view.getLocation(), view.getSize(), (ZIElementView) view.getParent()) ;
        parent.changeAllViews();
    }

    private void setRelBorder(Point point, Dimension size, ZIElementView view) {
        location.setRelBorder(point, size, view);
    }

    protected void changeAllViews() {
        for (ZIElementView view : views.values()) {
            view.doLayout();
            view.repaint();
        }
    }
}
