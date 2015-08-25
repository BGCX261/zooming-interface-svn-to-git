package zi.models.actions;

import zi.ZIGlassPane;
import zi.models.ZIContainer;
import zi.models.ZIItem;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Author: Olga Komaleva
 * Date: Apr 16, 2007
 */
public class DeleteItemAction extends AbstractAction {
    private final ZIItem item;

    public DeleteItemAction(ZIItem item) {
        this.item = item;
        putValue(NAME, "del");
    }

    public void actionPerformed(ActionEvent e) {
        ZIContainer parent = item.getParent();
        assert (parent != null);
        parent.remove(item);
        ZIGlassPane.get().setSelectedElement(null);
    }
}
