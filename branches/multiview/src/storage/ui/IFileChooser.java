package storage.ui;

/**
 * Provides a simple mechanism for the user to choose a file.
 *
 * @author www
 */
public interface IFileChooser {
    /**
     * Show file chooser dialog.
     *
     * @return <code>null</code>, if dialog cancelled,
     *         full filename otherwise.
     */
    public String choose();
}
