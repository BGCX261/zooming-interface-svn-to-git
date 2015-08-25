package classeditor.ui.packagecontainer.actions;

import classeditor.ui.packagecontainer.model.PackageContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Date: 19.04.2007
 * Time: 13:38:28
 *
 * @author Fedor Tsarev
 */
public class CreateClassAction extends AbstractAction {

    private final PackageContainer packageDocument;

    public CreateClassAction(PackageContainer packageContainer) {
        this.packageDocument = packageContainer;
        this.putValue(NAME, "cc");
//        this.putValue(SMALL_ICON, IconGenerator.get().getCreateClassIcon());
    }

    public void actionPerformed(ActionEvent e) {
        // TODO
    }
}
