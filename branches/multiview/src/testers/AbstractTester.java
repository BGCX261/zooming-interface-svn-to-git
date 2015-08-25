package testers;

import storage.Playback;
import storage.ui.ExitAction;
import storage.ui.SaveZIWorldAction;
import zi.ZIController;
import zi.ZIGlassPane;
import zi.models.ZIInformationPlane;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class for functionality checkers' entry points.
 * Creates frame with two views and Save-ZI-World action.
 * Initial ZI World state can be loaded from specified XML file,
 * or created manually via overriden method {@link #createUI()}.
 *
 * @author www
 */
public abstract class AbstractTester extends JFrame {
    private JPanel[] viewports;
    protected ZIController[] controllers;

    /**
     * Creates checker instance.
     */
    public AbstractTester() {
        this(null);
    }

    /**
     * Creates checker instance.
     *
     * @param filename name of XML file to load initial ZI World state from,
     *                 or <code>null</code> to create unsettled ZI World.
     */
    public AbstractTester(String filename) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setGlassPane(ZIGlassPane.get());
        ZIGlassPane.get().setSize(getSize());
        ZIGlassPane.get().setLocation(0, 0);
        ZIGlassPane.get().setVisible(true);

        if (filename == null) {
            createUnsettledWorld(2);
        } else {
            Playback player = new Playback(filename);
            viewports = player.getViewports();
            controllers = player.getControllers();
        }

        viewports[0].addComponentListener(new MyComponentListener());
        viewports[1].addComponentListener(new MyComponentListener());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, viewports[0], viewports[1]);
        splitPane.setResizeWeight(0.5);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);
        JMenu item = new JMenu("ZI World");
        menu.add(item);

        createUI();

        for (ZIController c : controllers) {
            c.init();
        }

        item.add(new SaveZIWorldAction(this));
        item.add(new JSeparator());
        item.add(new ExitAction());

//        JToolBar toolbar = new JToolBar();
//        toolbar.add(saveAction);
//        for (Component c : toolbar.getComponents()) {
//            c.setFocusable(false);
//        }
//        getContentPane().add(toolbar, BorderLayout.PAGE_START);

        setSize(1022, 553);
        setResizable(true);
    }

    /**
     * Creates unsettled ZI World.
     *
     * @param viewportsCount count of {@link javax.swing.JPanel}'s
     *                       representing {@link zi.models.ZIInformationPlane}'s views.
     */
    private void createUnsettledWorld(int viewportsCount) {
        controllers = new ZIController[viewportsCount];
        viewports = new JPanel[viewportsCount];
        for (int j = 0; j < viewportsCount; j++) {
            JPanel viewport = new JPanel();

            ZIController controller = new ZIController(viewport);
            ZIInformationPlane.get().addView(controller);

            int width = (int) ZIInformationPlane.get().getViews()[0].getRelLocation().getWidth();
            int height = (int) ZIInformationPlane.get().getViews()[0].getRelLocation().getHeight();
            viewport.setSize(width, height);

            viewport.setLayout(null);
            viewport.add(ZIInformationPlane.get().getView(controller));

            viewports[j] = viewport;
            controllers[j] = controller;
        }
    }

    /**
     * Delegates additional ZI World creation stage.
     * This method could be overriden if a program needs
     * to create specific elements of ZI World on it's own.
     */
    public void createUI() {
    }
}