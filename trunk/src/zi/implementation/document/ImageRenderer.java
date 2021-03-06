package zi.implementation.document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 25.02.2007
 * Time: 12:07:56
 */
public class ImageRenderer extends JComponent implements MouseListener, MouseMotionListener {

    protected int lastX;
    protected int lastY;

    protected Image image;
    protected int imageHeight;
    protected int imageWidth;

    private void init() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
    }

    public ImageRenderer(Image image, int imageWidth, int imageHeight) {
        init();
        this.image = image;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), new ImageObserver() {
            public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                return false;
            }
        });
    }

    public void clear() {
        image.getGraphics().clearRect(0, 0, imageWidth, imageHeight);
        this.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        double scaleX = 1.0 * imageWidth / this.getWidth();
        double scaleY = 1.0 * imageHeight / this.getHeight();

        lastX = (int) (e.getX() * scaleX);
        lastY = (int) (e.getY() * scaleY);
    }

    public void mouseDragged(MouseEvent e) {
        double scaleX = 1.0 * imageWidth / this.getWidth();
        double scaleY = 1.0 * imageHeight / this.getHeight();

        int x = (int) (e.getX() * scaleX);
        int y = (int) (e.getY() * scaleY);

        image.getGraphics().drawLine(lastX, lastY, x, y);

        lastX = x;
        lastY = y;

        this.repaint();

    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public boolean isFocusable() {
       return true;
   }

}
