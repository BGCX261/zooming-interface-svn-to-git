package classeditor.ui.packagecontainer.view;

import classeditor.ui.packagecontainer.model.PackageContainer;
import zi.views.ZIContainerView;
import zi.ZIController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 16.04.2007
 * Time: 14:19:58
 */
public class PackageContainerView extends ZIContainerView {

    protected PackageContainer owner;
    private JLabel label;

    public PackageContainerView(PackageContainer container, ZIController controller) {
        super(container, controller);
        this.owner = container;
        this.color = owner.getColor();
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(0.5f));

        int radius = Math.min(Math.min(15, getHeight() / 4), getWidth() / 4);

        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g.setColor(color);

        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g.drawLine(radius, radius, getWidth() - radius, getHeight() - radius);
        g.drawLine(radius, getHeight() - radius, getWidth() - radius, radius);

        int curWidth = this.getWidth();
        if (label == null) {
            label = new JLabel(owner.getPackageName());
        }
        label.setSize(curWidth, 15);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.paint(g.create(0, 0, curWidth, 15));
    }
}
