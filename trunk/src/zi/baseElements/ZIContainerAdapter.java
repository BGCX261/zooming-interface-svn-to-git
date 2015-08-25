package zi.baseElements;

import zi.baseElements.ZICompoundItem;
import zi.baseElements.ZIItem;

import javax.swing.*;
import java.util.ArrayList;

public class ZIContainerAdapter {
    private final ArrayList<ZIItem> containees = new ArrayList<ZIItem>();
    private final ArrayList<ZICompoundItem> containers = new ArrayList<ZICompoundItem>();
    private final JComponent owner;

    public ZIContainerAdapter(JComponent owner) {
        this.owner = owner;
    }

    public void add(ZICompoundItem container) {
        owner.add((JComponent)container);
        owner.setComponentZOrder((JComponent)container, 0);
        if (! containers.contains(container)) {
            containers.add(container);
        }
    }

    public void add(ZIItem containee) {
        owner.add((JComponent)containee);
        owner.setComponentZOrder((JComponent)containee, 0);
        if (! containees.contains(containee)) {
            containees.add(containee);
        }
    }

    public void remove(ZIItem containee) {
        owner.remove((JComponent)containee);
        if (containees.contains(containee)) {
            containees.remove(containee);
        }
    }

    public void remove(ZICompoundItem container) {
        owner.remove((JComponent)container);

        if (containers.contains(container)) {
            containers.remove(container);
        }
    }

    public ZIItem[] getContents() {
        ZIItem[] comps = new ZIItem[containers.size() + containees.size()];

        for (int i = 0; i < containers.size(); i++) {
            comps[i] = containers.get(i);
        }

        for (int j = containers.size(); j < comps.length; j++) {
            comps[j] = containees.get(j - containers.size());
        }
        return comps;
    }

    public ZICompoundItem[] getContainers() {
        ZICompoundItem[] comps = new ZICompoundItem[containers.size()];

        for (int i = 0; i < comps.length; i++) {
            comps[i] = containers.get(i);
        }
        return comps;
    }
}
