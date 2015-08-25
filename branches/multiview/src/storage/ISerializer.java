package storage;

import zi.document.image.model.ImageDocument;
import zi.document.text.model.TextDocument;
import zi.models.ZIItem;
import classeditor.ui.packagecontainer.model.PackageContainer;
import classeditor.ui.classdocument.model.ClassDocument;

/**
 * Simulates double dispatch in a conventional
 * single-dispatch object-oriented language such as Java.
 * <p/>
 * Author: www
 */
public interface ISerializer {
    void visit(ZIItem item);

    void visit(ImageDocument image);

    void visit(TextDocument text);

    void visit(PackageContainer container);

    void visit(ClassDocument doc);
}