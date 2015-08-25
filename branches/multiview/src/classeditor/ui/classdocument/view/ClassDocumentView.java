package classeditor.ui.classdocument.view;

import classeditor.ui.classdocument.model.ClassDocument;
import zi.ZIController;
import zi.document.text.view.TextDocumentView;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 14.04.2007
 * Time: 21:10:12
 */
public class ClassDocumentView extends TextDocumentView {

    protected ClassDocument owner;

    private JLabel label;
    private JLabel javaClassLabel;

    public ClassDocumentView(ClassDocument owner, int fontSize, Document document, ZIController controller) {
        super(owner, fontSize, document, controller);
        this.owner = owner;
    }

    protected void paintThumbnail(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        
        int curHeight = this.getHeight();
        int curWidth = this.getWidth();
        Insets i = getInsets();
        g = (Graphics2D) g.create(i.left, i.top, curWidth - i.left - i.right, curHeight - i.top - i.bottom);
        curHeight = curHeight - i.top - i.bottom;
        curWidth = curWidth - i.left - i.right;

//        g.setColor(new Color(240, 240, 240));
        g.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, curHeight, new Color(200, 200, 200)));
        g.fillRoundRect(0, 0, curWidth, curHeight, 3, 3);

        String name = owner.getJavaClass().getName();

        if (label == null) {
            label = new JLabel(name);
        }
        label.setSize(curWidth, 15);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(false);
        label.paint(g.create(0, (curHeight - 30) / 2, curWidth, 15));

        if (curHeight > 50) {
            if (javaClassLabel == null) {
                javaClassLabel = new JLabel("<<Java Class>>");
            }
            javaClassLabel.setSize(curWidth, 15);
            javaClassLabel.setHorizontalAlignment(JLabel.CENTER);
            javaClassLabel.setOpaque(false);
            javaClassLabel.paint(g.create(0, 0, curWidth, 15));
        }

        g.setColor(Color.BLACK);
        g.drawLine(0, curHeight / 2, curWidth, curHeight / 2);
        g.drawLine(0, 3 * curHeight / 4, curWidth, 3 * curHeight / 4);

    }

}
