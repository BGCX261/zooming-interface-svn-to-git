package storage;

import classeditor.projectmodel.JavaClass;
import classeditor.projectmodel.JavaPackage;
import classeditor.projectmodel.Project;
import classeditor.ui.classdocument.model.ClassDocument;
import classeditor.ui.packagecontainer.model.PackageContainer;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import storage.ui.*;
import zi.ZIController;
import zi.ZIGlassPane;
import zi.ZIListener;
import zi.document.image.model.ImageDocument;
import zi.document.text.model.TextDocument;
import zi.models.*;
import zi.views.ZIInformationPlaneView;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Allows playback of user actions with respect to ZI World.
 * <p/>
 * Author: www
 */
public class Playback {
    /**
     * Maximum delay in milliseconds between 0 and 60,000 milliseconds inclusive.
     */
    private static final int MAX_DELAY = 5000;

    private Object[][] events;
    private int eventsCount;
    private JFrame offsetFrame;
    private ReplayControlBar playerControls = null;
    private Action[] actions;

    private Element root;
    private Document document;

    private ArrayList<ZIController> controllers;
    private ArrayList<JPanel> viewports;

    private static Map<Integer, Object> id2item = new HashMap<Integer, Object>();
    private static Map<Object, Integer> item2id = new HashMap<Object, Integer>();
    private static Map<ZIItem, Integer> arrows = new HashMap<ZIItem, Integer>();

    private static Project project = null;

    static {
        try {
            project = new Project(new File("") {
                public boolean isDirectory() {
                    return true;
                }

                public File[] listFiles(FileFilter filter) {
                    return new File[0];
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
     * Reconstructs ZI World from given XML file.
     *
     * @param filename file name of XML document.
     * @return array of viewports ({@link javax.swing.JPanel}'s
     *         representing {@link zi.models.ZIInformationPlane}'s views).
     */
    public static JPanel[] reconstructZIWorld(String filename) {
        return (new Playback(filename, null, false)).getViewports();
    }

    /**
     * Loads zooming world's state from XML file or creates unsettled ZI World.
     *
     * @param filename name of XML file to load initial ZI World state from.
     */
    public Playback(String filename) {
        this(filename, null, false);
    }

    /**
     * Loads zooming world's state from XML file.
     *
     * @param filename file name of XML document.
     * @param frame    host frame of ZI World.
     */
    public Playback(String filename, JFrame frame) {
        this(filename, frame, true);
    }

    /**
     * Creates player with unsettled world.
     *
     * @param frame host frame of ZI World.
     */
    public Playback(JFrame frame) {
        this(null, frame, true);
    }

    /**
     * Loads zooming world's state from XML file.
     *
     * @param filename    file name of XML document.
     * @param frame       host frame of ZI World.
     * @param parseEvents states whether parsing events is necessary.
     */
    private Playback(String filename, JFrame frame, boolean parseEvents) {
        this.offsetFrame = frame;
        parseFile(filename, parseEvents, true);
    }

    private void setTitle(String filename) {
        if (offsetFrame != null) {
            offsetFrame.setTitle(filename == null ? "ZI World Player" : (new File(filename)).getName() + " - ZI World Player");
        }
    }

    private void parseFile(String filename, boolean parseEvents, boolean createSurface) {
        id2item.clear();
        item2id.clear();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is;
            if (filename == null) {
                is = new ByteArrayInputStream(("<?xml version=\"1.0\" encoding=\"utf-8\"?><ZoomingInterfaceWorld date=\"Fri Apr 13 12:30:11 MSD 2007\" version=\"4.0\">\n" +
                        "<InformationPlane Id=\"2\"/>\n" +
                        "<Multiview>\n" +
                        "<View Height=\"500\" Width=\"500\"/>\n" +
                        "<View Height=\"500\" Width=\"500\"/>\n" +
                        "</Multiview>\n" +
                        "<Events/>\n" +
                        "<FinalState/>\n" +
                        "<FinalLocations>\n" +
                        "<Location RelHeight=\"500,0\" RelWidth=\"500,0\" RelX=\"0,0\" RelY=\"0,0\" X=\"5\" Y=\"47\"/>\n" +
                        "<Location RelHeight=\"500,0\" RelWidth=\"500,0\" RelX=\"0,0\" RelY=\"0,0\" X=\"516\" Y=\"47\"/>\n" +
                        "</FinalLocations>\n" +
                        "<GlassPane Id=\"1\"/>\n" +
                        "</ZoomingInterfaceWorld>").getBytes());
            } else {
                if (filename.endsWith(".xml")) {
                    if ((new File(filename + ".gz")).exists()) {
                        is = new GZIPInputStream(new FileInputStream(filename + ".gz"));
                    } else {
                        is = new FileInputStream(filename);
                    }
                } else {
                    is = new GZIPInputStream(new FileInputStream(filename));
                }
            }
            Document doc = builder.parse(is);
            doc.normalizeDocument();
            document = doc;

            setTitle(filename);

            root = doc.getDocumentElement();

            if (createSurface) {
                // Reconstruct ZI World
                NodeList viewportNodes = root.getElementsByTagName("Multiview").item(0).getChildNodes();
                controllers = new ArrayList<ZIController>();
                viewports = new ArrayList<JPanel>();
                for (int j = 0; j < viewportNodes.getLength(); j++) {
                    if ("View".equalsIgnoreCase(viewportNodes.item(j).getNodeName())) {
                        JPanel viewport = new JPanel();
                        NamedNodeMap attr = viewportNodes.item(j).getAttributes();
                        int height = Integer.parseInt(attr.getNamedItem("Height").getNodeValue());
                        int width = Integer.parseInt(attr.getNamedItem("Width").getNodeValue());
                        Dimension size = new Dimension(width, height);
                        viewport.setSize(size);

                        ZIController controller = new ZIController(viewport);
                        ZIInformationPlane.get().addView(controller);

                        viewport.setLayout(null);
                        viewport.add(ZIInformationPlane.get().getView(controller));

                        viewports.add(viewport);
                        controllers.add(controller);
                    }
                }
            }

            if (root.getElementsByTagName("GlassPane").getLength() > 0
                    && root.getElementsByTagName("GlassPane").item(0).getAttributes().getNamedItem("Id") != null) {
                id2item.put(Integer.parseInt(root.getElementsByTagName("GlassPane").item(0)
                        .getAttributes().getNamedItem("Id").getNodeValue()), ZIGlassPane.get());
            }
            Node infoPlaneId = root.getElementsByTagName("InformationPlane").item(0).getAttributes().getNamedItem("Id");
            if (infoPlaneId != null) {
                id2item.put(Integer.parseInt(infoPlaneId.getNodeValue()), ZIInformationPlane.get());
            }

            NodeList children = root.getElementsByTagName("InformationPlane").item(0).getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if ("Object".equalsIgnoreCase(children.item(j).getNodeName())) {
                    transformDOMTreeIntoZIWorld(ZIInformationPlane.get(), children.item(j), !parseEvents);
                }
            }

            for (ZIItem dest : arrows.keySet()) {
                ZIItem src = (ZIItem) id2item.get(arrows.get(dest));
                ZIInformationPlane.get().addArrow(new Arrow(src, dest));
            }

            for (ZIController c : controllers) {
                c.init();
            }

            if (!parseEvents) {
                return;
            }

            for (Integer i : id2item.keySet()) {
                item2id.put(id2item.get(i), i);
            }

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

                    long timestamp = Long.parseLong(attr.getNamedItem("Timestamp").getNodeValue());
                    if (prevTS > 0) {
                        delay = 3 + (int) (timestamp - prevTS);
                        if (delay > MAX_DELAY) {
                            delay = MAX_DELAY;
                        }
                    }

                    int ID = Integer.parseInt(attr.getNamedItem("ID").getNodeValue());
                    int x = (attr.getNamedItem("X") == null
                            ? 0 : Integer.parseInt(attr.getNamedItem("X").getNodeValue()));
                    int y = (attr.getNamedItem("Y") == null
                            ? 0 : Integer.parseInt(attr.getNamedItem("Y").getNodeValue()));

                    if (ID == ComponentEvent.COMPONENT_MOVED || ID == ComponentEvent.COMPONENT_RESIZED) {
                        int width = Integer.parseInt(attr.getNamedItem("Width").getNodeValue());
                        int height = Integer.parseInt(attr.getNamedItem("Height").getNodeValue());
                        events[currEvent] = new Object[]{delay, ID, new Rectangle(x, y, width, height + 60)};
                    } else {
                        int modifiers = Integer.parseInt(attr.getNamedItem("Modifiers").getNodeValue());

                        int scrollAmount = (attr.getNamedItem("ScrollAmount") == null
                                ? 0 : Integer.parseInt(attr.getNamedItem("ScrollAmount").getNodeValue()));
                        int wheelRotation = (attr.getNamedItem("WheelRotation") == null
                                ? 0 : Integer.parseInt(attr.getNamedItem("WheelRotation").getNodeValue()));

                        switch (ID) {
                            case MouseEvent.MOUSE_MOVED:
                            case MouseEvent.MOUSE_DRAGGED:
                                events[currEvent] = new Object[]{delay, ID, x, y};
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

    private void removeViewsRecursively(ZIItem parent) {
        if (parent instanceof ZIContainer) {
            ZIContainer container = (ZIContainer) parent;
            for (ZIItem item : container.getItems()) {
                removeViewsRecursively(item);
            }
        }

        if (parent.getParent() != null) {
            parent.getParent().remove(parent);
        }

        for (ZIController c : controllers) {
            parent.removeView(c);
        }
    }

    private void detachViewsAndControllers() {
        removeViewsRecursively(ZIInformationPlane.get());
        ZIListener.reset(); // Attached to GlassPane items removed, focus management, listeners deleted.

        controllers.clear();
        for (JPanel viewport : viewports) {
            ZIController controller = new ZIController(viewport);
            ZIInformationPlane.get().addView(controller);

            viewport.setLayout(null);
            viewport.add(ZIInformationPlane.get().getView(controller));

            controllers.add(controller);
        }

        // Trick: forcibly transferring focus to information plane (no matter, which view gains focus).
        for (ZIController c : controllers) {
            ZIInformationPlane.get().getView(c).requestFocus();
        }
    }

    /**
     * Resets viewports to their initial state by detaching views from each component of
     * ZI hierarchy starting from {@link zi.models.ZIInformationPlane}, initializing
     * {@link zi.ZIController}'s and creating a new view for each component with
     * initial parameters.
     *
     * @param filename name of XML file to load ZI World from,
     *                 or <code>null</code> to reset loaded ZI World to initial state.
     */
    void reloadViewports(String filename) {
        detachViewsAndControllers();
        if (filename == null) {
            id2item.clear();
            if (root.getElementsByTagName("GlassPane").getLength() > 0
                    && root.getElementsByTagName("GlassPane").item(0).getAttributes().getNamedItem("Id") != null) {
                id2item.put(Integer.parseInt(root.getElementsByTagName("GlassPane").item(0)
                        .getAttributes().getNamedItem("Id").getNodeValue()), ZIGlassPane.get());
            }
            Node infoPlaneId = root.getElementsByTagName("InformationPlane").item(0).getAttributes().getNamedItem("Id");
            if (infoPlaneId != null) {
                id2item.put(Integer.parseInt(infoPlaneId.getNodeValue()), ZIInformationPlane.get());
            }
            item2id.clear();

            NodeList children = root.getElementsByTagName("InformationPlane").item(0).getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if ("Object".equalsIgnoreCase(children.item(j).getNodeName())) {
                    transformDOMTreeIntoZIWorld(ZIInformationPlane.get(), children.item(j), false);
                }
            }
            for (ZIItem dest : arrows.keySet()) {
                ZIItem src = (ZIItem) id2item.get(arrows.get(dest));
                ZIInformationPlane.get().addArrow(new Arrow(src, dest));
            }
            for (Integer i : id2item.keySet()) {
                item2id.put(id2item.get(i), i);
            }
            for (ZIController c : controllers) {
                c.init();
            }
        } else {
            // todo assumes that loaded dump has the same number of views layouted in the same fashion as in current dump
            parseFile(filename, true, false);
        }
    }

    private static void transformDOMTreeIntoZIWorld(ZIContainer parent, Node node, boolean original) {
        NamedNodeMap attr = node.getAttributes();
        double relX = Double.parseDouble(attr.getNamedItem("RelX").getNodeValue().replace(",", "."));
        double relY = Double.parseDouble(attr.getNamedItem("RelY").getNodeValue().replace(",", "."));
        double relWidth = Double.parseDouble(attr.getNamedItem("RelWidth").getNodeValue().replace(",", "."));
        double aspectRatio = Double.parseDouble(attr.getNamedItem("AspectRatio").getNodeValue().replace(",", "."));
        int minLength = Integer.parseInt(attr.getNamedItem("MinLength").getNodeValue());
        int maxLength = Integer.parseInt(attr.getNamedItem("MaxLength").getNodeValue());
        Color color = new Color(Integer.parseInt(attr.getNamedItem("Color").getNodeValue()));
        String clazz = attr.getNamedItem("Class").getNodeValue();
        Node eid = attr.getNamedItem("Id");

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

        RelLocation relLocation = new RelLocation(relX, relY, relWidth, aspectRatio);
        ZIItem element;
        if ("zi.models.ZIItem".equalsIgnoreCase(clazz)) {
            if (original) {
                element = new ZIItem(parent, minLength, maxLength, relLocation, color);
            } else {
                element = new ZIFalseItem(parent, minLength, maxLength, relLocation, color);
            }
        } else if ("zi.document.image.model.ImageDocument".equalsIgnoreCase(clazz)) {
            int thumbnailWidth = Integer.parseInt(features.get("ThumbnailWidth"));
            int thumbnailHeight = Integer.parseInt(features.get("ThumbnailHeight"));
            int imageHeight = Integer.parseInt(features.get("ImageHeight"));
            int imageWidth = Integer.parseInt(features.get("ImageWidth"));
            element = new ImageDocument(parent, minLength, maxLength, relLocation, color, thumbnailWidth, thumbnailHeight, imageHeight, imageWidth);
        } else if ("zi.document.text.model.TextDocument".equalsIgnoreCase(clazz)) {
            int thumbnailWidth = Integer.parseInt(features.get("ThumbnailWidth"));
            int thumbnailHeight = Integer.parseInt(features.get("ThumbnailHeight"));
            int fontSize = Integer.parseInt(features.get("FontSize"));
            element = new TextDocument(parent, minLength, maxLength, relLocation, color, thumbnailWidth, thumbnailHeight, fontSize);
        } else if (("classedito" + "r.ui.packageco" + "ntainer.model.P" + "ackageContainer").equalsIgnoreCase(clazz)) {
            String name = features.get("JavaPacka" + "ge.name");
            // todo args 2-4 seems to be null always, but this behaviour may be changed in future
            element = new PackageContainer(parent, minLength, maxLength, relLocation, color, new JavaPackage(name, new JavaPackage(null, null, project, null), project, ""));
        } else if (("classedito" + "r.ui.classdo" + "cument.model.C" + "lassDocument").equalsIgnoreCase(clazz)) {
            int thumbnailWidth = Integer.parseInt(features.get("ThumbnailWidth"));
            int thumbnailHeight = Integer.parseInt(features.get("ThumbnailHeight"));
            int fontSize = Integer.parseInt(features.get("FontSize"));
            String name = features.get("JavaCla" + "ss.name");
            // todo args 2-4 seems to be null always, but this behaviour may be changed in future
            JavaClass c = new JavaClass(name, new JavaPackage(null, null, project, null), project, "");

            String content = features.get("JavaCla" + "ss.content");
            if (content != null) {
                ArrayList<String> data = new ArrayList<String>();
                data.add(content);
                c.addToDocument(data);
            }
            element = new ClassDocument(parent, minLength, maxLength, relLocation, color, thumbnailWidth, thumbnailHeight, fontSize, c);
            String arrow = features.get("JavaCla" + "ss.Arrow");
            if (arrow != null) {
                arrows.put(element, Integer.parseInt(arrow));
            }
        } else { // "zi.models.ZIContainer" assumed
            if (original) {
                element = new ZIContainer(parent, minLength, maxLength, relLocation, color);
            } else {
                element = new ZIFalseContainer(parent, minLength, maxLength, relLocation, color);
            }
        }
        if (eid != null) {
            int id = Integer.parseInt(eid.getNodeValue());
            id2item.put(id, element);
        }
        parent.add(element);

        if (element instanceof ZIContainer) {
            ZIContainer container = (ZIContainer) element;
            for (int j = 0; j < children.getLength(); j++) {
                if ("Object".equalsIgnoreCase(children.item(j).getNodeName())) {
                    transformDOMTreeIntoZIWorld(container, children.item(j), original);
                }
            }
        }
    }

    /**
     * Gets array of {@link javax.swing.JPanel}'s representing set
     * of {@link zi.models.ZIInformationPlane}'s views.
     *
     * @return array of viewports.
     */
    public JPanel[] getViewports() {
        if (viewports != null) {
            return viewports.toArray(new JPanel[viewports.size()]);
        } else {
            return null;
        }
    }

    /**
     * Gets controllers associated with views.
     *
     * @return array of controllers.
     */
    public ZIController[] getControllers() {
        if (controllers != null) {
            return controllers.toArray(new ZIController[controllers.size()]);
        } else {
            return null;
        }
    }

    /**
     * Replays saved events from initial position to final position.
     *
     * @param initialPosition position of initial event in events history to start replaying with.
     * @param finalPosition   position of last event in events history to play back.
     */
    public void replayRecord(int initialPosition, int finalPosition) {
        IExchange def = new IExchange() {
            public boolean isRunning() {
                return true;
            }

            public void notifyPosition(int pos) {
            }

            public void cursorMoved(Point p) {
            }

            public void done() {
            }

            public int getSpeed() {
                return 1;
            }

            public void leftClick() {
            }

            public void rightClick() {
            }

            public void mouseReleased() {
            }
        };
        replayRecord(initialPosition, finalPosition, def, null);
    }

    /**
     * Replays saved events from initial position to final position.
     *
     * @param initialPosition position of initial event in events history to start replaying with.
     * @param finalPosition   position of last event in events history to play back.
     * @param exchange        provides communication with executing environment
     * @param initialCursor   initial position of the cursor on the screen.
     */
    void replayRecord(int initialPosition, int finalPosition, IExchange exchange, Point initialCursor) {
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
            int speed = exchange.getSpeed();
            final Robot robot = new Robot();
            if (speed > 0) {
                robot.setAutoWaitForIdle(true);
            }

            if (initialCursor != null) {
                robot.mouseMove(initialCursor.x, initialCursor.y);
            }

            int currPos = initialPosition;
            int modifiers;
            int delay;
            while (exchange.isRunning() && (currPos < finalPosition)) {
                delay = (Integer) events[currPos][0];
                if (speed > 0) {
                    robot.delay((int) Math.ceil(delay / speed));
                }
                if (!exchange.isRunning()) {
                    break;
                }
                if ((speed < 0) && (currPos < finalPosition)) {
                    while ((((Integer) events[currPos][1]) == MouseEvent.MOUSE_MOVED) &&
                            (((Integer) events[currPos + 1][1]) == MouseEvent.MOUSE_MOVED) &&
                            (currPos < finalPosition)) {
                        exchange.notifyPosition(currPos);
                        currPos++;
                    }
                    while (currPos > 1 && (((Integer) events[currPos - 1][1]) != MouseEvent.MOUSE_PRESSED) &&
                            (((Integer) events[currPos][1]) == MouseEvent.MOUSE_DRAGGED) &&
                            (((Integer) events[currPos + 1][1]) == MouseEvent.MOUSE_DRAGGED) &&
                            (currPos < finalPosition)) {
                        exchange.notifyPosition(currPos);
                        currPos++;
                    }
                }
                switch ((Integer) events[currPos][1]) {
                    case MouseEvent.MOUSE_MOVED:
                    case MouseEvent.MOUSE_DRAGGED:
                        if (speed < 0) {
                            robot.waitForIdle();
                        }
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
                            if ((speed > 0) & (speed < 3)) {
                                robot.delay(100);
                            }
                        }
                        if (modifiers == InputEvent.BUTTON3_MASK) {
                            exchange.rightClick();
                            if ((speed > 0) & (speed < 3)) {
                                robot.delay(100);
                            }
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
                        exchange.mouseReleased();
                        break;
                    case MouseEvent.MOUSE_WHEEL:
                        if (speed < 0) {
                            robot.waitForIdle();
                        }
                        robot.mouseWheel((Integer) events[currPos][2]);
                        break;
                    case ComponentEvent.COMPONENT_MOVED:
                    case ComponentEvent.COMPONENT_RESIZED:
                        offsetFrame.setBounds((Rectangle) events[currPos][2]);
                        offsetFrame.dispatchEvent(new ComponentEvent(offsetFrame, (Integer) events[currPos][1]));
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

    Object[][] getEventsData() {
        return events;
    }

    /**
     * Gets player controls.
     *
     * @return player controls.
     */
    public JComponent getPlayerControls() {
        if (playerControls == null) {
            playerControls = new ReplayControlBar(offsetFrame, this);
        }
        return playerControls;
    }

    /**
     * Gets array of player actions: open file,
     * save with new final state, reset, play/pause, compare.
     *
     * @return array of actions, some of them may be <code>null</code>.
     */
    public Action[] getActions() {
        if (actions != null) {
            return actions;
        }
        if (playerControls == null) {
            playerControls = new ReplayControlBar(offsetFrame, this);
        }
        Action save = new SaveNewFinalStateAction() {
            public void processFile(String filename) {
                Node[] oldFinalState = new Node[]{root.getElementsByTagName("FinalState").item(0),
                        root.getElementsByTagName("FinalLocations").item(0),
                        root.getElementsByTagName("GlassPane").item(0)};
                for (Node n : oldFinalState) {
                    if (n != null) {
                        root.removeChild(n);
                    }
                }
                Logger.registerAll(id2item);
                Node[] finalStateDump = Logger.createFinalStateDump(document, offsetFrame);
                for (Node n : finalStateDump) {
                    root.appendChild(n);
                }
                document.normalizeDocument();
                Logger.writeDocumentToXML(document, filename);
                setTitle(filename);
            }
        };
        Action compare = new CompareAction() {
            public void actionPerformed(ActionEvent e) {
                if (!((root.getElementsByTagName("FinalState").getLength() > 0)
                        && (root.getElementsByTagName("FinalLocations").getLength() > 0)
                        && (root.getElementsByTagName("GlassPane").getLength() > 0)
                        && (root.getElementsByTagName("GlassPane").item(0).getAttributes().getNamedItem("Id") != null)
                )) {
                    JOptionPane.showMessageDialog(null,
                            "XML file doesn't contain final state to perform comparison!",
                            "Compare", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int distinction = 0;
                NodeList children = root.getElementsByTagName("FinalState").item(0).getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    if ("Object".equalsIgnoreCase(children.item(j).getNodeName())) {
                        distinction += compareRecursively(2, children.item(j)); // Starting from InformationPlane
                    }
                }
                NodeList GPchildren = root.getElementsByTagName("GlassPane").item(0).getChildNodes();
                for (int j = 0; j < GPchildren.getLength(); j++) {
                    if ("Object".equalsIgnoreCase(GPchildren.item(j).getNodeName())) {
                        distinction += compareRecursively(1, GPchildren.item(j)); // Starting from GlassPane
                    }
                }
                System.out.println("Compare: " + distinction + " diff out of " + (id2item.size() - 2));

                Map<Point, ZIInformationPlaneView> point2view = new HashMap<Point, ZIInformationPlaneView>();
                for (ZIInformationPlaneView v : ZIInformationPlane.get().getViews()) {
                    Point p = v.getLocation();
                    SwingUtilities.convertPointToScreen(p, v);
                    if (offsetFrame != null) {
                        p.translate(-offsetFrame.getX(), -offsetFrame.getY());
                    }
                    point2view.put(p, v);
                }

                NodeList locations = root.getElementsByTagName("FinalLocations").item(0).getChildNodes();
                for (int j = 0; j < locations.getLength(); j++) {
                    if ("Location".equalsIgnoreCase(locations.item(j).getNodeName())) {
                        NamedNodeMap attr = locations.item(j).getAttributes();
                        double relX = Double.parseDouble(attr.getNamedItem("RelX").getNodeValue().replace(",", "."));
                        double relY = Double.parseDouble(attr.getNamedItem("RelY").getNodeValue().replace(",", "."));
                        double relHeight = Double.parseDouble(attr.getNamedItem("RelHeight").getNodeValue().replace(",", "."));
                        double relWidth = Double.parseDouble(attr.getNamedItem("RelWidth").getNodeValue().replace(",", "."));
                        int x = Integer.parseInt(attr.getNamedItem("X").getNodeValue());
                        int y = Integer.parseInt(attr.getNamedItem("Y").getNodeValue());

                        ZIInformationPlaneView achievedView = point2view.remove(new Point(x, y));
                        if (achievedView != null) {
                            Location achievedLocation = achievedView.getRelLocation();

                            if ((relX != achievedLocation.getX()) | (relY != achievedLocation.getY())
                                    | (relHeight != achievedLocation.getHeight())
                                    | (relWidth != achievedLocation.getWidth())) {
//                            ZIGlassPane.link = new Link(achievedView, new Location(relX, relY, relHeight, relWidth));
                                System.out.println("Wrong view: " + achievedView);
                            }
                        } else {
                            // view not found
                        }
                    }
                }

            }
        };
        actions = new Action[]{playerControls.getOpenFileAction(), save,
                null, playerControls.getResetAction(), playerControls.getPlayAction(), compare,
                null, new ExitAction()};
        return actions;
    }

    private int compareRecursively(int savedParentId, Node node) {
        // Parsing saved state...
        int distinction = 0;

        NamedNodeMap attr = node.getAttributes();
        double relX = Double.parseDouble(attr.getNamedItem("RelX").getNodeValue().replace(",", "."));
        double relY = Double.parseDouble(attr.getNamedItem("RelY").getNodeValue().replace(",", "."));
//        double relWidth = Double.parseDouble(attr.getNamedItem("RelWidth").getNodeValue().replace(",", "."));
        double aspectRatio = Double.parseDouble(attr.getNamedItem("AspectRatio").getNodeValue().replace(",", "."));
//        int minLength = Integer.parseInt(attr.getNamedItem("MinLength").getNodeValue());
//        int maxLength = Integer.parseInt(attr.getNamedItem("MaxLength").getNodeValue());
        Color color = new Color(Integer.parseInt(attr.getNamedItem("Color").getNodeValue()));
        String clazz = attr.getNamedItem("Class").getNodeValue();
        int id = Integer.parseInt(attr.getNamedItem("Id").getNodeValue());

        LinkedList<String> diff = new LinkedList<String>();
        // todo id2item.contains(id)?
        ZIItem achievedItem = (ZIItem) id2item.get(id);

        // Checking aspect ratio...
        if (achievedItem.getAbsoluteProportion() != aspectRatio) {
            diff.add("Aspect ratio doesn't match: " + achievedItem.getAbsoluteProportion()
                    + " instead of " + aspectRatio);
        }

        // Checking position...
        if (achievedItem.getRelX() != relX | achievedItem.getRelY() != relY) {
            diff.add("Location doesn't match: (" + achievedItem.getRelX() + ", " + achievedItem.getRelY()
                    + ") instead of (" + relX + ", " + relY + ")");
        }

        // Checking color...
        if (!achievedItem.getColor().equals(color)) {
            Color achievedColor = achievedItem.getColor();
            diff.add("Color doesn't match: <font bgcolor=\"rgb(" + achievedColor.getRed() + ", "
                    + achievedColor.getGreen() + ", " + achievedColor.getBlue() + ")\">"
                    + "&nbsp;&nbsp;&nbsp;&nbsp;</font> insted of <font bgcolor=\"rgb(" + color.getRed() + ", "
                    + color.getGreen() + ", " + color.getBlue() + ")\">"
                    + "&nbsp;&nbsp;&nbsp;&nbsp;</font>");
        }

        // Checking parents...
        Object achievedItemParent = achievedItem.getParent();
        if (achievedItemParent == null) {
            achievedItemParent = ZIGlassPane.get();
        }
        int achievedItemParentId = item2id.get(achievedItemParent);
        if (achievedItemParentId != savedParentId) {
            Color achievedItemParentColor = Color.white;
            if (achievedItemParent instanceof ZIItem) {
                achievedItemParentColor = ((ZIItem) achievedItemParent).getColor();
            }
            if (achievedItemParent == ZIInformationPlane.get()) {
                achievedItemParentColor = Color.pink;
            }
            Object savedParent = id2item.get(savedParentId);
            Color savedParentColor = Color.white;
            if (savedParent != null) {
                if (savedParent instanceof ZIItem) {
                    savedParentColor = ((ZIItem) savedParent).getColor();
                }
                if (savedParent == ZIInformationPlane.get()) {
                    savedParentColor = Color.pink;
                }
            }
            diff.add("Parent doesn't match: <font bgcolor=\"rgb(" + achievedItemParentColor.getRed() + ", "
                    + achievedItemParentColor.getGreen() + ", " + achievedItemParentColor.getBlue() + ")\">"
                    + achievedItemParentId + "</font> insted of <font bgcolor=\"rgb(" + savedParentColor.getRed() + ", "
                    + savedParentColor.getGreen() + ", " + savedParentColor.getBlue() + ")\">"
                    + savedParentId + "</font>");
        }

        // Features ignored...

        Object item = id2item.get(id);
        if (item instanceof ZIFalseElement) {
            ZIFalseElement fe = (ZIFalseElement) id2item.get(id);
            if (diff.isEmpty()) {
                fe.mark(false);
            } else {
                fe.mark(true);
                fe.setText(diff);
                distinction = 1;
//            System.out.println("False: " + id + " @ " + id2item.get(id));
            }
        }

        if ("zi.models.ZIContainer".equalsIgnoreCase(clazz)) {
            NodeList children = node.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if ("Object".equalsIgnoreCase(children.item(j).getNodeName())) {
                    distinction += compareRecursively(id, children.item(j));
                }
            }
        }

        return distinction;
    }
}