package zi.document.text.model;

import storage.ISerializable;
import storage.ISerializer;
import zi.ZIController;
import zi.ZIListener;
import zi.document.abstractdocument.model.ZIAbstractDocument;
import zi.document.text.view.TextDocumentView;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.views.ZIElementView;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author BlackboX
 * @author Fedor Tsarev
 * @date 06.03.2007
 *
 */
public class TextDocument extends ZIAbstractDocument implements DocumentListener, ISerializable {

    protected final int fontSize;
    protected Document document;

    public TextDocument(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color, int thumbnailWidth, int thumbnailHeight, int fontSize) {
        super(parent, minLength, maxLength, location, color, thumbnailWidth, thumbnailHeight);
        this.fontSize = fontSize;
        document = new PlainDocument();
        document.addDocumentListener(this);
    }

    @Override
    protected ZIElementView generateView(ZIController controller) {
        return new TextDocumentView(this, fontSize, document, controller);
    }

    public void insertUpdate(DocumentEvent e) {
        System.out.println("TextDocument.insertUpdate");
        changeAllViews();
    }

    public void removeUpdate(DocumentEvent e) {
        System.out.println("TextDocument.removeUpdate");
        changeAllViews();
    }

    public void changedUpdate(DocumentEvent e) {
        System.out.println("TextDocument.changedUpdate");
        changeAllViews();
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

    public int getFontSize() {
        return fontSize;
    }
}
