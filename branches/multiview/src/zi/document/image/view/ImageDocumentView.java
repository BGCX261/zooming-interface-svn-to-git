package zi.document.image.view;

import zi.ZIController;
import zi.document.abstractdocument.view.ZIAbstractDocumentView;
import zi.document.image.model.ImageDocument;
import zi.document.image.view.actions.ClearAction;
import zi.models.ZIItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 02.04.2007
 * Time: 14:38:11
 */
public class ImageDocumentView extends ZIAbstractDocumentView {

    protected ImageDocument owner;

    protected final ImageRenderer imageRenderer;

    public ImageDocumentView(ImageDocument owner, ZIController controller) {
        super(owner, controller);
        
        this.owner = owner;

        documentPanel.setLayout(new BorderLayout());

        imageRenderer = new ImageRenderer(owner, owner.getImageWidth(), owner.getImageHeight());

        JToolBar toolBar = new JToolBar();
        toolBar.add(new ClearAction(imageRenderer));
        toolBar.setFloatable(false);

        documentPanel.add(toolBar, BorderLayout.NORTH);
        documentPanel.add(imageRenderer, BorderLayout.CENTER);
    }

    protected void paintThumbnail(Graphics g) {
        // do nothing
    }

    protected void zoom() {
        // Everything is done automatically
    }

    public ZIItem getOwner() {
        return owner;
    }

    public ZIController getController() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
