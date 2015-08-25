package storage.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Compare recorded final state with achieved one.
 *
 * @author www
 */
public abstract class CompareAction extends AbstractAction {
    private final static ImageIcon COMPARE_ICON = new ImageIcon(new byte[]{71, 73, 70, 56, 57, 97, 30, 0, 30, 0, (byte) 230, 0, 0, (byte) 255, (byte) 255, (byte) 255, (byte) 250, (byte) 253, (byte) 250, (byte) 249, (byte) 253, (byte) 247, (byte) 248, (byte) 252, (byte) 247, (byte) 245, (byte) 251, (byte) 244, (byte) 244, (byte) 251, (byte) 242, (byte) 235, (byte) 248, (byte) 233, (byte) 228, (byte) 245, (byte) 225, (byte) 222, (byte) 242, (byte) 217, (byte) 221, (byte) 242, (byte) 217, (byte) 210, (byte) 238, (byte) 201, (byte) 204, (byte) 236, (byte) 196, (byte) 197, (byte) 233, (byte) 191, (byte) 196, (byte) 233, (byte) 186, (byte) 193, (byte) 232, (byte) 184, (byte) 173, (byte) 225, (byte) 160, (byte) 150, (byte) 216, (byte) 134, (byte) 147, (byte) 214, (byte) 135, (byte) 146, (byte) 214, (byte) 129, (byte) 139, (byte) 213, 119, (byte) 131, (byte) 210, 104, (byte) 130, (byte) 208, 115, 103, (byte) 222, 36, 101, (byte) 221, 35, 127, (byte) 206, 114, 97, (byte) 221, 33, 104, (byte) 218, 40, 103, (byte) 218, 39, 100, (byte) 217, 38, 94, (byte) 220, 32, 97, (byte) 217, 36, 90, (byte) 219, 30, 94, (byte) 216, 35, 86, (byte) 217, 29, 90, (byte) 214, 34, 82, (byte) 215, 27, 77, (byte) 214, 27, 86, (byte) 211, 32, 105, (byte) 199, 77, 98, (byte) 204, 46, 105, (byte) 199, 84, 82, (byte) 210, 30, 103, (byte) 199, 70, 95, (byte) 203, 44, 102, (byte) 196, 86, 73, (byte) 212, 24, 78, (byte) 209, 29, 98, (byte) 197, 76, 69, (byte) 211, 23, 101, (byte) 196, 83, 101, (byte) 197, 76, 73, (byte) 208, 26, 92, (byte) 201, 43, 88, (byte) 201, 41, 95, (byte) 199, 47, 68, (byte) 206, 24, 92, (byte) 197, 46, 84, (byte) 199, 40, 88, (byte) 196, 44, 85, (byte) 196, 55, 88, (byte) 195, 43, 88, (byte) 196, 43, 80, (byte) 197, 37, 84, (byte) 194, 42, 85, (byte) 194, 42, 85, (byte) 194, 41, 76, (byte) 195, 35, 84, (byte) 193, 41, 80, (byte) 192, 39, 63, (byte) 198, 25, 81, (byte) 188, 65, 76, (byte) 190, 37, 75, (byte) 190, 37, 72, (byte) 192, 33, 71, (byte) 188, 35, 71, (byte) 188, 36, 67, (byte) 190, 31, 71, (byte) 187, 36, 71, (byte) 187, 35, 68, (byte) 187, 36, 68, (byte) 186, 35, 66, (byte) 185, 33, 62, (byte) 183, 30, 64, (byte) 182, 45, 61, (byte) 182, 30, 61, (byte) 183, 30, 56, (byte) 184, 27, 60, (byte) 180, 39, 56, (byte) 180, 28, 57, (byte) 180, 28, 52, (byte) 178, 25, 51, (byte) 178, 25, 46, (byte) 176, 23, 46, (byte) 175, 23, 41, (byte) 173, 20, 41, (byte) 173, 21, 40, (byte) 172, 21, 37, (byte) 171, 18, 37, (byte) 171, 19, 32, (byte) 169, 16, 32, (byte) 168, 16, 32, (byte) 169, 15, 32, (byte) 168, 15, 27, (byte) 167, 13, 28, (byte) 166, 14, 27, (byte) 166, 13, 27, (byte) 166, 14, 28, (byte) 166, 13, 23, (byte) 164, 12, 23, (byte) 164, 11, 61, (byte) 147, 50, 19, (byte) 163, 9, 19, (byte) 162, 9, 18, (byte) 162, 9, 15, (byte) 160, 7, 15, (byte) 161, 7, 11, (byte) 158, 5, 33, (byte) 132, 17, 29, 125, 12, 30, 91, 19, (byte) 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, (byte) 249, 4, 5, 20, 0, 120, 0, 44, 0, 0, 0, 0, 30, 0, 30, 0, 0, 7, (byte) 238, (byte) 128, 120, (byte) 130, (byte) 131, (byte) 132, (byte) 133, (byte) 134, (byte) 135, (byte) 136, (byte) 137, (byte) 138, (byte) 139, (byte) 140, (byte) 141, (byte) 142, (byte) 143, (byte) 144, (byte) 145, (byte) 146, (byte) 147, (byte) 148, (byte) 149, (byte) 141, 110, 117, 118, 119, (byte) 155, (byte) 156, (byte) 155, 118, 117, 110, (byte) 144, 117, 26, (byte) 164, 30, 30, 34, 37, 37, 46, 51, 55, 69, 117, (byte) 144, 118, 22, 39, 39, 52, 52, 57, 62, 66, 73, 76, 59, 86, 118, (byte) 144, 119, 23, 54, 54, 61, 61, 68, 71, 74, 74, 15, 18, 91, 119, (byte) 191, 25, 56, 56, 65, 68, 72, 78, 78, 13, 0, 21, 93, (byte) 205, (byte) 143, 119, 29, 60, 60, (byte) 211, 72, 81, 16, 0, 0, 17, 94, (byte) 219, (byte) 142, 119, 31, 67, 67, (byte) 212, 81, 50, 1, 0, 6, 44, 98, (byte) 233, (byte) 141, 119, 33, 68, 42, 10, 38, 85, 11, 0, 18, 92, 9, 83, (byte) 230, 30, (byte) 163, 59, 35, 40, 8, 0, (byte) 224, 0, 5, (byte) 128, 3, 83, (byte) 194, (byte) 144, 73, 99, 112, (byte) 209, 29, 18, 77, 38, 20, 0, (byte) 128, 96, (byte) 128, 17, 49, 102, (byte) 206, (byte) 180, (byte) 169, (byte) 168, (byte) 232, 78, (byte) 139, 40, 82, 94, 16, 0, (byte) 128, 65, 34, 26, 54, 111, 72, 38, (byte) 178, 3, (byte) 131, 74, 22, 45, 49, 24, 72, 84, (byte) 211, 38, 78, 28, 95, (byte) 143, (byte) 234, 20, (byte) 193, (byte) 178, (byte) 133, (byte) 203, (byte) 151, 47, 99, (byte) 214, (byte) 180, (byte) 129, 3, (byte) 135, (byte) 206, (byte) 171, 71, (byte) 152, 52, 117, (byte) 226, (byte) 244, 41, (byte) 148, (byte) 165, (byte) 171, 88, (byte) 179, 106, (byte) 221, (byte) 202, (byte) 181, (byte) 171, (byte) 215, 72, (byte) 129, 0, 0, 59});

    /**
     * Creates compare action.
     */
    public CompareAction() {
        super("Compare", COMPARE_ICON);
        putValue(SHORT_DESCRIPTION, "Compare recorded final state with achieved one");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
    }

    /**
     * Invoked when an action occurs.
     */
    public abstract void actionPerformed(ActionEvent e);
}
