package storage.ui;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Simple filter for {@link ChooseFileDialog}.
 *
 * @author www
 * @see ChooseFileDialog
 */
class ChooseFileDialogFilter extends FileFilter {
    private HashSet<String> filters;
    private String filterDescription;
    private boolean showExt;

    /**
     * Choose file dialog filter constructor with specified filter.
     *
     * @param filter filter: description and extension list (example: "Solution|dpr;java;cpp;pas").
     */
    public ChooseFileDialogFilter(String filter) {
        this(filter, true);
    }

    /**
     * Choose file dialog filter constructor with specified filter.
     *
     * @param filter                     filter: description and extension list (example: "Solution|dpr;java;cpp;pas").
     * @param showExtensionInDescription whether to show filter extensions in description or not.
     */
    public ChooseFileDialogFilter(String filter, boolean showExtensionInDescription) {
        filters = new HashSet<String>();
        showExt = showExtensionInDescription;
        if (filter != null) {
            StringTokenizer st = new StringTokenizer(filter, ".,;|");
            filterDescription = st.nextToken();

            String token;
            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (token.length() > 0) {
                    filters.add(token.toLowerCase());
                }
            }
        }
    }

    /**
     * Extract extension from file.
     *
     * @param f file.
     * @return extension.
     */
    private static String extractExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    /**
     * Whether the given file is accepted by this filter.
     *
     * @param f file to test.
     * @return true, if given file accepted.
     */
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = extractExtension(f);
            if (extension != null && filters.contains(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get description of this filter.
     *
     * @return description of this filter.
     */
    public String getDescription() {
        if (showExt) {
            String description = filterDescription == null ? "(" : filterDescription + " (";

            Iterator<String> it = filters.iterator();
            while (it.hasNext()) {
                description += "*." + it.next();
                if (it.hasNext()) {
                    description += ", ";
                }
            }
            description += ")";
            return description;
        } else {
            return filterDescription;
        }
    }
}