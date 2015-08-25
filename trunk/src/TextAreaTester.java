import zi.ZIController;
import zi.ZIGlassPane;
import zi.ZIListener;
import zi.baseElements.RelLocation;
import zi.baseElements.ZIItem;
import zi.implementation.InformationPane;
import zi.implementation.ZIContainerImpl;
import zi.implementation.document.TextDocument;

import javax.swing.*;
import java.awt.*;

/**
 * @author BlackboX
 * @date 06.03.2007
 */
public class TextAreaTester extends JFrame {

    TextAreaTester() {

        this.setSize(600, 600);
        int minWidth = 50;
        int maxWidth = ZIItem.INFINITE_ZOOMING;
        ZIContainerImpl oldCont;

        final ZIController controller = new ZIController(this.getContentPane());
        ZIListener listener = new ZIListener(controller);
        InformationPane.get().addZIListener(listener);
        oldCont = new ZIContainerImpl(InformationPane.get(), minWidth, maxWidth, Color.GREEN, new RelLocation(0, 0, 0.5, 0.5));

        TextDocument zita = new TextDocument(oldCont, minWidth, maxWidth, new RelLocation(0, 0, 0.5, 0.5), 60, 60, 8);
        zita.addZIListener(listener);
        //ZIImage imageDocument =  new ZIImage(oldCont, minWidth, maxWidth, new RelLocation(0, 0, 0.5, 0.5), 200, 200); 

        oldCont.getContainer().add(zita);
        oldCont.addZIListener(listener);

        InformationPane.get().getContainer().add(oldCont);

        this.setGlassPane(ZIGlassPane.get());

        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        ZIGlassPane.get().setSize(this.getWidth(), this.getHeight());
        ZIGlassPane.get().setLocation(0, 0);
        ZIGlassPane.get().setVisible(true);

    }

    public static void main(String[] args) {
        new TextAreaTester().setVisible(true);
    }
}
