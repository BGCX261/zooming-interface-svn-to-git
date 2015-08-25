package storage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import zi.baseElements.ZICompoundItem;
import zi.baseElements.ZIItem;
import zi.baseElements.ZIItemAdapter;
import zi.implementation.InformationPane;
import zi.implementation.ZIContainerImpl;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

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
    }

    private DocumentBuilder builder;
    private Document document;
    private Element infoPlane;
    private Node events;
    private boolean isRecordingMode;

    /**
     * Creates Logger.
     */
    public Logger() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private Element transformZIWorldIntoDOMTree(ZICompoundItem ziContainer) {
        Element e = document.createElement("Object");

        ZIItemAdapter containee = ziContainer.getItem();
        e.setAttribute("MaxLength", Integer.toString(containee.getMaxLength()));
        e.setAttribute("MinLength", Integer.toString(containee.getMinLength()));
        e.setAttribute("RelHeight", nf.format(containee.getRelHeight()));
        e.setAttribute("RelWidth", nf.format(containee.getRelWidth()));
        e.setAttribute("RelX", nf.format(containee.getRelX()));
        e.setAttribute("RelY", nf.format(containee.getRelY()));
        e.setAttribute("Class", ziContainer.getClass().getName());

        for (ZIItem container : ziContainer.getContainer().getContents()) {
            e.appendChild(transformZIWorldIntoDOMTree((ZICompoundItem) container));
        }

        if (ziContainer instanceof ISerializable) {
            ISerializable s = (ISerializable) ziContainer;
            s.accept(new FeatureSaver(document, e));
        }

        return e;
    }

    /**
     * Switches writer to recording mode when it tracks dynamic status of zooming world.
     */
    public void startRecording() {
        if (isRecordingMode) {
            return;
        }
        document = builder.newDocument();
        infoPlane = document.createElement("InformationPlane");

        for (ZIItem container : InformationPane.get().getContainer().getContents()) {
            infoPlane.appendChild(transformZIWorldIntoDOMTree((ZIContainerImpl) container));
        }

        events = document.createElement("Events");

        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_WHEEL_EVENT_MASK
                | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);

        isRecordingMode = true;
    }

    /**
     * Stops recording.
     */
    public void stopRecording() {
        if (isRecordingMode) {
            Toolkit.getDefaultToolkit().removeAWTEventListener(this);
            document = null;
            infoPlane = null;
            events = null;
            isRecordingMode = false;
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
        try {
            Element root = document.createElement("ZoomingInterfaceWorld");
            root.setAttribute("version", "1.3");
            root.setAttribute("date", (new Date()).toString());

            document.appendChild(root);
            root.appendChild(infoPlane);
            root.appendChild(events);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(document);
            transformer.transform(source, result);
            String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + sw.toString();

            char buffer[] = new char[xmlString.length()];
            xmlString.getChars(0, xmlString.length(), buffer, 0);

            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(buffer);
            fileWriter.close();

            document.removeChild(root);
            root.removeChild(infoPlane);
            root.removeChild(events);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when an event is dispatched in the AWT.
     */
    public void eventDispatched(AWTEvent AWTEvent) {
//        System.out.println("            " + AWTEvent);

        MouseEvent e = (MouseEvent) AWTEvent;
        if ((e.getID() == MouseEvent.MOUSE_ENTERED) || (e.getID() == MouseEvent.MOUSE_EXITED)) {
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

        if ((e.getID() == MouseEvent.MOUSE_MOVED) || (e.getID() == MouseEvent.MOUSE_DRAGGED)) {
            Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), InformationPane.get());
            event.setAttribute("X", Integer.toString((int) p.getX()));
            event.setAttribute("Y", Integer.toString((int) p.getY()));
        }

        events.appendChild(event);
    }
}