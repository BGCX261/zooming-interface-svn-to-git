package classeditor.projectmodel;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 18.04.2007
 * Time: 10:25:18
 */
public class Project {

    private HashMap<String, JavaPackage> name2package = new HashMap<String, JavaPackage>();
    private HashMap<String, JavaClass> name2class = new HashMap<String, JavaClass>();

    public HashSet<JavaClass> getJavaClasses() {
        return javaClasses;
    }

    private HashSet<JavaClass> javaClasses = new HashSet<JavaClass>();

    private final JavaPackage root;

    /**
     * 
     * @param file
     * @throws java.io.IOException
     */
    public Project(File file) throws IOException {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Cannot create project from one file - directory needed");
        }

        root = new JavaPackage(file.getName(), null, this, "");
        String fullName = "";
        name2package.put(fullName, root);

        File[] innerFiles = file.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return !pathname.isHidden() && (pathname.isDirectory() || pathname.getName().endsWith(".java"));
            }
        });

        Arrays.sort(innerFiles, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return (o1.isDirectory() ? 0 : 1) - (o2.isDirectory() ? 0 : 1);
            }
        });

        for (File innerFile : innerFiles) {
            root.addElement(createFromFile(innerFile, fullName, root));
        }
    }

    public JavaClass getJavaClassByFullName(String fullName) {
        return name2class.get(fullName);
    }

    private ProjectElement createFromFile(File file, String path, JavaPackage parent) throws IOException {
        if (file.isDirectory()) {
            //JavaPackage result = new PackageContainer(parent, 10, -1, relLocation, Color.GRAY, new JavaPackage(file.getName(), null));
            JavaPackage result = new JavaPackage(file.getName(), parent, this, path);
            String fullName = path + "." + file.getName();
            name2package.put(fullName, result);

            File[] innerFiles = file.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return !pathname.isHidden() && (pathname.isDirectory() || pathname.getName().endsWith(".java"));
                }
            });

            Arrays.sort(innerFiles, new Comparator<File>() {

                public int compare(File o1, File o2) {
                    return (o1.isDirectory() ? 0 : 1) - (o2.isDirectory() ? 0 : 1);
                }
            });

            for (File innerFile : innerFiles) {
                result.addElement(createFromFile(innerFile, fullName, result));
            }

            return result;
        } else if (file.isFile()) {
            JavaClass result = new JavaClass(getFilenameWithoutExtension(file.getName()), parent, this, path);

            String fullName = path + "." + getFilenameWithoutExtension(file.getName());

            name2class.put(fullName, result);
            javaClasses.add(result);

            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<String> content = new ArrayList<String>();
            while (true) {
                String s = reader.readLine();
                if (s == null) {
                    break;
                }
                content.add(s);
            }
            result.addToDocument(content);

            return result;
        }
        return null;
    }

    private String getFilenameWithoutExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    public ProjectElement getRoot() {
        return root;
    }

    public void removeElement(ProjectElement projectElement) {
        name2class.remove(projectElement.getFullName());
    }

    public void writeToDisk(String path) throws FileNotFoundException {
        File file = new File(path);
        root.write(file);
    }

}
