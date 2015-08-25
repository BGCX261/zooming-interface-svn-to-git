package storage;

import classeditor.ui.classdocument.model.ClassDocument;
import classeditor.ui.packagecontainer.model.PackageContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zi.document.image.model.ImageDocument;
import zi.document.text.model.TextDocument;
import zi.models.Arrow;
import zi.models.ZIInformationPlane;
import zi.models.ZIItem;

import javax.swing.text.BadLocationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of function applicator, a mapper, which knows
 * how to traverse a particular type of object and apply a function to its elements.
 * <p/>
 * Author: www
 */
class FeatureSaver implements ISerializer {
    private final Document document;
    private final Element e;
    /**
     * Mapping: Keys --> Features.
     */
    private Map<String, String> features;

    public FeatureSaver(Document document, Element e) {
        this.document = document;
        this.e = e;
        features = new HashMap<String, String>();
    }

    /**
     * Saves ZI-object-specific features.
     */
    private void saveFeatures() {
        if (features != null && !features.isEmpty()) {
            Element featureSet = document.createElement("FeatureSet");
            for (String k : features.keySet()) {
                Element feature = document.createElement("Feature");
                feature.setAttribute("Key", k);
                feature.setAttribute("Value", features.get(k));
                featureSet.appendChild(feature);
            }
            e.appendChild(featureSet);
        }
    }

    public void visit(ZIItem item) {
    }

    public void visit(ImageDocument image) {
        features.put("ThumbnailWidth", Integer.toString(image.getThumbnailWidth()));
        features.put("ThumbnailHeight", Integer.toString(image.getThumbnailHeight()));
        features.put("ImageWidth", Integer.toString(image.getImageWidth()));
        features.put("ImageHeight", Integer.toString(image.getImageHeight()));
        saveFeatures();
    }

    public void visit(TextDocument text) {
        features.put("ThumbnailWidth", Integer.toString(text.getThumbnailWidth()));
        features.put("ThumbnailHeight", Integer.toString(text.getThumbnailHeight()));
        features.put("FontSize", Integer.toString(text.getFontSize()));
        saveFeatures();
    }

    public void visit(PackageContainer container) {
        features.put("JavaPack" + "age.name", container.getPackageName());
        saveFeatures();
    }

    public void visit(ClassDocument doc) {
        features.put("ThumbnailWidth", Integer.toString(doc.getThumbnailWidth()));
        features.put("ThumbnailHeight", Integer.toString(doc.getThumbnailHeight()));
        features.put("FontSize", Integer.toString(doc.getFontSize()));
        features.put("JavaCla" + "ss.name", doc.getJavaClass().getName());
        try {
            features.put("JavaCla" + "ss.content", doc.getJavaClass().getDocument().getText(1, doc.getJavaClass().getDocument().getLength()));
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        for (Arrow arrow : ZIInformationPlane.get().getArrows()) {
            if (arrow.child == doc) {
                features.put("JavaCla" + "ss.Arrow", Integer.toString(Logger.identify(arrow.parent)));
            }
        }
        saveFeatures();
    }
}