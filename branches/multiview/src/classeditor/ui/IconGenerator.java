package classeditor.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Date: 19.04.2007
 * Time: 17:57:52
 *
 * @author Fedor Tsarev
 */
public class IconGenerator {

    private static IconGenerator instance;

    public static IconGenerator getInstance() {
        if (instance == null) {
            instance = new IconGenerator();
        }
        return instance;
    }

    public ImageIcon getCreateClassIcon() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 16, 16);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Dialog", Font.BOLD, 10));
        g.drawString("C", 2, 12);
        return new ImageIcon(image);
    }

}
