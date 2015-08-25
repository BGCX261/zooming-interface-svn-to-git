package classeditor.ui.classdocument.model;

import classeditor.projectmodel.JavaClass;
import classeditor.ui.classdocument.actions.DeleteClassAction;
import classeditor.ui.classdocument.view.ClassDocumentView;
import storage.ISerializable;
import storage.ISerializer;
import zi.ZIController;
import zi.document.text.model.TextDocument;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.views.ZIElementView;

import java.awt.*;

/**
 * User: Fedor Tsarev
 * Date: 14.04.2007
 */
public class ClassDocument extends TextDocument implements ISerializable {

    protected final JavaClass javaClass;

    public JavaClass getJavaClass() {
        return javaClass;
    }

    public ClassDocument(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color, int thumbnailWidth, int thumbnailHeight, int fontSize, JavaClass javaClass) {
        super(parent, minLength, maxLength, location, color, thumbnailWidth, thumbnailHeight, fontSize);
        this.javaClass = javaClass;
        this.document = javaClass.getDocument();
        actions.clear();
        actions.add(new DeleteClassAction(this));
    }

    @Override
    protected ZIElementView generateView(ZIController controller) {
        return new ClassDocumentView(this, fontSize, document, controller);
    }

    /**
     * Calls back the {@link storage.ISerializer}'s <code>visit()</code> method for corresponding class so that
     * separate concrete visitor classes can then be written that perform some particular operations.
     *
     * @param serializer visitor instance.
     */
    public void accept(ISerializer serializer) {
        serializer.visit(this);
    }

}
