package testers;

import storage.Playback;
import zi.ZIGlassPane;

import javax.swing.*;
import java.awt.*;

/**
 * Just reads some XML file, builds up ZI World and replays actions.
 * <p/>
 * Author: www
 */
class ReplayTester extends JFrame {
    /**
     * Creates player.
     */
    private ReplayTester() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        setGlassPane(ZIGlassPane.get());
        ZIGlassPane.get().setSize(getSize());
        ZIGlassPane.get().setLocation(0, 0);
        ZIGlassPane.get().setVisible(true);

        Playback player = new Playback(this);
        JPanel[] viewports = player.getViewports();
        viewports[0].addComponentListener(new MyComponentListener());
        viewports[1].addComponentListener(new MyComponentListener());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, viewports[0], viewports[1]);
        splitPane.setResizeWeight(0.5);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        getContentPane().add(player.getPlayerControls(), BorderLayout.PAGE_END);

        JMenuBar menu = new JMenuBar();
        JMenu item = new JMenu("Player");
        for (Action a : player.getActions()) {
            if (a == null) {
                item.add(new JSeparator());
            } else {
                item.add(a);
            }
        }
        menu.add(item);
        setJMenuBar(menu);
    }

    /**
     * Application entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        final JFrame j = new ReplayTester();

        Dimension size = new Dimension(1022, 553 + 60);
        j.setSize(size);
        j.setPreferredSize(size);
        j.setVisible(true);
    }
}