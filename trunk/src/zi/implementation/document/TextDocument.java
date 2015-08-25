package zi.implementation.document;

import storage.ISerializer;
import zi.baseElements.RelLocation;
import zi.baseElements.ZIContainer;
import zi.implementation.ZIAbstractDocument;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author BlackboX
 * @author Fedor Tsarev
 * @date 06.03.2007
 *
 */
public class TextDocument extends ZIAbstractDocument {

    private final JTextArea textArea;

    private final int fontSize;

    public TextDocument(ZIContainer parent, int minWidth, int maxWidth, RelLocation location, int thumbnailWidth, int thumbnailHeight, int fontSize) {
        super(parent, minWidth, maxWidth, location, thumbnailWidth, thumbnailHeight);
        this.fontSize = fontSize;
        documentPanel.setLayout(new BorderLayout());
        documentPanel.setBorder(new LineBorder(Color.BLACK));
        textArea = new JTextArea();
        documentPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    protected void zoom() {
        int curHeight = getHeight();
        int curWidth = getWidth();
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, (int) (fontSize * 1.0 * curWidth / minWidth)));
    }

    /**
     * Calls back the {@link storage.ISerializer}'s visit() method for corresponding class so that
     * separate concrete visitor classes can then be written that perform some particular operations.
     *
     * @param serializer visitor instance.
     */
    public void accept(ISerializer serializer) {
        serializer.visit(this);
    }
}
