package testers;

import zi.document.image.model.ImageDocument;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.models.ZIInformationPlane;
import zi.models.ZIItem;

import java.awt.*;

public class ImageDocumentTester extends AbstractTester {
    public void createUI() {
        int minLength = 10;
        int maxLength = ZIItem.INFINITE_ZOOMING;
        ZIContainer parent = new ZIContainer(ZIInformationPlane.get(), minLength, maxLength,
                new RelLocation(0, 0, 0.5, 0.5), Color.GREEN);
        ZIInformationPlane.get().add(parent);

        double relX = Math.random() * 0.5;
        double relY = Math.random() * 0.5;
        parent.add(new ImageDocument(parent, 30, -1, new RelLocation(relX, relY, 0.5, 0.5), Color.white, 50, 50, 100, 100));
    }

    public static void main(String[] args) {
        new ImageDocumentTester().setVisible(true);
    }
}
