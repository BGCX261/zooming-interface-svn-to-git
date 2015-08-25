package zi.implementation;

import storage.ISerializable;
import zi.ZIListener;
import zi.baseElements.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Abstract document class. Provides basic functionality:
 * drawing titles and supporting active and inactive state.
 */
public abstract class ZIAbstractDocument extends JComponent implements ZICompoundItem, ISerializable {

    private static final int TITLE_HEIGHT = 15;

    protected final static int ACTIVE = 1;
    protected final static int NON_ACTIVE = 0;

    protected int state;

    protected final JPanel documentPanel;

    protected final ZIContainerAdapter container;
    protected final ZIItemAdapter containee;

    protected final int thumbnailWidth;
    protected final int thumbnailHeight;

    protected final int minWidth;
    protected final int maxWidth;

    public ZIAbstractDocument(ZIContainer parent, int minWidth, int maxWidth, RelLocation location, int thumbnailWidth, int thumbnailHeight) {
        containee = new ZIItemAdapter(parent, minWidth, maxWidth, location);
        container = new ZIContainerAdapter(this);

        this.minWidth = minWidth;
        this.maxWidth = maxWidth;

        this.thumbnailHeight = thumbnailHeight;
        this.thumbnailWidth = thumbnailWidth;
        documentPanel = new JPanel();

        this.add(documentPanel, BorderLayout.CENTER);
        documentPanel.setVisible(false);

        setState(NON_ACTIVE);

    }

    protected final void paintComponent(Graphics g) {
        zoom();
        int curHeight = getHeight();
        int curWidth = getWidth();
        switch (state) {
            case ACTIVE: {
                this.setComponentZOrder(documentPanel, 0);
                if (curHeight <= containee.getMinLength() || curWidth < containee.getMinLength()) {
                    documentPanel.setVisible(false);
                } else if (curHeight <= thumbnailHeight || curWidth <= thumbnailWidth) {
                    documentPanel.setVisible(false);
                    g.setColor(Color.RED);
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(Color.YELLOW);
                    g.fillRect(0, 0, getWidth(), TITLE_HEIGHT);

                    g.setColor(Color.BLACK);
                    g.drawString("Active", 0, TITLE_HEIGHT - 2);

                    documentPanel.setVisible(true);
                    documentPanel.setBounds(0, TITLE_HEIGHT, getWidth(), getHeight() - TITLE_HEIGHT);
                    documentPanel.doLayout();
                    super.paintComponent(g);
                }
                break;
            }
            case NON_ACTIVE: {

                documentPanel.setVisible(true);
                documentPanel.setBounds(0, TITLE_HEIGHT, getWidth(), getHeight() - TITLE_HEIGHT);
                documentPanel.doLayout();

                BufferedImage imageShownInInactiveState = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                documentPanel.paintComponents(imageShownInInactiveState.getGraphics());

                documentPanel.setVisible(false);


                g.setColor(Color.BLUE);
                g.fillRect(0, 0, getWidth(), TITLE_HEIGHT);

                g.setColor(Color.WHITE);
                g.drawString("Inactive", 0, TITLE_HEIGHT - 2);

                g.drawImage(imageShownInInactiveState, 0, TITLE_HEIGHT, this.getWidth(), this.getHeight(), new ImageObserver() {
                    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                        return false;
                    }
                });
                break;
            }
        }
        doLayout();
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
//        if (state == ACTIVE) {
//            setLayout(new BorderLayout());
//        } else {
//            setLayout(new ZILayout());
//        }
        repaint();
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public ZIContainerAdapter getContainer() {
        return container;
    }

    public ZIItemAdapter getItem() {
        return containee;
    }

    public void addZIListener(ZIListener listener) {
        addMouseListener(listener);
        addMouseMotionListener(listener);
        addMouseWheelListener(listener);
    }

}
