package storage.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Play/Pause action.
 *
 * @author www
 */
public abstract class PlayPauseAction extends AbstractAction {
    private final static ImageIcon PLAY_ICON = new ImageIcon(new byte[]{71, 73, 70, 56, 57, 97, 30, 0, 30, 0, (byte) 213, 0, 0, (byte) 153, (byte) 255, 102, (byte) 148, (byte) 251, 99, (byte) 148, (byte) 251, 98, (byte) 140, (byte) 247, 93, (byte) 140, (byte) 246, 94, (byte) 140, (byte) 246, 93, (byte) 132, (byte) 240, 88, (byte) 132, (byte) 241, 88, 122, (byte) 235, 82, 122, (byte) 234, 81, 112, (byte) 227, 74, 111, (byte) 227, 75, 111, (byte) 227, 74, 100, (byte) 220, 67, 100, (byte) 220, 66, 100, (byte) 219, 66, 88, (byte) 212, 59, 89, (byte) 212, 58, 88, (byte) 212, 58, 102, (byte) 204, 51, 76, (byte) 204, 51, 77, (byte) 204, 51, 65, (byte) 196, 43, 64, (byte) 196, 43, 52, (byte) 189, 35, 53, (byte) 188, 35, 53, (byte) 189, 36, 42, (byte) 181, 28, 41, (byte) 181, 28, 42, (byte) 180, 28, 32, (byte) 174, 20, 31, (byte) 174, 21, 31, (byte) 173, 20, 31, (byte) 173, 21, 22, (byte) 168, 14, 22, (byte) 167, 14, 21, (byte) 168, 14, 21, (byte) 167, 15, 0, (byte) 153, 0, (byte) 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, (byte) 249, 4, 5, 20, 0, 39, 0, 44, 0, 0, 0, 0, 30, 0, 30, 0, 0, 6, 127, (byte) 192, (byte) 147, 112, 72, 44, 26, (byte) 143, (byte) 200, (byte) 164, 114, (byte) 201, 108, 58, (byte) 159, (byte) 208, (byte) 168, 116, 74, (byte) 173, 90, (byte) 175, 69, (byte) 147, 9, 75, 52, 37, (byte) 182, (byte) 220, (byte) 147, (byte) 137, (byte) 209, 0, 71, (byte) 181, 104, 116, 35, 82, 49, 63, 77, (byte) 128, 0, (byte) 193, (byte) 144, 88, 52, 32, 20, (byte) 139, (byte) 198, (byte) 221, (byte) 132, 23, 14, 8, 10, 14, 120, 23, 25, 27, 33, 124, 75, 38, 2, 7, 117, 15, 16, 21, 22, 24, 29, 30, 36, 19, (byte) 137, 73, 38, 3, 9, (byte) 130, 18, 121, 25, 28, 31, 37, (byte) 151, 80, 38, 19, (byte) 167, (byte) 168, 19, 27, 32, 34, (byte) 164, (byte) 165, 105, 104, 33, 35, (byte) 174, 87, 38, (byte) 179, (byte) 152, 83, (byte) 166, (byte) 184, (byte) 185, (byte) 187, 97, (byte) 190, (byte) 191, (byte) 192, (byte) 193, (byte) 194, (byte) 195, (byte) 196, 76, 65, 0, 59});

    /**
     * Creates play/pause action.
     */
    public PlayPauseAction() {
        super("Play / Pause", PLAY_ICON);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
    }

    /**
     * Invoked when an action occurs.
     */
    public abstract void actionPerformed(ActionEvent e);
}
