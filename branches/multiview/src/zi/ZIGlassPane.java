package zi;

import zi.models.ZIInformationPlane;
import zi.models.ZIItem;
import zi.models.actions.MaximizingShortcutAction;
import zi.views.ZIElementView;
import zi.views.ZIInformationPlaneView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Author: Olga Komaleva
 * Author: BlackboX
 * Author: www
 * Date: Feb 18, 2007
 */
@SuppressWarnings("serial")
public class ZIGlassPane extends JComponent {
    private static ZIGlassPane glassPane = null;

    public static ZIGlassPane get() {
        if (glassPane == null) {
            glassPane = new ZIGlassPane();
        }
        return glassPane;
    }

    private Point rectPoint = null;
    private Point rectSize = new Point();
    private JToolBar coverRectanglePanel = new JToolBar();

    private static final int SHADOW_OFFSET = 4;
    private static final Color SHADOW_COLOR = new Color(127, 87, 87);

    private ArrayList<ZIElementView> attachedViews = null;
    private ArrayList<Point> attDs = null;
    private ArrayList<BufferedImage> shadows = null;
    private ArrayList<BufferedImage> images = null;

    private ZIElementView selectedView;
    
    private JToolBar activityPanel = new JToolBar();

    private JToolBar shortcutPanel = new JToolBar() {
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);
        }
    };

    private ZIGlassPane() {
        add(activityPanel);
        activityPanel.setFloatable(false);
        add(shortcutPanel);
        shortcutPanel.setFloatable(false);
    }

    private void paintActivityPanel(Graphics g) {
        if (selectedView != null) {
            ZIInformationPlaneView informationPlaneView = (ZIInformationPlaneView) ZIInformationPlane.get().getView(selectedView.getController());

            Point p = selectedView.getLocation();
            Point informationPlanePoint = SwingUtilities.convertPoint(selectedView.getParent(), p, informationPlaneView);
            Point deltaPoint = new Point(0, 0);

            if (informationPlanePoint.x < 0) {
                deltaPoint.x = informationPlanePoint.x;
                informationPlanePoint.x = 0;
            }

            if (informationPlanePoint.y < 0) {
                deltaPoint.y = informationPlanePoint.y;
                informationPlanePoint.y = 0;
            }

            if ((selectedView.getWidth() + deltaPoint.x >= activityPanel.getWidth())
                    && (selectedView.getHeight() + deltaPoint.y >= activityPanel.getHeight())) {
                Point gPPoint = SwingUtilities.convertPoint(informationPlaneView, informationPlanePoint, this);
                activityPanel.setLocation(gPPoint);
                activityPanel.repaint();
            } else {
                activityPanel.setSize(0, 0);
            }
        } else {
            activityPanel.setSize(0, 0);
        }
    }

    private void paintAttachedViews(Graphics g) {
        if (attachedViews != null) {
            boolean imagesAreExisted = true;
            if (images == null) {
                imagesAreExisted = false;
                images = new ArrayList<BufferedImage>(attachedViews.size());
            }

            boolean shadowsAreExisted = true;
            if (shadows == null) {
                shadowsAreExisted = false;
                shadows = new ArrayList<BufferedImage>(attachedViews.size());
            }

            for (int i = 0; i < attachedViews.size(); i++) {
                ZIElementView attachedView = attachedViews.get(i);
                Point attD = attDs.get(i);

                int x = (int) (ZIListener.zim.getLastX() + attD.getX());
                int y = (int) (ZIListener.zim.getLastY() + attD.getY());
                int w = attachedView.getWidth();
                int h = attachedView.getHeight();

                if (w > 1500 | h > 1500) {
                    g.setColor(SHADOW_COLOR);
                    g.fillRect(x + SHADOW_OFFSET, y + SHADOW_OFFSET, w, h);

                    Graphics cg = g.create(x, y, w, h);
                    attachedView.paint(cg);
                    cg.dispose();
                } else {
                    if (!imagesAreExisted) {
                        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                        images.add(image);
                        Graphics2D g2 = image.createGraphics();
                        attachedView.paint(g2);
                        g2.dispose();
                    }
                    if (!shadowsAreExisted) {
                        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = buffer.createGraphics();
                        attachedView.paint(g2);
                        BufferedImage shadow = ShadowRenderer.createShadow(buffer, SHADOW_OFFSET, 0.5f, Color.black);
                        shadows.add(shadow);
                        g2.dispose();
                    }
                    g.drawImage(shadows.get(i), x, y, null);
                    g.drawImage(images.get(i), x, y, null);
                }
            }

        }
    }

    private void paintCoverRectangle(Graphics g) {
        if (rectPoint != null) {
            int x = (int) rectPoint.getX();
            int y = (int) rectPoint.getY();
            int width = (int) rectSize.getX();
            int height = (int) rectSize.getY();

            if (width < 0) {
                x += width;
                width = -width;
            }

            if (height < 0) {
                y += height;
                height = -height;
            }
            
            g.drawRect(x, y, width, height);
        }
    }

    protected void paintChildren(Graphics g) {
        paintActivityPanel(g);
        paintAttachedViews(g);
        paintCoverRectangle(g);

        shortcutPanel.repaint();
    }

    public void setAttached(ZIElementView element, int x, int y) {
        if (element == null) {
            attachedViews = null;
            shadows = null;
            images = null;
            attDs = null;
        } else {
            attachedViews = new ArrayList<ZIElementView>(1);
            attachedViews.add(element);
            attDs = new ArrayList<Point>(1);
            attDs.add(new Point(x, y));
        }
        repaint();
    }

    public Point[] getAttDs() {
        if (attDs == null) {
            return null;
        } else {
            Point[] ps = new Point[attDs.size()];
            for (int i = 0; i < ps.length; i++) {
                ps[i] =  new Point((int) attDs.get(i).getX(), (int) attDs.get(i).getY());
            }
            return ps;
        }
    } 

    public ZIElementView[] getAttachedElements() {
        if (attachedViews == null) {
            return new ZIElementView[0];
        } else {
            return attachedViews.toArray(new ZIElementView[attachedViews.size()]);
        }
    }

    public void setSelectedElement(ZIElementView view) {
        activityPanel.removeAll();
        this.selectedView = view;
        if (view != null) {
            Point p = view.getLocation();
            Point gPPoint = SwingUtilities.convertPoint(view.getParent(), p, this);
            activityPanel.setLocation(gPPoint);
            Action[] actions = view.getOwner().getActions();
            activityPanel.setSize(34*actions.length, 20);
            for (Action action : actions) {
                activityPanel.add(action);
            }
        } else {
            activityPanel.setSize(0, 0);
        }
        get().repaint();
    }

    public void addShortcut(MaximizingShortcutAction action) {
        shortcutPanel.add(action);
        shortcutPanel.setBounds(0, getHeight() - 20, getWidth(), 20);
    }

    public void setCoverRectangle(Point point) {
        if ((point == null) || (rectPoint == null)) {
            rectPoint = point;
            return;
        }
        rectSize.setLocation(point.x - rectPoint.x, point.y - rectPoint.y);

        repaint();
    }


    public void setCoverRectangleActions(ZIElementView ownerView, Point attP)  {

        if (rectPoint == null) {
            return;
        }

        Component[] children = ownerView.getComponents();
        attachedViews = new ArrayList<ZIElementView>(children.length/2);
        attDs = new ArrayList<Point>(children.length/2);

        if (0 > rectSize.getX()) {
            rectPoint.setLocation(rectPoint.getX() + rectSize.getX(), rectPoint.getY());
            rectSize.setLocation(-rectSize.getX(), rectSize.getY());
        }

        if (0 > rectSize.getY()) {
            rectPoint.setLocation(rectPoint.getX(), rectPoint.getY()  + rectSize.getY());
            rectSize.setLocation(rectSize.getX(), -rectSize.getY());
        }

        rectPoint = SwingUtilities.convertPoint(this, rectPoint, ownerView);

        for (Component child : children) {
            double left = Math.max(child.getX(), rectPoint.getX());
            double right = Math.min(child.getX() + child.getWidth(), rectPoint.getX() + rectSize.getX());

            if (left < right) {
                double top = Math.max(child.getY(), rectPoint.getY());
                double bottom = Math.min(child.getY() + child.getHeight(), rectPoint.getY() + rectSize.getY());
                if (top < bottom) {
                    Point p = SwingUtilities.convertPoint(ownerView, attP, child);
                    ZIItem item = ((ZIElementView)child).getOwner();
                    item.getParent().remove(item);
                    item.setParent(null);
                    attDs.add(new Point((int) -p.getX(),(int) -p.getY()));
                    attachedViews.add((ZIElementView) child);
                }
            }
        }
        rectPoint = null;

        if (attachedViews.size() == 0) {
            attachedViews = null;
            attDs = null;
        }

        repaint();

    }
}




