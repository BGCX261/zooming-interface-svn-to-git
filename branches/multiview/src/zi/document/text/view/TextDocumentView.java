package zi.document.text.view;

import zi.ZIController;
import zi.document.abstractdocument.view.ZIAbstractDocumentView;
import zi.document.text.model.TextDocument;
import zi.models.ZIItem;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 08.04.2007
 * Time: 11:44:10
 */
public class TextDocumentView extends ZIAbstractDocumentView {

    private final JTextArea textArea;
    private final int fontSize;
    
    protected final TextDocument owner;

    public TextDocumentView(TextDocument owner, int fontSize, Document document, ZIController controller) {
        super(owner, controller);
        this.owner = owner;

        this.fontSize = fontSize;
        documentPanel.setLayout(new BorderLayout());
        documentPanel.setBorder(new LineBorder(Color.BLACK));
        textArea = new JTextArea();
        
        textArea.setDocument(document);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 8));
        JScrollPane scrollPane = new JScrollPane(textArea);
        documentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    protected void paintThumbnail(Graphics g) {
        
    }

    protected void zoom() {
        int curWidth = getWidth();
        int newFontSize = Math.min(14, (int) (fontSize * 1.0 * curWidth / owner.getThumbnailWidth()));
        if (newFontSize <= 0) {
            newFontSize = 1;
        }
//        int newFontSize = (int) (fontSize * 1.0 * curWidth / owner.getThumbnailWidth());
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, newFontSize));
    }

    public ZIItem getOwner() {
        return owner;
    }
}
