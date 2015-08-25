package classeditor;

import classeditor.projectmodel.JavaClass;
import classeditor.projectmodel.JavaPackage;
import classeditor.projectmodel.Project;
import classeditor.projectmodel.ProjectElement;
import classeditor.ui.actions.SaveProjectAction;
import classeditor.ui.classdocument.model.ClassDocument;
import classeditor.ui.packagecontainer.model.PackageContainer;
import testers.AbstractTester;
import zi.ZIController;
import zi.models.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 15.04.2007
 * Time: 18:55:12
 *
 * @author Fedor Tsarev
 */
public class MainForm extends AbstractTester {

    private Map<ProjectElement, ZIItem> element2item;

    public void createUI() {
        element2item = new HashMap<ProjectElement, ZIItem>();
        ZIInformationPlane plane = ZIInformationPlane.get();
        FileDialog dialog = new FileDialog(this, "", FileDialog.LOAD);
        dialog.setVisible(true);

        Project project = null;
        try {
            if (dialog.getDirectory() == null) {
                System.exit(0);
            }
            File file = new File(dialog.getDirectory());
            project = new Project(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ZIItem item = null;
        try {
            item = createFromProject(project, plane, new RelLocation(0.25, 0.25, 0.5, 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.setFocusable(false);
        dialog.dispose();

        plane.add(item);

        for (JavaClass jc : project.getJavaClasses()) {
            ProjectElement parElem = null;
            try {
                parElem = jc.getAnscestor();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (parElem == null) {
                continue;
            }

            ZIItem parent = element2item.get(parElem);
            ZIItem child = element2item.get(jc);
            if (child == null) {
                continue;
            }
            ZIInformationPlane.get().addArrow(new Arrow(parent, child));
        }

        JMenu menu = new JMenu("Project");

        menu.add(new SaveProjectAction(project));

        this.getJMenuBar().add(menu);

        setSize(1022, 553);
        setResizable(true);
    }

    private ZIItem createFromProject(Project project, ZIContainer parent, RelLocation relLocation) throws IOException {
        return createFromProjectElement(project.getRoot(), parent, relLocation);
    }

    private ZIItem createFromProjectElement(ProjectElement element, ZIContainer parent, RelLocation relLocation) throws IOException {
        if (element.isJavaPackage()) {
            PackageContainer result = new PackageContainer(parent, 10, -1, relLocation, Color.GRAY, (JavaPackage) element);
            for (ZIController c : controllers) {
                result.addView(c);
            }
            ProjectElement[] innerElements = element.listInnerElements();
            int filesCnt = innerElements.length;
            int n = (int) Math.ceil(Math.sqrt(filesCnt));

            double unit = 1.0 / (11 * n + 1);

            outer:
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int num = n * i + j;
                    if (num >= innerElements.length) {
                        break outer;
                    }
                    RelLocation relLoc = new RelLocation((i + 1) * unit + i * 10 * unit, (j + 1) * unit + j * 10 * unit, 10 * unit, 1);
                    result.add(createFromProjectElement(innerElements[num], result, relLoc));
                }
            }

            element2item.put(element, result);

            return result;
        } else if (element.isJavaClass()) {
            ClassDocument result = new ClassDocument(parent, 10, -1, relLocation, new Color(240, 240, 240), 150, 150, 8, (JavaClass) element);
            for (ZIController c : controllers) {
                result.addView(c);
            }

            element2item.put(element, result);

            return result;
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        MainForm mf = new MainForm();
        mf.setTitle("Zooming UML diagram");
        mf.setVisible(true);
    }

}
