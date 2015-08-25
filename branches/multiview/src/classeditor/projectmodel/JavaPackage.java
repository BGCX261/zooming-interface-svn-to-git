package classeditor.projectmodel;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 12.04.2007
 * Time: 21:21:45
 */
public class JavaPackage extends ProjectElement {

    public JavaPackage(String name, JavaPackage parent, Project project, String path) {
        super(project, path, name);
        this.parent = parent;
        isJavaClass = false;
    }

    public String getName() {
        return name;
    }

    public void write(File file) throws FileNotFoundException {
        String path = file.getAbsolutePath() + "\\\\" + name;
        File cur = new File(path);
        cur.mkdirs();
        for (ProjectElement pe : this.innerProjectElements) {
            pe.write(cur);
        }
    }
}
