package classeditor.ui.packagecontainer.actions;

import classeditor.ui.packagecontainer.model.PackageContainer;
import zi.ZIGlassPane;
import zi.models.ZIContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Date: 19.04.2007
 * Time: 13:39:30
 *
 * @author Fedor Tsarev
 */
public class DeletePackageAction extends AbstractAction {

    private final PackageContainer packageContainer;

    public DeletePackageAction(PackageContainer packageContainer) {
        this.packageContainer = packageContainer;
        this.putValue(NAME, "dp");
    }

    public void actionPerformed(ActionEvent e) {
        ZIContainer parent = packageContainer.getParent();
        assert parent != null;
        parent.remove(packageContainer);
        ZIGlassPane.get().setSelectedElement(null);
        // TODO
        packageContainer.getJavaPackage().removeFromProject();
    }
}
