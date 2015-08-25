package storage.ui;

import zi.ZIController;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.models.ZIItem;
import zi.views.ZIElementView;

import java.awt.*;
import java.util.List;


/**
 * Item, whose state could be not identical to saved one.
 *
 * @author www
 */
public class ZIFalseItem extends ZIItem implements ZIFalseElement {
    private boolean markedAsFalse = false;

    public ZIFalseItem(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color) {
        super(parent, minLength, maxLength, location, color);
    }

    /**
     * Marks element as correct or not identical to saved one.
     *
     * @param isFalse <code>true</code>, if element's final state doesn't matched achieved one;
     *                <code>false</code> otherwise.
     */
    public void mark(boolean isFalse) {
        markedAsFalse = isFalse;
        if (!isFalse) {
            for (ZIElementView v : views.values()) {
                v.setToolTipText(null);
            }
        }
        for (ZIElementView v : views.values()) {
            v.repaint();
        }
    }

    /**
     * Whether element's final state doesn't matched achieved one.
     *
     * @return <code>true</code>, if element's final state doesn't matched achieved one;
     *         <code>false</code> otherwise.
     */
    public boolean isMarked() {
        return markedAsFalse;
    }

    /**
     * Sets found problems list.
     *
     * @param text list containing found problems description.
     */
    public void setText(List<String> text) {
        StringBuffer sb = new StringBuffer();
        for (String s : text) {
            sb.append(s);
            sb.append("<br>");
        }
        for (ZIElementView v : views.values()) {
            v.setToolTipText("<html>" + sb.toString() + "</html>");
        }
    }

    /**
     * Returns the canonical runtime class of an object.
     *
     * @return The <code>java.lang.Class</code> object that represents
     *         the canonical runtime class of the object.
     */
    public Class getCanonicalClass() {
        return ZIItem.class;
    }

    @Override
    protected ZIElementView generateView(ZIController controller) {
        return new ZIFalseItemView(this, controller);
    }
}