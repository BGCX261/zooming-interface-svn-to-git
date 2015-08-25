package zi.models;

import zi.views.ZIInformationPlaneView;
import zi.views.ZIElementView;
import zi.views.ZIItemView;
import zi.ZIListener;
import zi.ZIController;
import zi.ZIInformationPlaneLayout;
import zi.models.actions.CreateContainerAction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Author: Olga Komaleva
 * Date: Feb 22, 2007
 */

//singleton
public class ZIInformationPlane extends ZIContainer {
    private static ZIInformationPlane instance = null;
    private final ArrayList<Action> iPActions = new ArrayList<Action>();
    private final ArrayList<Arrow> arrows = new ArrayList<Arrow>();

    public ZIInformationPlane(ZIContainer parent, int minLength, int maxLength, RelLocation location, Color color) {
        super(parent, minLength, maxLength, location, color);
        //iPActions.add(new CreateContainerAction(this));
    }

    public static ZIInformationPlane get() {
        if (instance == null) {
            instance = new ZIInformationPlane(null, ZIItem.INFINITE_ZOOMING, ZIItem.INFINITE_ZOOMING, new RelLocation(1, 1, 1, 1), Color.black);
        }
        return instance;
    }

    public void setParent(ZIContainer parent) {
    }

    public ZIContainer getParent() {
        return null;
    }

    public boolean setRelBorder(double relX, double relY, double relWidth, double relHeight) {
        return false;
    }

    @Override
    protected ZIElementView generateView(ZIController controller) {
        ZIInformationPlaneView v = new ZIInformationPlaneView(controller);
        v.setLayout(new ZIInformationPlaneLayout(controller, v));
        return v;
    }

    public double getAbsoluteProportion() {
        return 1;
    }

    public double getRelWidth() {
        return 1;
    }

    public double getRelX() {
        return 0;
    }

    public double getRelY() {
        return 0;
    }

/*
    public boolean removeArrow(Arrow arrow) {
        return arrows.remove(arrow);        
    }                                */

    public void addArrow(Arrow arrow) {
        arrows.add(arrow);
    }

    public Arrow[] getArrows() {
        return arrows.toArray(new Arrow[arrows.size()]);
    }

    public Action[] getActions() {
         return iPActions.toArray(new Action[iPActions.size()]);
    }


   public final ZIInformationPlaneView[] getViews() {
       return views.values().toArray(new ZIInformationPlaneView[views.size()]);
   }
}



