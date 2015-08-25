package storage.ui;

import storage.ILogger;
import storage.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Saves ZI World to specified file.
 *
 * @author www
 */
public class SaveZIWorldAction extends AbstractAction {
    private final static ImageIcon SAVE_ICON = new ImageIcon(new byte[]{71, 73, 70, 56, 57, 97, 27, 0, 27, 0, (byte) 230, 0, 0, 82, 66, (byte) 134, (byte) 193, (byte) 193, (byte) 210, (byte) 136, (byte) 128, (byte) 179, 122, 122, (byte) 131, 102, 84, (byte) 161, (byte) 247, (byte) 247, (byte) 247, 0, (byte) 255, 0, (byte) 197, (byte) 197, (byte) 197, (byte) 227, (byte) 224, (byte) 226, (byte) 153, (byte) 153, (byte) 153, 122, 108, (byte) 181, 99, 83, (byte) 157, 122, 106, (byte) 174, (byte) 153, (byte) 153, (byte) 204, (byte) 221, (byte) 218, (byte) 220, (byte) 128, 115, (byte) 184, (byte) 139, 127, (byte) 199, (byte) 255, (byte) 255, (byte) 255, (byte) 136, 123, (byte) 190, 87, 68, (byte) 155, (byte) 152, (byte) 145, (byte) 199, 110, 96, (byte) 169, (byte) 204, (byte) 204, (byte) 204, (byte) 237, (byte) 233, (byte) 234, (byte) 212, (byte) 210, (byte) 213, (byte) 132, 122, (byte) 183, 100, 88, (byte) 146, 112, 94, (byte) 167, 125, 112, (byte) 183, (byte) 136, (byte) 136, (byte) 142, 84, 69, (byte) 137, (byte) 227, (byte) 227, (byte) 226, 102, 102, (byte) 153, (byte) 130, 117, (byte) 186, (byte) 145, (byte) 135, (byte) 194, (byte) 209, (byte) 208, (byte) 211, (byte) 216, (byte) 216, (byte) 219, (byte) 238, (byte) 235, (byte) 234, 99, 74, (byte) 156, (byte) 167, (byte) 166, (byte) 192, (byte) 225, (byte) 225, (byte) 235, 108, 91, (byte) 165, (byte) 142, (byte) 132, (byte) 190, (byte) 161, (byte) 155, (byte) 203, 88, 73, (byte) 141, (byte) 163, (byte) 163, (byte) 166, 86, 71, (byte) 153, 104, 90, (byte) 158, (byte) 214, (byte) 214, (byte) 214, (byte) 222, (byte) 222, (byte) 222, 117, 110, (byte) 153, (byte) 133, 121, (byte) 189, (byte) 131, (byte) 131, (byte) 138, (byte) 145, (byte) 134, (byte) 192, (byte) 209, (byte) 209, (byte) 232, 110, 96, (byte) 166, (byte) 230, (byte) 226, (byte) 228, (byte) 142, (byte) 131, (byte) 194, (byte) 153, (byte) 153, (byte) 204, (byte) 154, (byte) 146, (byte) 198, 127, 117, (byte) 176, (byte) 230, (byte) 230, (byte) 230, 126, 112, (byte) 186, 114, 100, (byte) 170, 92, 73, (byte) 156, (byte) 236, (byte) 234, (byte) 236, (byte) 241, (byte) 239, (byte) 239, (byte) 149, (byte) 141, (byte) 195, 93, 76, (byte) 151, (byte) 156, (byte) 156, (byte) 161, 102, 102, (byte) 153, (byte) 214, (byte) 222, (byte) 222, (byte) 129, (byte) 129, (byte) 178, (byte) 141, (byte) 141, (byte) 148, 86, 69, (byte) 147, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, (byte) 249, 4, 5, 20, 0, 6, 0, 44, 0, 0, 0, 0, 27, 0, 27, 0, 0, 7, (byte) 255, (byte) 128, 12, (byte) 130, 12, 48, (byte) 133, (byte) 134, (byte) 135, (byte) 136, (byte) 134, 27, (byte) 139, 27, 6, 12, 43, 57, 12, 17, (byte) 147, (byte) 148, (byte) 149, (byte) 150, (byte) 148, 27, 16, 16, 63, 35, (byte) 143, (byte) 145, (byte) 151, (byte) 149, 5, (byte) 162, (byte) 163, 5, 17, 27, 60, 47, 63, 7, (byte) 158, (byte) 146, 61, (byte) 174, (byte) 175, (byte) 174, 31, (byte) 178, 49, (byte) 180, 36, 36, (byte) 166, 50, 47, 21, (byte) 171, (byte) 144, (byte) 146, (byte) 160, 17, (byte) 164, (byte) 164, (byte) 166, 2, 15, 55, (byte) 188, (byte) 159, (byte) 176, (byte) 177, 31, (byte) 180, 49, (byte) 182, 36, (byte) 133, (byte) 166, 51, 28, (byte) 198, (byte) 172, (byte) 160, (byte) 193, (byte) 193, (byte) 209, (byte) 211, (byte) 199, (byte) 173, 61, (byte) 178, (byte) 179, (byte) 181, (byte) 207, (byte) 133, 35, 35, (byte) 166, 33, 10, 41, (byte) 219, (byte) 150, (byte) 215, (byte) 235, 27, (byte) 230, (byte) 232, (byte) 213, (byte) 222, (byte) 203, (byte) 224, (byte) 134, (byte) 227, (byte) 227, (byte) 229, (byte) 231, (byte) 233, (byte) 147, (byte) 235, (byte) 251, 27, 15, 10, 4, (byte) 171, 118, 124, 66, (byte) 128, 0, 92, (byte) 184, 122, 8, 11, 108, (byte) 240, (byte) 241, 47, (byte) 224, 39, 96, (byte) 251, (byte) 216, 49, 92, 16, 80, 2, 3, 20, (byte) 191, 40, 5, (byte) 179, (byte) 177, 65, (byte) 129, 2, (byte) 138, 12, 118, 72, (byte) 144, (byte) 192, (byte) 168, (byte) 164, (byte) 201, (byte) 147, 29, 63, 86, (byte) 148, 48, (byte) 163, (byte) 229, (byte) 140, 16, 15, 98, (byte) 198, (byte) 244, 72, (byte) 179, (byte) 166, 71, (byte) 144, 34, 89, (byte) 186, (byte) 132, 25, (byte) 147, (byte) 131, 79, (byte) 155, 30, 125, 114, 48, (byte) 177, (byte) 138, (byte) 130, (byte) 206, 25, 63, (byte) 146, 42, 93, (byte) 186, 52, (byte) 131, (byte) 208, (byte) 161, 69, 93, (byte) 254, (byte) 176, (byte) 128, (byte) 129, 68, 12, 4, 61, 46, 104, (byte) 189, (byte) 128, 35, 8, (byte) 128, (byte) 175, 34, 100, 2, (byte) 137, (byte) 218, (byte) 242, 71, (byte) 213, 23, 26, 52, 108, (byte) 229, 26, (byte) 163, 7, 0, 34, 68, (byte) 145, 106, (byte) 136, 93, 53, 36, (byte) 132, (byte) 221, 31, 14, 28, (byte) 188, 8, (byte) 193, (byte) 160, 68, (byte) 137, 110, (byte) 205, 112, (byte) 188, (byte) 141, 59, (byte) 151, 65, (byte) 221, (byte) 187, 4, 95, 60, 96, 112, (byte) 161, 71, 65, 18, 35, 98, 120, 88, (byte) 176, (byte) 160, (byte) 134, (byte) 221, 16, 46, (byte) 232, 94, (byte) 254, (byte) 129, (byte) 227, (byte) 130, 98, 6, (byte) 142, 29, (byte) 192, 24, 97, (byte) 193, 1, 11, (byte) 202, 42, 100, 78, 88, (byte) 197, 96, (byte) 179, (byte) 214, 23, 28, 24, 32, 16, 77, (byte) 250, 0, (byte) 140, (byte) 211, 11, 82, (byte) 199, 92, 29, 0, 9, (byte) 131, (byte) 152, 63, (byte) 132, 8, (byte) 129, 45, (byte) 155, (byte) 182, 5, 11, 36, 88, 16, 32, (byte) 160, (byte) 251, (byte) 193, 106, 3, 39, 82, 84, (byte) 168, (byte) 160, (byte) 164, 69, (byte) 139, 34, 9, 18, 36, (byte) 233, 64, 99, (byte) 128, (byte) 247, 1, 46, (byte) 194, (byte) 139, 15, 127, (byte) 192, (byte) 128, 1, 24, 7, (byte) 210, (byte) 171, 95, (byte) 207, (byte) 190, 125, (byte) 250, 17, (byte) 129, 0, 0, 59});
    private final static IFileChooser chooser = new ChooseFileDialog("ZI World XML dump|xml;gz", "Save XML dump file ...", "Save", false);

    private ILogger LOG;

    /**
     * Creates save-to-XML-file action.
     *
     * @param frame host frame of ZI World.
     */
    public SaveZIWorldAction(JFrame frame) {
        super("Save", SAVE_ICON);
        putValue(SHORT_DESCRIPTION, "Save to XML file");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));

        LOG = new Logger(frame);
        LOG.startRecording();
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        String filename = chooser.choose();
        if (filename != null) {
            filename = filename.replace(".xml.gz", ".xml");
            if (!filename.endsWith(".xml")) {
                filename += ".xml";
            }
            LOG.writeXML(filename);
        }
    }
}