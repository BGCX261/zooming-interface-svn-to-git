package storage;

import zi.implementation.ZIContainerImpl;
import zi.implementation.document.ImageDocument;
import zi.implementation.document.TextDocument;

/**
 * Simulates double dispatch in a conventional
 * single-dispatch object-oriented language such as Java.
 * <p/>
 * Author: www
 */
public interface ISerializer {
    void visit(ImageDocument image);

    void visit(ZIContainerImpl container);

    void visit(TextDocument document);
}