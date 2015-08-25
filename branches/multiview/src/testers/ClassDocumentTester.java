package testers;

import classeditor.projectmodel.JavaClass;
import classeditor.ui.classdocument.model.ClassDocument;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.models.ZIInformationPlane;
import zi.models.ZIItem;

import java.awt.*;

public class ClassDocumentTester extends AbstractTester {
    public void createUI() {
        int minLength = 10;
        int maxLength = ZIItem.INFINITE_ZOOMING;
        ZIContainer parent = new ZIContainer(ZIInformationPlane.get(), minLength, maxLength,
                new RelLocation(0, 0, 0.5, 0.5), Color.GREEN);
        ZIInformationPlane.get().add(parent);

        JavaClass javaClass = new JavaClass("TestClass.java", null, null, "");
        parent.add(new ClassDocument(parent, 30, -1, new RelLocation(0.1, 0.1, 0.25, 1), Color.white, 150, 100, 1, javaClass));
        parent.add(new ClassDocument(parent, 30, -1, new RelLocation(0.45, 0.1, 0.25, 1), Color.white, 150, 100, 1, javaClass));
    }

    public static void main(String[] args) {
        new ClassDocumentTester().setVisible(true);
    }
}
