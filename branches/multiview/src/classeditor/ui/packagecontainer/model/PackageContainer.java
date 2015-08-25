package classeditor.ui.packagecontainer.model;

import classeditor.projectmodel.JavaPackage;
import classeditor.ui.packagecontainer.actions.CreateClassAction;
import classeditor.ui.packagecontainer.actions.CreatePackageAction;
import classeditor.ui.packagecontainer.actions.DeletePackageAction;
import classeditor.ui.packagecontainer.view.PackageContainerView;
import storage.ISerializable;
import storage.ISerializer;
import zi.ZIContainerLayout;
import zi.ZIController;
import zi.models.RelLocation;
import zi.models.ZIContainer;
import zi.views.ZIElementView;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 16.04.2007
 * Time: 14:16:53
 */
public class PackageContainer extends ZIContainer implements ISerializable {

    public JavaPackage getJavaPackage() {
        return javaPackage;
    }

    private final JavaPackage javaPackage;

    public PackageContainer(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color, JavaPackage javaPackage) {
        super(parent, minLength, maxLength, location, color);
        this.javaPackage = javaPackage;
        actions.clear();
        containerActions.clear();
        containerActions.add(new CreateClassAction(this));
        containerActions.add(new CreatePackageAction(this));
        containerActions.add(new DeletePackageAction(this));
    }

    public String getPackageName() {
        return javaPackage.getName();
    }

    @Override
    protected ZIElementView generateView(ZIController controller) {
        PackageContainerView view = new PackageContainerView(this, controller);
        view.setLayout(new ZIContainerLayout(controller));
        return view;
    }

    /**
     * Calls back the {@link storage.ISerializer}'s <code>visit()</code> method for corresponding class so that
     * separate concrete visitor classes can then be written that perform some particular operations.
     *
     * @param serializer visitor instance.
     */
    public void accept(ISerializer serializer) {
        serializer.visit(this);
    }
}
