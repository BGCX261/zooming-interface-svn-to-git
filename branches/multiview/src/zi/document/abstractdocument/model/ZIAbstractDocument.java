package zi.document.abstractdocument.model;

import zi.ZIController;
import zi.views.ZIElementView;
import zi.document.abstractdocument.view.ZIAbstractDocumentView;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.models.ZIItem;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 01.04.2007
 * Time: 23:27:58
 */
public class ZIAbstractDocument extends ZIItem {

    protected final int thumbnailWidth;
    protected final int thumbnailHeight;
    
    public ZIAbstractDocument(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color, int thumbnailWidth, int thumbnailHeight) {
        super(parent, minLength, maxLength, location, color);
        this.thumbnailHeight = thumbnailHeight;
        this.thumbnailWidth = thumbnailWidth;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    
}
