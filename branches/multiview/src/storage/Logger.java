package storage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import storage.ui.ZIFalseElement;
import zi.ZIGlassPane;
import zi.models.Location;
import zi.models.ZIContainer;
import zi.models.ZIInformationPlane;
import zi.models.ZIItem;
import zi.views.ZIElementView;
import zi.views.ZIInformationPlaneView;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * Allows recording of user actions with respect to ZI World.
 * <p/>
 * NOTE: GlassPane is unrelated to ZI World thus not being saved.
 * NOTE: Targeting might be not very precise.
 * <p/>
 * Author: www
 */
public class Logger implements ILogger, AWTEventListener {
    private static NumberFormat nf = new DecimalFormat();

    static {
        nf.setMaximumFractionDigits(100);
        nf.setMinimumFractionDigits(1);
        nf.setGroupingUsed(false);
    }

    private DocumentBuilder builder;
    private Document document;
    private Element infoPlane;
    private Element views;
    private Node events;
    private boolean isRecordingMode;
    private static Map<Object, Integer> item2id = new HashMap<Object, Integer>();
    private static int id;
    private JFrame frame;

    /**
     * Creates Logger.
     *
     * @param frame host frame of ZI World.
     */
    public Logger(JFrame frame) {
        this.frame = frame;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void register(Object item) {
        id++;
        item2id.put(item, id);
    }

    private static void registerAll(ZIItem element) {
        register(element);

        if (element instanceof ZIContainer) {
            ZIContainer container = (ZIContainer) element;
            for (ZIItem item : container.getItems()) {
                registerAll(item);
            }
        }
    }

    static void registerAll(Map<Integer, Object> id2item) {
        item2id.clear();
        for (Integer i : id2item.keySet()) {
            item2id.put(id2item.get(i), i);
        }
    }

    static int identify(Object item) {
        if (!item2id.containsKey(item)) {
            register(item);
        }
        return item2id.get(item);
    }

    private static Element transformZIWorldIntoDOMTree(ZIItem element, Document document) {
        Element e = document.createElement("Object");

        e.setAttribute("MaxLength", Integer.toString(element.getMaxLength()));
        e.setAttribute("MinLength", Integer.toString(element.getMinLength()));
        e.setAttribute("AspectRatio", nf.format(element.getAbsoluteProportion()));
        e.setAttribute("RelWidth", nf.format(element.getRelWidth()));
        e.setAttribute("RelX", nf.format(element.getRelX()));
        e.setAttribute("RelY", nf.format(element.getRelY()));
        e.setAttribute("Color", Integer.toString(element.getColor().getRGB()));

        if (element instanceof ZIFalseElement) {
            e.setAttribute("Class", ((ZIFalseElement) element).getCanonicalClass().getName());
        } else {
            e.setAttribute("Class", element.getClass().getName());
        }

        e.setAttribute("Id", Integer.toString(identify(element)));

        if (element instanceof ZIContainer) {
            ZIContainer container = (ZIContainer) element;
            for (ZIItem item : container.getItems()) {
                e.appendChild(transformZIWorldIntoDOMTree(item, document));
            }
        }

        element.accept(new FeatureSaver(document, e));
        return e;
    }

    /**
     * Switches writer to recording mode when it tracks dynamic status of zooming world.
     */
    public void startRecording() {
        if (isRecordingMode) {
            return;
        }
        register(ZIGlassPane.get());
        registerAll(ZIInformationPlane.get());

        document = builder.newDocument();
        infoPlane = document.createElement("InformationPlane");
        infoPlane.setAttribute("Id", Integer.toString(identify(ZIInformationPlane.get())));

        for (ZIItem item : ZIInformationPlane.get().getItems()) {
            infoPlane.appendChild(transformZIWorldIntoDOMTree(item, document));
        }

        events = document.createElement("Events");

        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_WHEEL_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK);

        isRecordingMode = true;

        views = document.createElement("Multiview");
        for (ZIElementView view : ZIInformationPlane.get().getViews()) {
            Element e = document.createElement("View");

            e.setAttribute("Height", Integer.toString(view.getHeight()));
            e.setAttribute("Width", Integer.toString(view.getWidth()));

            views.appendChild(e);
        }
    }

    /**
     * Stops recording.
     */
    public void stopRecording() {
        if (isRecordingMode) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            document = null;
            infoPlane = null;
            views = null;
            events = null;
            isRecordingMode = false;
            item2id.clear();
        }
    }

    static Node[] createFinalStateDump(Document document, Component offsetFrame) {
        Element finalState = document.createElement("FinalState");
        for (ZIItem item : ZIInformationPlane.get().getItems()) {
            finalState.appendChild(transformZIWorldIntoDOMTree(item, document));
        }

        Element finalLocations = document.createElement("FinalLocations");
        for (ZIInformationPlaneView v : ZIInformationPlane.get().getViews()) {
            Element location = document.createElement("Location");

            Point p = v.getLocation();
            SwingUtilities.convertPointToScreen(p, v);
            if (offsetFrame != null) {
                p.translate(-offsetFrame.getX(), -offsetFrame.getY());
            }

            location.setAttribute("X", Integer.toString(p.x));
            location.setAttribute("Y", Integer.toString(p.y));

            Location relLocation = v.getRelLocation();
            location.setAttribute("RelX", nf.format(relLocation.getX()));
            location.setAttribute("RelY", nf.format(relLocation.getY()));
            location.setAttribute("RelHeight", nf.format(relLocation.getHeight()));
            location.setAttribute("RelWidth", nf.format(relLocation.getWidth()));

            finalLocations.appendChild(location);
        }

        Element glassPaneContent = document.createElement("GlassPane");
        glassPaneContent.setAttribute("Id", Integer.toString(identify(ZIGlassPane.get())));
        ZIElementView[] attachedViews = ZIGlassPane.get().getAttachedElements();
        if (attachedViews != null) { // God helps those who help themselves
            for (ZIElementView v : attachedViews) {
                glassPaneContent.appendChild(transformZIWorldIntoDOMTree(v.getOwner(), document));
            }
        }

        return new Node[]{finalState, finalLocations, glassPaneContent};
    }

    static void writeDocumentToXML(Document document, String filename) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
            String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + sw.toString();

            // Create the GZIP output stream
            GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(filename + ".gz"));
            out.write(xmlString.getBytes());
            out.finish();
            out.close();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes collected dump to XML file.
     *
     * @param filename XML file name to write occured events to.
     */
    public void writeXML(String filename) {
        if (!isRecordingMode) {
            return;
        }

        Element root = document.createElement("ZoomingInterfaceWorld");
        root.setAttribute("version", "4.0");
        root.setAttribute("date", (new Date()).toString());

        document.appendChild(root);
        root.appendChild(infoPlane);
        root.appendChild(views);
        root.appendChild(events);

        for (Node n : createFinalStateDump(document, null)) {
            root.appendChild(n);
        }

        writeDocumentToXML(document, filename);

        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            root.removeChild(children.item(i));
        }
        document.removeChild(root);
    }

    /**
     * Invoked when an event is dispatched in the AWT.
     */
    public void eventDispatched(AWTEvent AWTEvent) {
        if (AWTEvent instanceof MouseEvent) {
            MouseEvent e = (MouseEvent) AWTEvent;
            if (e.getID() == MouseEvent.MOUSE_ENTERED || e.getID() == MouseEvent.MOUSE_EXITED) {
                return;
            }
            Element event = document.createElement("Event");
            event.setAttribute("ID", Integer.toString(e.getID()));
            event.setAttribute("Timestamp", Long.toString(System.currentTimeMillis()));
            event.setAttribute("Modifiers", Integer.toString(e.getModifiers()));

            if (e instanceof MouseWheelEvent) {
                MouseWheelEvent AWTMouseWheelEvent = (MouseWheelEvent) e;
                event.setAttribute("ScrollAmount", Integer.toString(AWTMouseWheelEvent.getScrollAmount()));
                event.setAttribute("WheelRotation", Integer.toString(AWTMouseWheelEvent.getWheelRotation()));
            }

            Point p = e.getPoint();
            SwingUtilities.convertPointToScreen(p, (Component) e.getSource());
            event.setAttribute("X", Integer.toString(p.x - frame.getX()));
            event.setAttribute("Y", Integer.toString(p.y - frame.getY()));

            events.appendChild(event);
        } else if (AWTEvent instanceof ComponentEvent) {
            ComponentEvent e = (ComponentEvent) AWTEvent;
            if (e.getSource() == frame) {
                if (e.getID() == ComponentEvent.COMPONENT_MOVED || e.getID() == ComponentEvent.COMPONENT_RESIZED) {
                    Element event = document.createElement("Event");
                    event.setAttribute("ID", Integer.toString(e.getID()));
                    event.setAttribute("Timestamp", Long.toString(System.currentTimeMillis()));

                    Rectangle b = ((Component) e.getSource()).getBounds();
                    event.setAttribute("X", Integer.toString(b.x));
                    event.setAttribute("Y", Integer.toString(b.y));
                    event.setAttribute("Width", Integer.toString(b.width));
                    event.setAttribute("Height", Integer.toString(b.height));

                    events.appendChild(event);
                }
            }
        }
    }
}