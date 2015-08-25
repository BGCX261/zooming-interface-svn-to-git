package classeditor.ui.packagecontainer.actions;

import classeditor.ui.packagecontainer.model.PackageContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Date: 19.04.2007
 * Time: 13:39:06
 *
 * @author Fedor Tsarev
 */
public class CreatePackageAction extends AbstractAction {

    private final PackageContainer packageContainer;

    public CreatePackageAction(PackageContainer packageContainer) {
        this.packageContainer = packageContainer;
        this.putValue(NAME, "cp");
    }

    public void actionPerformed(ActionEvent e) {
        // TODO
    }
}
