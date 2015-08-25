package storage.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Terminates running application.
 *
 * @author www
 */
public class ExitAction extends AbstractAction {
    private final static ImageIcon EXIT_ICON = new ImageIcon(new byte[]{71, 73, 70, 56, 57, 97, 30, 0, 24, 0, (byte) 196, 0, 0, 69, 69, 69, 0, (byte) 255, 0, (byte) 153, (byte) 153, (byte) 153, 102, 102, 102, (byte) 196, (byte) 196, (byte) 196, (byte) 161, (byte) 161, (byte) 161, (byte) 213, (byte) 213, (byte) 213, (byte) 141, (byte) 141, (byte) 141, 75, 75, 75, (byte) 134, (byte) 134, (byte) 134, 90, 90, 90, (byte) 189, (byte) 189, (byte) 189, (byte) 222, (byte) 222, (byte) 222, 121, 121, 121, (byte) 204, (byte) 204, (byte) 204, (byte) 184, (byte) 184, (byte) 184, (byte) 153, (byte) 153, (byte) 153, (byte) 232, (byte) 232, (byte) 232, 110, 110, 110, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, (byte) 249, 4, 5, 20, 0, 1, 0, 44, 0, 0, 0, 0, 30, 0, 24, 0, 0, 5, (byte) 140, 96, 32, (byte) 142, 100, 105, (byte) 158, 104, (byte) 170, (byte) 174, 108, 123, 46, 76, (byte) 202, 44, (byte) 174, (byte) 248, 20, 69, 108, 50, (byte) 248, (byte) 227, 62, (byte) 130, (byte) 160, 64, 55, 98, 8, 5, 62, (byte) 150, (byte) 241, 72, 92, 10, (byte) 137, 43, (byte) 195, 97, 58, 53, 4, (byte) 164, (byte) 212, (byte) 131, (byte) 181, 102, 72, 120, (byte) 189, (byte) 139, (byte) 175, 119, 91, (byte) 187, 38, 26, (byte) 232, 116, 35, 65, 46, 95, (byte) 213, (byte) 233, (byte) 182, 59, 32, 0, (byte) 216, (byte) 237, (byte) 130, 57, (byte) 137, 112, (byte) 239, 19, (byte) 244, 1, 124, 125, 126, 115, (byte) 130, 119, 117, (byte) 132, 53, (byte) 134, 118, 127, (byte) 139, 0, 127, 45, (byte) 142, (byte) 144, (byte) 129, (byte) 131, (byte) 147, 41, 14, 8, (byte) 153, (byte) 153, (byte) 150, (byte) 129, (byte) 154, (byte) 153, 14, 44, 9, 10, (byte) 163, (byte) 160, 38, 14, (byte) 163, 10, 9, 46, 2, 18, (byte) 165, 39, 14, 18, 121, 53, 17, 43, (byte) 180, (byte) 128, (byte) 183, (byte) 184, (byte) 185, 44, 33, 0, 59});

    /**
     * Creates exit action.
     */
    public ExitAction() {
        super("Exit", EXIT_ICON);
        putValue(SHORT_DESCRIPTION, "Exit");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
