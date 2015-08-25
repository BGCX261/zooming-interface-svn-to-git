package zi.models.actions;

import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.AbstractAction;

import zi.ZIController;
import zi.ZIGlassPane;
import zi.views.ZIElementView;

/**
 * @author BlackboX
 */
@SuppressWarnings("serial")
public class MaximizingShortcutAction extends AbstractAction {
    
    private final ZIElementView ziev;
    private final ZIController ctrlr;
    
    public MaximizingShortcutAction(ZIElementView v, ZIController c) {
        ziev = v;
        ctrlr = c;
        ZIGlassPane.get().addShortcut(this);
        putValue(NAME, "" + v.hashCode());
    }
    
    public void actionPerformed(ActionEvent e) {
        //FIXME: if corresponding ZIItem will be deleted, everything will fail
        ctrlr.zoom(ziev);
    }
    
}
