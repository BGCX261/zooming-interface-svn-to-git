package zi.document.abstractdocument.view;

import zi.ZIController;
import zi.document.abstractdocument.model.ZIAbstractDocument;
import zi.views.ZIElementView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 01.04.2007
 * Time: 23:28:13
 */
public abstract class ZIAbstractDocumentView extends ZIElementView implements ComponentListener {
    protected final static int ACTIVE = 1;
    protected final static int NON_ACTIVE = 0;

    protected int state;

    protected final JComponent documentPanel;

    protected ZIAbstractDocument owner;

    protected TitledBorder activeBorder;
    protected TitledBorder inactiveBorder;

    protected ZIController controller;

    public ZIAbstractDocumentView(ZIAbstractDocument owner, ZIController controller) {
        this.owner = owner;
        this.controller = controller;

        this.setLayout(null);

        activeBorder = new TitledBorder(new LineBorder(Color.BLUE), "Active");
        inactiveBorder = new TitledBorder(new LineBorder(Color.BLACK), "Inactive");

        documentPanel = new JComponent() {
        };
        this.add(documentPanel, BorderLayout.CENTER);

        documentPanel.setVisible(true);

        this.addComponentListener(this);

        setState(NON_ACTIVE);
    }

    public ZIController getController() {
        return controller; 
    }

    protected void paintBackground(Graphics g) {
        int curHeight = getHeight();
        int curWidth = getWidth();
        g.setColor(owner.getColor());
        g.fillRect(0, 0, curWidth, curHeight);
    }

    public void paintComponent(Graphics g) {
//        System.out.println("paint");
        int curHeight = getHeight();
        int curWidth = getWidth();

        paintBackground(g);

        if (curHeight <= owner.getMinLength() || curWidth < owner.getMinLength()) {
            // Component is invisible
        } else if (curHeight <= owner.getThumbnailHeight() || curWidth <= owner.getThumbnailWidth()) {
//            System.out.println("paint thumbnail");
            paintThumbnail(g);
        } else {
            switch (state) {
                case ACTIVE: {
                    break;
                }

                case NON_ACTIVE: {
                    Insets i = getInsets();
                    for (Component c : documentPanel.getComponents()) {
                        Graphics gg = g.create(c.getX() + i.left, c.getY() + i.top, c.getWidth(), c.getHeight());
                        if (c.isVisible()) {
                            c.paint(gg);
                            if (c instanceof Container) {
                                paintum(gg, (Container) c);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    protected abstract void paintThumbnail(Graphics g);

    private void paintum(Graphics g, Container container) {
        for (Component c : container.getComponents()) {
            Graphics gg = g.create(c.getX(), c.getY(), c.getWidth(), c.getHeight());
            if (c.isVisible()) {
                c.paint(gg);
                if (c instanceof Container) {
                    paintum(gg, (Container) c);
                }
            }
        }
    }

    /**
     * Recalculates properties of components inside the
     * document according with current zoom
     */
    protected abstract void zoom();

    public void changeActivityState() {
        setState(ACTIVE + NON_ACTIVE - state);
    }

    private void setState(int newState) {
        state = newState;
        zoomDocumentPanel();
    }

    protected void zoomDocumentPanel() {
        int curHeight = this.getHeight();
        int curWidth = this.getWidth();

        double fontSize = 1.0 * curHeight / owner.getThumbnailHeight() * 4;
        activeBorder.setTitleFont(new Font("Dialog", Font.PLAIN, (int) fontSize));
        inactiveBorder.setTitleFont(new Font("Dialog", Font.PLAIN, (int) fontSize));

        if (state == ACTIVE) {
            this.setBorder(activeBorder);
            if (curHeight <= owner.getMinLength() || curWidth < owner.getMinLength()) {
                documentPanel.setVisible(false);
            } else if (curHeight <= owner.getThumbnailHeight() || curWidth <= owner.getThumbnailWidth()) {
                documentPanel.setVisible(false);
            } else {
                documentPanel.setVisible(true);
                Insets i = getInsets();
                documentPanel.setBounds(i.left, i.top, curWidth - i.left - i.right, curHeight - i.top - i.bottom);
                documentPanel.doLayout();
            }
        } else {
            this.setBorder(inactiveBorder);
            Insets i = getInsets();
            documentPanel.setVisible(true);
            documentPanel.setBounds(i.left, i.top, curWidth - i.left - i.right, curHeight - i.top - i.bottom);
            documentPanel.doLayout();
            documentPanel.setVisible(false);
        }
        updateUI();
    }

    public void gotZIFocus() {
        super.gotZIFocus();
        changeActivityState();
    }

    public void lostZIFocus() {
        super.lostZIFocus();
        changeActivityState();
    }


    public void componentResized(ComponentEvent e) {
        zoomDocumentPanel();
        zoom();
    }

    public void componentMoved(ComponentEvent e) {
        // do nothing
    }

    public void componentShown(ComponentEvent e) {
        // do nothing
    }

    public void componentHidden(ComponentEvent e) {
        // do nothing
    }
}
