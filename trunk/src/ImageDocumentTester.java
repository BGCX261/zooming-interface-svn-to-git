import zi.ZIController;
import zi.ZIGlassPane;
import zi.ZIListener;
import zi.baseElements.RelLocation;
import zi.baseElements.ZIItem;
import zi.implementation.InformationPane;
import zi.implementation.ZIContainerImpl;
import zi.implementation.document.ImageDocument;

import javax.swing.*;
import java.awt.*;

public class ImageDocumentTester extends JFrame {

    public void dispose(){
        super.dispose();
    }

    ImageDocumentTester() {

        this.setSize(600, 600);
        int minWidth = 10;
        int maxWidth = ZIItem.INFINITE_ZOOMING;

        getContentPane().setSize(600, 600);
        final ZIController controller = new ZIController(this.getContentPane());

        ZIListener listener = new ZIListener(controller);

        InformationPane.get().addZIListener(listener);
        ZIContainerImpl oldCont = new ZIContainerImpl(InformationPane.get(), minWidth, maxWidth, Color.GREEN, new RelLocation(0, 0, 0.5, 0.5));

        ImageDocument imageDocument = new ImageDocument(oldCont, minWidth, maxWidth, new RelLocation(0, 0, 0.5, 0.5), 60, 60, 200, 200);
        imageDocument.addZIListener(listener);

        oldCont.getContainer().add(imageDocument);
        oldCont.addZIListener(listener);


        InformationPane.get().getContainer().add(oldCont);

        this.setGlassPane(ZIGlassPane.get());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        ZIGlassPane.get().setSize(this.getWidth(), this.getHeight());
        ZIGlassPane.get().setLocation(0, 0);
        ZIGlassPane.get().setVisible(true);

    }

    public static void main(String[] args) {
        new ImageDocumentTester().setVisible(true);
    }
}
