package classeditor.projectmodel;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 12.04.2007
 * Time: 19:59:53
 */
public class JavaClass extends ProjectElement {

    protected Document document;

    public JavaClass(String name, JavaPackage parent, Project project, String path) {
        super(project, path, name);
        this.parent = parent;
        document = new PlainDocument();
        isJavaClass = true;
    }

    public String getName() {
        return name;
    }

    public void addToDocument(Collection<String> collection) {
        for (String s : collection) {
            try {
                document.insertString(document.getEndPosition().getOffset(), s + "\n", null);
            } catch (BadLocationException e) {
                // Cannot happen :)
            }
        }
    }

    public Document getDocument() {
        return document;
    }

    public JavaClass getAnscestor() throws IOException {
        return new Parser().getAnscestor();
    }


    public void write(File file) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(new File(file.getAbsolutePath() + "\\\\" + this.name + ".java"));
        try {
            out.print(document.getText(0, document.getLength()));
        } catch (BadLocationException e) {
            // should never happen
            assert false;
        }
        out.close();
    }

    class Parser {
        final String EXTENDS_KEYWORD = "extends";
        final String IMPORT_KEYWORD = "import";

        private JavaClass getAnscestor() throws IOException {
            StreamTokenizer st = null;
            ArrayList<String> imports = new ArrayList<String>();
            
            try {
                st = new StreamTokenizer(new StringReader(document.getText(0, document.getLength())));
                st.slashSlashComments(true);
                st.slashStarComments(true);
            } catch (BadLocationException e) {
                // should never happen
            }
            while (true) {
                int type = st.nextToken();
                if (type == StreamTokenizer.TT_EOF) {
                    break;
                }
                if (EXTENDS_KEYWORD.equals(st.sval)) {
                    st.nextToken();
                    String className = st.sval;
                    for (String s : imports) {
                        if (s.endsWith(className)) {
                            return project.getJavaClassByFullName("." + s);
                        }
                    }
                    return project.getJavaClassByFullName(path + "." + className);
                } else if (IMPORT_KEYWORD.equals(st.sval)) {
                    st.nextToken();
                    imports.add(st.sval);
                }
            }
            return null;
        }
    }

}
