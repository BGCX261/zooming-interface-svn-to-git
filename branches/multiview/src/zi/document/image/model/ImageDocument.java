package zi.document.image.model;

import zi.ZIController;
import zi.ZIListener;
import zi.document.abstractdocument.model.ZIAbstractDocument;
import zi.document.image.view.ImageDocumentView;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.views.ZIElementView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import storage.ISerializable;
import storage.ISerializer;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 02.04.2007
 * Time: 13:45:22
 */
public class ImageDocument extends ZIAbstractDocument implements ISerializable {
    private BufferedImage image;

    protected int imageWidth;
    protected int imageHeight;

    public ImageDocument(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color, int thumbnailWidth, int thumbnailHeight, int imageHeight, int imageWidth) {
        super(parent, minLength, maxLength, location, color, thumbnailWidth, thumbnailHeight);
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;

        image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public Image getImage() {
        return image;
    }

    public void clear() {
        image.getGraphics().clearRect(0, 0, imageWidth, imageHeight);
        changeAllViews();
    }

    public void drawLine(int lastX, int lastY, int x, int y) {
        image.getGraphics().drawLine(lastX, lastY, x, y);
        changeAllViews();
    }

    @Override
    protected ZIElementView generateView(ZIController controller) {
        return new ImageDocumentView(this, controller);
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
