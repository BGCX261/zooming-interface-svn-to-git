package classeditor.projectmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

/**
 *
 * @author Fedor Tsarev
 *
 * Date: 18.04.2007
 * Time: 21:33:38
 */
public abstract class ProjectElement {

    protected final Project project;

    protected final HashSet<ProjectElement> innerProjectElements = new HashSet<ProjectElement>();

    protected ProjectElement parent;

    protected boolean isJavaClass;

    protected String path;

    protected String name;

    public ProjectElement(Project project, String path, String name) {
        this.project = project;
        this.path = path;
        this.name = name;
    }

    protected void addElement(ProjectElement element) {
        innerProjectElements.add(element);
    }

    protected void setParent(ProjectElement parent) {
        this.parent = parent;
    }

    public final boolean isJavaClass() {
        return isJavaClass;
    }

    public final boolean isJavaPackage() {
        return !isJavaClass;
    }

    public ProjectElement[] listInnerElements() {
        return innerProjectElements.toArray(new ProjectElement[innerProjectElements.size()]);        
    }

    public void removeFromProject() {
        if (parent != null) {
            parent.innerProjectElements.remove(this);
        }
        project.removeElement(this);
    }

    public String getFullName() {
        return path + "." + name;
    }

    // TODO: maybe use visitor pattern
    public abstract void write(File file) throws FileNotFoundException;
    
}
