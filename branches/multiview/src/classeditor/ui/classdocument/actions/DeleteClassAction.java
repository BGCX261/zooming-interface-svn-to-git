package classeditor.ui.classdocument.actions;

import classeditor.ui.classdocument.model.ClassDocument;
import zi.ZIGlassPane;
import zi.models.ZIContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Date: 19.04.2007
 * Time: 13:32:57
 *
 * @author Fedor Tsarev
 */
public class DeleteClassAction extends AbstractAction {

    private final ClassDocument classDocument;

    public DeleteClassAction(ClassDocument classDocument) {
        this.classDocument = classDocument;
        putValue(NAME, "dc");
        putValue(LONG_DESCRIPTION, "Delete class");
//        putValue(SMALL_ICON, new ImageIcon());
    }

    public void actionPerformed(ActionEvent e) {
        ZIContainer parent = classDocument.getParent();
        assert parent != null;
        parent.remove(classDocument);
        ZIGlassPane.get().setSelectedElement(null);
        // TODO
        classDocument.getJavaClass().removeFromProject();
    }
}
