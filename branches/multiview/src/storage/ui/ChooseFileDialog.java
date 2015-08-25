package storage.ui;

import javax.swing.*;
import java.io.File;

/**
 * File choose dialog.<br>
 * Using example:<br>
 * ChooseFileDialog cf = new ChooseFileDialog("Solution|dpr;java;cpp;pas");<br>
 * String res = cf.choose();<br>
 * if (res == null) {<br>
 * System.out.println("Dialog was cancelled");<br>
 * } else {<br>
 * System.out.println(res);<br>
 * }<br>
 *
 * @author www
 */
public class ChooseFileDialog implements IFileChooser {
    private JFileChooser chooser;
    private String action;

    /**
     * Choose file dialog constructor with specified filter.
     *
     * @param filter filter: description and extension list (example: "Solution|dpr;java;cpp;pas").
     */
    public ChooseFileDialog(String filter) {
        this(filter, null, null, true);
    }

    /**
     * Choose file dialog constructor with specified filter and title.
     *
     * @param filter filter: description and extension list (example: "Solution|dpr;java;cpp;pas").
     * @param title  title of dialog.
     */
    public ChooseFileDialog(String filter, String title) {
        this(filter, title, null, true);
    }

    /**
     * Choose file dialog constructor with specified filter, title, action and filter view.
     *
     * @param filter                     filter: description and extension list (example: "Solution|dpr;java;cpp;pas").
     * @param title                      title of dialog.
     * @param buttonAction               choose button's caption (example: "Open", "Save", "Choose", "Submit").
     * @param showExtensionInDescription whether to show filter extensions in description or not.
     */
    public ChooseFileDialog(String filter, String title, String buttonAction, boolean showExtensionInDescription) {
        chooser = new JFileChooser();
        action = buttonAction;
        chooser.setFileFilter(new ChooseFileDialogFilter(filter, showExtensionInDescription));
        chooser.setDialogTitle(title);
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    }

    /**
     * Show file choose dialog.
     *
     * @return <code>null</code>, if dialog cancelled, full filename otherwise.
     */
    public String choose() {
        int res = chooser.showDialog(null, action);
        if (res == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getPath();
        } else {
            return null;
        }
    }
}