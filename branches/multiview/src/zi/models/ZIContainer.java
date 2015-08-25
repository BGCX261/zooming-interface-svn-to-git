package zi.models;

import zi.ZIContainerLayout;
import zi.ZIController;
import zi.models.actions.CreateContainerAction;
import zi.models.actions.DeleteItemAction;
import zi.views.ZIContainerView;
import zi.views.ZIElementView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Author: Olga Komaleva
 * Date: Mar 18, 2007
 */
public class ZIContainer extends ZIItem {
    private final ArrayList<ZIContainer> containers = new ArrayList<ZIContainer>();
    private final ArrayList<ZIItem> items = new ArrayList<ZIItem>();
    protected final ArrayList<Action> containerActions = new ArrayList<Action>();

    public ZIContainer(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color) {
        super(parent, minLength, maxLength, location, color);
        //containerActions.add(new CreateContainerAction(this));
        //containerActions.add(new DeleteItemAction(this));
    }

    public void add(ZIItem item) {
        item.setParent(this);
        if ((item.getClass() == ZIContainer.class) && (!containers.contains(item))) {//todo
            containers.add((ZIContainer) item);
            addToAllViews(item);
            return;
        }

        if (!items.contains(item)) {
            items.add(item);
            addToAllViews(item);
        }
    }

    private void addToAllViews(ZIItem item) {
        for (ZIController controller : views.keySet()) {
            ZIElementView parent = views.get(controller);
            ZIElementView view = item.getView(controller);
            if (view != null) {
                parent.add(view);
                parent.setComponentZOrder(view, 0);
                parent.doLayout();
            } else {
                item.addView(controller);
            }
        }
    }

    private void removeFromAllViews(ZIItem item) {
        for (ZIController controller : views.keySet()) {
            ZIElementView parent = views.get(controller);
            ZIElementView view = item.getView(controller);
            if (view != null) {
                parent.remove(view);
                parent.doLayout();
                parent.repaint();
            }
        }
    }

    public void remove(ZIItem item) {
        if (items.contains(item)) {
            items.remove(item);
            removeFromAllViews(item);
            return;
        }

        if (containers.contains(item)) {
            containers.remove(item);
            removeFromAllViews(item);
        }
    }

    @Override
    protected ZIElementView generateView(ZIController controller) {
        ZIElementView view = new ZIContainerView(this, controller);
        view.setLayout(new ZIContainerLayout(controller));
        return view;
    }

    public ZIContainer[] getContainers() {
        ZIContainer[] comps = new ZIContainer[containers.size()];

        for (int i = 0; i < comps.length; i++) {
            comps[i] = containers.get(i);
        }
        return comps;
    }

    /**
     * Gets array of {@link zi.models.ZIItem}'s. Method doesn't return
     * array of items, as it may seem, but array of both
     * {@link zi.models.ZIContainer}'s and {@link zi.models.ZIItem}'s.
     *
     * @return array of all items.
     */
    public ZIItem[] getItems() {
        ZIItem[] comps = new ZIItem[containers.size() + items.size()];

        for (int i = 0; i < containers.size(); i++) {
            comps[i] = containers.get(i);
        }

        for (int j = containers.size(); j < comps.length; j++) {
            comps[j] = items.get(j - containers.size());
        }
        return comps;
    }

    public Action[] getActions() {
        return containerActions.toArray(new Action[containerActions.size()]);
    }
}
