package storage;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import zi.ZIGlassPane;
import zi.ZIListener;
import zi.baseElements.RelLocation;
import zi.baseElements.ZICompoundItem;
import zi.baseElements.ZIContainer;
import zi.implementation.InformationPane;
import zi.implementation.ZIContainerImpl;
import zi.implementation.document.ImageDocument;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Allows playback of user actions with respect to ZI World.
 * <p/>
 * Author: www
 */
public class Playback {
    /**
     * Maximum delay in milliseconds between 0 and 60,000 milliseconds inclusive.
     */
    public static final int MAX_DELAY = 10000;

    private Object[][] events;
    private int eventsCount;
    private Component offsetFrame;

    protected interface IExchange {
        boolean isRunning();

        void notifyPosition(int pos);

        void cursorMoved(Point p);

        void done();

        int getSpeed();

        void leftClick();

        void rightClick();

        void mouseReleased();
    }

    /**
     * Loads zooming world's state from XML file.
     *
     * @param filename file name of XML document.
     * @param listener listener attached to some external JFrame.
     */
    public Playback(String filename, ZIListener listener) {
        offsetFrame = InformationPane.get().getParent().getParent().getParent().getParent();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(filename));
            doc.normalizeDocument();

            Element root = doc.getDocumentElement();

            // Reconstruct ZI World
            Node infPlane = root.getElementsByTagName("InformationPlane").item(0);

            InformationPane.get().addZIListener(listener);

            NodeList children = infPlane.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if ("Object".equalsIgnoreCase(children.item(j).getNodeName())) {
                    InformationPane.get().getContainer().add(transformDOMTreeIntoZIWorld(InformationPane.get(), children.item(j), listener));
                }
            }

            InformationPane.get().repaint();
            ZIGlassPane.get().repaint();

            // Parse events
            long prevTS = 0;
            Node eventsTag = root.getElementsByTagName("Events").item(0);
            NodeList eventNodes = eventsTag.getChildNodes();
            eventsCount = 0;
            for (int j = 0; j < eventNodes.getLength(); j++) {
                if ("Event".equalsIgnoreCase(eventNodes.item(j).getNodeName())) {
                    eventsCount++;
                }
            }
            events = new Object[eventsCount][4];
            int currEvent = 0;
            int delay = 0;
            for (int j = 0; j < eventNodes.getLength(); j++) {
                if ("Event".equalsIgnoreCase(eventNodes.item(j).getNodeName())) {
                    Node node = eventNodes.item(j);
                    NamedNodeMap attr = node.getAttributes();

                    int ID = Integer.parseInt(attr.getNamedItem("ID").getNodeValue());
                    int modifiers = Integer.parseInt(attr.getNamedItem("Modifiers").getNodeValue());
                    long timestamp = Long.parseLong(attr.getNamedItem("Timestamp").getNodeValue());
                    int x = (attr.getNamedItem("X") == null
                            ? 0 : Integer.parseInt(attr.getNamedItem("X").getNodeValue()));
                    int y = (attr.getNamedItem("Y") == null
                            ? 0 : Integer.parseInt(attr.getNamedItem("Y").getNodeValue()));

                    int scrollAmount = (attr.getNamedItem("ScrollAmount") == null
                            ? 0 : Integer.parseInt(attr.getNamedItem("ScrollAmount").getNodeValue()));
                    int wheelRotation = (attr.getNamedItem("WheelRotation") == null
                            ? 0 : Integer.parseInt(attr.getNamedItem("WheelRotation").getNodeValue()));

                    if (prevTS > 0) {
                        delay = 3 + (int) (timestamp - prevTS);
                        if (delay > MAX_DELAY) {
                            delay = MAX_DELAY;
                        }
                    }
                    switch (ID) {
                        case MouseEvent.MOUSE_MOVED:
                        case MouseEvent.MOUSE_DRAGGED:
                            Point p = SwingUtilities.convertPoint(InformationPane.get(), new Point(x, y), offsetFrame);
                            events[currEvent] = new Object[]{delay, ID, p.x, p.y};
                            break;
                        case MouseEvent.MOUSE_PRESSED:
                            events[currEvent] = new Object[]{delay, ID, modifiers, 0};
                            break;
                        case MouseEvent.MOUSE_RELEASED:
                            events[currEvent] = new Object[]{delay, ID, modifiers, 0};
                            break;
                        case MouseEvent.MOUSE_WHEEL:
                            events[currEvent] = new Object[]{delay, ID, wheelRotation * scrollAmount, 0};
                            break;
                        default:
                            events[currEvent] = new Object[]{delay, 0, 0, 0};
                            break;
                    }
                    prevTS = timestamp;
                    currEvent++;
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private ZICompoundItem transformDOMTreeIntoZIWorld(ZIContainer parent, Node node, ZIListener listener) {
        NamedNodeMap attr = node.getAttributes();
        double relX = Double.parseDouble(attr.getNamedItem("RelX").getNodeValue().replace(",", "."));
        double relY = Double.parseDouble(attr.getNamedItem("RelY").getNodeValue().replace(",", "."));
        double relWidth = Double.parseDouble(attr.getNamedItem("RelWidth").getNodeValue().replace(",", "."));
        double relHeight = Double.parseDouble(attr.getNamedItem("RelHeight").getNodeValue().replace(",", "."));
        int minWidth = Integer.parseInt(attr.getNamedItem("MinLength").getNodeValue());
        int maxWidth = Integer.parseInt(attr.getNamedItem("MaxLength").getNodeValue());
        String clazz = attr.getNamedItem("Class").getNodeValue();

        NodeList children = node.getChildNodes();
        Map<String, String> features = new TreeMap<String, String>();
        for (int j = 0; j < children.getLength(); j++) {
            if ("FeatureSet".equalsIgnoreCase(children.item(j).getNodeName())) {
                NodeList featureNodes = children.item(j).getChildNodes();
                for (int h = 0; h < featureNodes.getLength(); h++) {
                    if ("Feature".equalsIgnoreCase(featureNodes.item(h).getNodeName())) {
                        NamedNodeMap featureAttr = featureNodes.item(h).getAttributes();
                        String k = featureAttr.getNamedItem("Key").getNodeValue();
                        String v = featureAttr.getNamedItem("Value").getNodeValue();
                        features.put(k, v);
                    }
                }
            }
        }

        ZICompoundItem container;
        if ("zi.implementation.document.ImageDocument".equalsIgnoreCase(clazz)) {
            int thumbnailWidth = Integer.parseInt(features.get("ThumbnailWidth"));
            int thumbnailHeight = Integer.parseInt(features.get("ThumbnailHeight"));
            int imageWidth = Integer.parseInt(features.get("ImageWidth"));
            int imageHeight = Integer.parseInt(features.get("ImageHeight"));

            container = new ImageDocument(parent, minWidth, maxWidth, new RelLocation(relX, relY, relWidth, relHeight),
                    thumbnailWidth, thumbnailHeight, imageWidth, imageHeight);
        } else { // "zi.implementation.ZIContainerImpl" assumed
            Color color = Color.BLACK;

            String c = features.get("Color");
            if (c != null) {
                color = new Color(Integer.parseInt(c));
            }

            container = new ZIContainerImpl(parent, minWidth, maxWidth, color, new RelLocation(relX, relY, relWidth, relHeight));
        }

        container.addZIListener(listener);

        for (int j = 0; j < children.getLength(); j++) {
            if ("Object".equalsIgnoreCase(children.item(j).getNodeName())) {
                container.getContainer().add(transformDOMTreeIntoZIWorld(container, children.item(j), listener));
            }
        }
        return container;
    }

    /**
     * Replays saved events from initial position to final position.
     *
     * @param initialPosition position of initial event in events history to start replaying with.
     * @param finalPosition   position of last event in events history to play back.
     * @param exchange        provides communication with executing environment
     * @param initialCursor   initial position of the cursor on the screen.
     */
    public void replayRecord(int initialPosition, int finalPosition, IExchange exchange, Point initialCursor) {
        if (finalPosition > eventsCount) {
            finalPosition = eventsCount;
        }
        if (initialPosition < 0) {
            initialPosition = 0;
        }
        if (initialPosition > finalPosition) {
            throw new IllegalArgumentException("Initial position number must be smaller than final position number!");
        }
        try {
            final Robot robot = new Robot();
            robot.setAutoWaitForIdle(true);

            if (initialCursor != null) {
                robot.mouseMove(initialCursor.x, initialCursor.y);
            }

            int currPos = initialPosition;
            int speed = exchange.getSpeed();
            int modifiers;
            int delay;
            while (exchange.isRunning() && (currPos < finalPosition)) {
                delay = (Integer) events[currPos][0];
                robot.delay((int) Math.ceil(delay / speed));
                if (!exchange.isRunning()) {
                    break;
                }
                switch ((Integer) events[currPos][1]) {
                    case MouseEvent.MOUSE_MOVED:
                    case MouseEvent.MOUSE_DRAGGED:
                        robot.mouseMove((Integer) events[currPos][2] + offsetFrame.getX(),
                                (Integer) events[currPos][3] + offsetFrame.getY());
                        exchange.cursorMoved(new Point((Integer) events[currPos][2] + offsetFrame.getX(),
                                (Integer) events[currPos][3] + offsetFrame.getY()));
                        break;
                    case MouseEvent.MOUSE_PRESSED:
                        modifiers = (Integer) events[currPos][2];
                        if ((modifiers & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK) {
                            robot.keyPress(KeyEvent.VK_CONTROL);
                            robot.mousePress(modifiers - MouseEvent.CTRL_MASK);
                            robot.keyRelease(KeyEvent.VK_CONTROL);
                        } else {
                            robot.mousePress(modifiers);
                        }
                        if (modifiers == InputEvent.BUTTON1_MASK) {
                            exchange.leftClick();
                        }
                        if (modifiers == InputEvent.BUTTON3_MASK) {
                            exchange.rightClick();
                        }
                        break;
                    case MouseEvent.MOUSE_RELEASED:
                        modifiers = (Integer) events[currPos][2];
                        if ((modifiers & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK) {
                            robot.keyPress(KeyEvent.VK_CONTROL);
                            robot.mouseRelease(modifiers - MouseEvent.CTRL_MASK);
                            robot.keyRelease(KeyEvent.VK_CONTROL);
                        } else {
                            robot.mouseRelease(modifiers);
                        }
                        if (speed < 3) {
                            robot.delay(100);
                        }
                        exchange.mouseReleased();
                        break;
                    case MouseEvent.MOUSE_WHEEL:
                        robot.mouseWheel((Integer) events[currPos][2]);
                        break;
                    default: // all others ignored
                        break;
                }
                exchange.notifyPosition(currPos);
                currPos++;
            }
            if (currPos == finalPosition) {
                exchange.done();
            }

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets number of events to play back.
     *
     * @return number of events.
     */
    public int getEventsCount() {
        return eventsCount;
    }
}