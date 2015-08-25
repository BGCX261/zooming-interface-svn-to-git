package zi.implementation.document;

import storage.ISerializer;
import zi.baseElements.RelLocation;
import zi.baseElements.ZIContainer;
import zi.implementation.ZIAbstractDocument;
import zi.implementation.document.actions.ClearAction;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 22.02.2007
 * Time: 23:37:09
 * To change this template use File | Settings | File Templates.
 */

public class ImageDocument extends ZIAbstractDocument {

    private final int imageWidth;
    private final int imageHeight;

    private final JToolBar toolBar;

    private final ImageRenderer imageRenderer;
    private final BufferedImage image;

    public ImageDocument(ZIContainer parent, int minWidth, int maxWidth, RelLocation location, int thumbnailWidth, int thumbnailHeight, int imageWidth, int imageHeight) {
        super(parent, minWidth, maxWidth, location, thumbnailWidth, thumbnailHeight);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        documentPanel.setLayout(new BorderLayout());
        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        imageRenderer = new ImageRenderer(image, imageWidth, imageHeight);

        toolBar = new JToolBar();
        toolBar.add(new ClearAction(imageRenderer));
        toolBar.setFloatable(false);
        
        documentPanel.add(toolBar, BorderLayout.NORTH);
        documentPanel.add(imageRenderer, BorderLayout.CENTER);
    }


    public void clear() {
        imageRenderer.clear();
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    /*public boolean isFocusable() {
        return true;
    }*/


    /**
     * Calls back the {@link storage.ISerializer}'s visit() method for corresponding class so that
     * separate concrete visitor classes can then be written that perform some particular operations.
     *
     * @param serializer visitor instance.
     */
    public void accept(ISerializer serializer) {
        serializer.visit(this);
    }

    protected void zoom() {
        // Do nothing
        // Everything is done automatically
    }
}
