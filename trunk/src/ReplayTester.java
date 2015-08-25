import storage.Playback;
import storage.ReplayControlBar;
import zi.ZIController;
import zi.ZIGlassPane;
import zi.ZIListener;

import javax.swing.*;
import java.awt.*;

/**
 * Just reads some XML file, builds up ZI World and replays actions.
 * <p/>
 * Author: www
 */
public class ReplayTester {
    /**
     * Application entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        final JFrame j = new JFrame("ZI World Player");
        j.setSize(600, 600);
        j.getContentPane().setSize(j.getSize());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        j.setLocation((screenSize.width - j.getWidth()) / 2, (screenSize.height - j.getHeight()) / 2);
        j.setResizable(false);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        j.setGlassPane(ZIGlassPane.get());
        ZIGlassPane.get().setVisible(true);

        final ReplayControlBar bar = new ReplayControlBar(j, new Playback("sample.xml",
                new ZIListener(new ZIController(j.getContentPane()))));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                j.setVisible(true);
                bar.setVisible(true);
            }
        });
    }
}