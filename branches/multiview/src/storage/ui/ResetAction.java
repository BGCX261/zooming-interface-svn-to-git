package storage.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Reset player state action.
 *
 * @author www
 */
public abstract class ResetAction extends AbstractAction {
    private final static ImageIcon RESET_ICON = new ImageIcon(new byte[]{71, 73, 70, 56, 57, 97, 30, 0, 30, 0, (byte) 247, 0, 0, (byte) 237, (byte) 249, (byte) 253, (byte) 236, (byte) 249, (byte) 252, (byte) 234, (byte) 248, (byte) 252, (byte) 235, (byte) 248, (byte) 252, (byte) 233, (byte) 248, (byte) 252, (byte) 234, (byte) 247, (byte) 251, (byte) 232, (byte) 247, (byte) 252, (byte) 235, (byte) 247, (byte) 252, (byte) 227, (byte) 245, (byte) 251, (byte) 224, (byte) 243, (byte) 252, (byte) 217, (byte) 241, (byte) 248, (byte) 214, (byte) 240, (byte) 249, (byte) 215, (byte) 240, (byte) 251, (byte) 215, (byte) 240, (byte) 247, (byte) 213, (byte) 239, (byte) 251, (byte) 217, (byte) 238, (byte) 248, (byte) 211, (byte) 238, (byte) 248, (byte) 210, (byte) 210, (byte) 199, (byte) 238, (byte) 204, (byte) 158, (byte) 158, (byte) 216, (byte) 238, (byte) 237, (byte) 200, (byte) 151, (byte) 204, (byte) 204, (byte) 192, (byte) 151, (byte) 213, (byte) 237, (byte) 200, (byte) 200, (byte) 188, (byte) 235, (byte) 195, (byte) 140, (byte) 234, (byte) 194, (byte) 138, (byte) 199, (byte) 199, (byte) 185, (byte) 234, (byte) 193, (byte) 139, (byte) 198, (byte) 198, (byte) 184, (byte) 140, (byte) 209, (byte) 235, (byte) 234, (byte) 191, (byte) 136, (byte) 233, (byte) 189, (byte) 131, (byte) 196, (byte) 196, (byte) 181, (byte) 139, (byte) 207, (byte) 234, (byte) 232, (byte) 188, (byte) 129, (byte) 232, (byte) 188, (byte) 132, (byte) 194, (byte) 194, (byte) 179, (byte) 131, (byte) 205, (byte) 233, (byte) 230, (byte) 187, (byte) 129, (byte) 129, (byte) 204, (byte) 232, (byte) 232, (byte) 185, 126, (byte) 192, (byte) 192, (byte) 177, (byte) 238, (byte) 175, (byte) 185, (byte) 230, (byte) 184, 127, (byte) 190, (byte) 190, (byte) 176, (byte) 230, (byte) 182, 122, 126, (byte) 200, (byte) 232, (byte) 228, (byte) 181, 119, (byte) 188, (byte) 188, (byte) 173, (byte) 187, (byte) 187, (byte) 172, (byte) 228, (byte) 178, 117, 122, (byte) 199, (byte) 230, (byte) 237, (byte) 169, (byte) 180, (byte) 228, (byte) 177, 116, 119, (byte) 198, (byte) 228, (byte) 185, (byte) 185, (byte) 168, (byte) 184, (byte) 184, (byte) 166, (byte) 227, (byte) 176, 114, 117, (byte) 196, (byte) 228, (byte) 183, (byte) 183, (byte) 166, (byte) 227, (byte) 174, 113, (byte) 227, (byte) 173, 111, (byte) 227, (byte) 173, 112, 116, (byte) 195, (byte) 228, (byte) 182, (byte) 182, (byte) 165, 114, (byte) 194, (byte) 227, (byte) 181, (byte) 181, (byte) 164, (byte) 226, (byte) 172, 110, (byte) 226, (byte) 171, 108, (byte) 179, (byte) 179, (byte) 162, (byte) 224, (byte) 169, 106, (byte) 224, (byte) 170, 107, (byte) 235, (byte) 160, (byte) 173, (byte) 178, (byte) 178, (byte) 160, (byte) 177, (byte) 177, (byte) 159, 111, (byte) 190, (byte) 227, (byte) 234, (byte) 159, (byte) 171, (byte) 224, (byte) 168, 103, (byte) 224, (byte) 169, 103, (byte) 224, (byte) 167, 102, (byte) 222, (byte) 168, 104, (byte) 224, (byte) 166, 101, 107, (byte) 188, (byte) 224, (byte) 222, (byte) 165, 102, (byte) 234, (byte) 155, (byte) 169, 103, (byte) 188, (byte) 224, (byte) 223, (byte) 165, 99, (byte) 222, (byte) 165, 100, (byte) 174, (byte) 174, (byte) 156, (byte) 223, (byte) 164, 97, 104, (byte) 187, (byte) 222, 102, (byte) 185, (byte) 222, 102, (byte) 186, (byte) 224, (byte) 172, (byte) 172, (byte) 152, (byte) 232, (byte) 153, (byte) 167, (byte) 233, (byte) 152, (byte) 166, 101, (byte) 185, (byte) 224, (byte) 221, (byte) 162, 95, (byte) 232, (byte) 151, (byte) 164, 99, (byte) 185, (byte) 223, (byte) 221, (byte) 160, 92, (byte) 230, (byte) 150, (byte) 164, (byte) 170, (byte) 170, (byte) 151, 97, (byte) 184, (byte) 223, (byte) 230, (byte) 148, (byte) 161, (byte) 232, (byte) 147, (byte) 161, 95, (byte) 182, (byte) 221, (byte) 168, (byte) 168, (byte) 150, (byte) 219, (byte) 157, 89, (byte) 166, (byte) 166, (byte) 148, (byte) 230, (byte) 145, (byte) 159, (byte) 218, (byte) 155, 88, 92, (byte) 179, (byte) 221, (byte) 165, (byte) 165, (byte) 146, (byte) 218, (byte) 153, 87, (byte) 164, (byte) 164, (byte) 145, 89, (byte) 178, (byte) 219, (byte) 218, (byte) 152, 86, 88, (byte) 176, (byte) 218, (byte) 228, (byte) 141, (byte) 156, (byte) 216, (byte) 150, 83, (byte) 161, (byte) 161, (byte) 141, (byte) 216, (byte) 151, 84, 87, (byte) 174, (byte) 218, (byte) 228, (byte) 139, (byte) 153, 86, (byte) 173, (byte) 218, 84, (byte) 173, (byte) 216, (byte) 214, (byte) 148, 81, (byte) 227, (byte) 137, (byte) 150, 83, (byte) 172, (byte) 216, (byte) 214, (byte) 146, 78, 81, (byte) 170, (byte) 214, (byte) 227, (byte) 133, (byte) 147, (byte) 227, (byte) 134, (byte) 148, (byte) 226, (byte) 132, (byte) 146, (byte) 212, (byte) 143, 75, (byte) 212, (byte) 143, 74, (byte) 226, (byte) 129, (byte) 144, 78, (byte) 166, (byte) 214, 75, (byte) 165, (byte) 212, (byte) 150, (byte) 150, (byte) 128, (byte) 224, 127, (byte) 141, (byte) 224, 125, (byte) 139, (byte) 224, 124, (byte) 139, (byte) 224, 123, (byte) 138, (byte) 223, 122, (byte) 137, (byte) 222, 122, (byte) 138, (byte) 223, 119, (byte) 135, (byte) 205, (byte) 130, 62, (byte) 221, 117, (byte) 132, (byte) 140, (byte) 140, 117, (byte) 221, 115, (byte) 130, 62, (byte) 153, (byte) 205, (byte) 219, 112, (byte) 128, (byte) 200, 119, 53, (byte) 216, 104, 120, 53, (byte) 144, (byte) 200, (byte) 214, 102, 117, (byte) 214, 100, 115, (byte) 212, 97, 113, (byte) 212, 95, 112, (byte) 205, 82, 99, (byte) 200, 72, 88, (byte) 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, (byte) 249, 4, 5, 20, 0, (byte) 163, 0, 44, 0, 0, 0, 0, 30, 0, 30, 0, 0, 8, (byte) 255, 0, 71, 9, 28, 72, 80, 32, 0, 0, 1, 18, 42, 92, (byte) 200, 16, 65, (byte) 193, (byte) 135, (byte) 163, 8, 76, (byte) 176, 64, (byte) 177, (byte) 162, 69, (byte) 139, 33, 22, 64, 44, 104, (byte) 160, 67, (byte) 137, (byte) 143, 32, 67, (byte) 134, (byte) 252, 1, 97, (byte) 224, 65, (byte) 134, 40, 23, 34, 56, (byte) 193, (byte) 178, (byte) 229, (byte) 137, 32, 37, 5, 18, (byte) 144, 64, (byte) 161, (byte) 166, (byte) 205, (byte) 155, 55, 55, 44, 112, (byte) 193, (byte) 179, (byte) 167, (byte) 139, 37, 49, 71, 25, (byte) 192, (byte) 240, (byte) 161, (byte) 168, (byte) 209, (byte) 163, 71, 107, 64, (byte) 152, (byte) 193, (byte) 180, (byte) 233, 12, 41, 65, 13, 100, 16, 65, (byte) 181, (byte) 170, 85, (byte) 171, 57, 32, (byte) 216, (byte) 216, (byte) 202, (byte) 213, (byte) 134, (byte) 150, (byte) 168, 30, 80, (byte) 136, 29, 75, (byte) 150, 108, 15, 8, 58, (byte) 210, (byte) 170, (byte) 213, (byte) 177, 37, (byte) 234, (byte) 136, 22, 112, (byte) 227, (byte) 202, (byte) 149, 123, 4, 66, (byte) 149, (byte) 187, 120, (byte) 171, (byte) 216, (byte) 129, 112, 16, (byte) 128, 1, 19, 47, 2, 11, 30, 60, 24, 10, 4, 46, (byte) 136, 19, 115, (byte) 217, 3, (byte) 129, (byte) 128, 10, 26, 6, 86, (byte) 200, (byte) 152, 76, (byte) 185, 114, (byte) 229, 41, 16, (byte) 192, 104, (byte) 222, 12, (byte) 166, 15, 4, 3, 72, (byte) 190, 24, (byte) 200, (byte) 225, (byte) 164, (byte) 180, (byte) 233, (byte) 211, (byte) 167, (byte) 223, 64, 24, (byte) 195, (byte) 186, (byte) 245, 24, 63, (byte) 159, (byte) 153, (byte) 136, 49, (byte) 192, (byte) 227, (byte) 137, (byte) 237, (byte) 219, (byte) 184, 113, (byte) 203, (byte) 129, (byte) 160, 16, (byte) 193, (byte) 153, (byte) 223, (byte) 129, 30, 24, (byte) 160, (byte) 146, (byte) 198, (byte) 128, (byte) 143, 40, (byte) 200, (byte) 147, 43, 87, 94, 7, 66, (byte) 132, 8, 21, 22, (byte) 168, (byte) 153, 62, 72, (byte) 184, 23, 55, 6, (byte) 134, 88, (byte) 217, (byte) 206, (byte) 189, 123, 119, 61, 16, 46, 92, (byte) 255, 96, 1, 1, (byte) 142, 121, 69, (byte) 194, (byte) 203, (byte) 220, 57, 64, 36, (byte) 139, (byte) 251, (byte) 247, (byte) 240, (byte) 225, (byte) 227, 121, (byte) 160, 65, 3, 12, 8, 116, (byte) 242, 47, 82, 96, 0, 13, (byte) 159, 3, 70, (byte) 132, 33, (byte) 224, (byte) 128, 4, 18, (byte) 248, (byte) 199, 3, 28, 112, 16, 3, 4, (byte) 152, 52, (byte) 200, 73, 3, 6, 0, (byte) 210, (byte) 200, 1, 77, (byte) 144, 97, (byte) 225, (byte) 133, 24, 98, 40, (byte) 200, 3, 32, (byte) 128, 112, 3, 4, 14, (byte) 132, 24, (byte) 162, 1, (byte) 133, 60, 50, (byte) 192, 21, 108, (byte) 164, (byte) 168, (byte) 226, (byte) 138, 43, 30, (byte) 162, 0, 9, 36, (byte) 236, 16, (byte) 148, 64, 6, 16, 2, 73, 1, (byte) 136, 80, (byte) 162, (byte) 227, (byte) 142, 60, (byte) 242, (byte) 168, 73, 3, 41, (byte) 164, 32, (byte) 196, (byte) 140, 66, 25, 18, 73, 2, 12, (byte) 136, (byte) 168, (byte) 228, (byte) 146, 74, (byte) 226, (byte) 128, 67, 23, 68, 30, (byte) 144, (byte) 200, 36, 84, 86, 105, (byte) 165, (byte) 149, (byte) 155, 60, (byte) 224, (byte) 228, (byte) 150, 80, 22, 116, 64, 35, (byte) 149, (byte) 132, 41, (byte) 230, (byte) 152, 99, 118, (byte) 242, 0, 16, 104, (byte) 162, 105, 70, (byte) 148, (byte) 142, 92, (byte) 226, (byte) 230, (byte) 155, 112, (byte) 194, (byte) 233, (byte) 201, 3, 66, (byte) 212, 89, (byte) 231, 26, 68, 14, 32, 73, 38, 124, (byte) 246, (byte) 233, (byte) 167, (byte) 159, (byte) 159, 40, 80, (byte) 196, (byte) 160, (byte) 131, (byte) 182, (byte) 241, 64, 65, 5, (byte) 128, 18, (byte) 202, (byte) 162, (byte) 140, 54, (byte) 218, (byte) 168, 40, 13, 36, 33, (byte) 169, (byte) 164, 113, 28, 74, 16, (byte) 146, 76, 102, 42, (byte) 162, 18, (byte) 156, 114, 58, (byte) 135, (byte) 165, 27, 9, 52, (byte) 128, 16, 88, (byte) 148, 106, (byte) 234, (byte) 169, (byte) 167, (byte) 230, (byte) 161, 64, (byte) 168, 3, 21, (byte) 144, 7, 35, (byte) 176, (byte) 198, 15, 42, (byte) 171, (byte) 172, (byte) 150, 52, (byte) 192, (byte) 170, 64, (byte) 152, 106, (byte) 154, 105, 65, 1, 1, 0, 59});

    /**
     * Creates reset action.
     */
    public ResetAction() {
        super("Reset", RESET_ICON);
        putValue(SHORT_DESCRIPTION, "Reset player state");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, 0));
    }

    /**
     * Invoked when an action occurs.
     */
    public abstract void actionPerformed(ActionEvent e);
}