package classeditor.ui.actions;

import classeditor.projectmodel.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

/**
 * Date: 19.04.2007
 * Time: 18:37:11
 *
 * @author Fedor Tsarev
 */
public class SaveProjectAction extends AbstractAction {

    private final Project project;

    public SaveProjectAction(Project project) {
        this.project = project;
        putValue(NAME, "Save project to disk");
    }

    public void actionPerformed(ActionEvent e) {
        try {
            //TODO
            project.writeToDisk("d:\\fedor-tsarev\\test");
        } catch (FileNotFoundException e1) {
            // TODO
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
