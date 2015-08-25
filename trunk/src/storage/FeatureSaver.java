package storage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zi.implementation.ZIContainerImpl;
import zi.implementation.document.ImageDocument;
import zi.implementation.document.TextDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of function applicator, a mapper, which knows
 * how to traverse a particular type of object and apply a function to its elements.
 * <p/>
 * Author: www
 */
public class FeatureSaver implements ISerializer {
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

    public void visit(ImageDocument image) {
        features.put("ThumbnailWidth", Integer.toString(image.getThumbnailWidth()));
        features.put("ThumbnailHeight", Integer.toString(image.getThumbnailHeight()));
        features.put("ImageWidth", Integer.toString(image.getWidth()));
        features.put("ImageHeight", Integer.toString(image.getHeight()));
        saveFeatures();
    }

    public void visit(ZIContainerImpl container) {
        features.put("Color", Integer.toString(container.getColor().getRGB()));
        saveFeatures();
    }

    public void visit(TextDocument document) {
    }
}