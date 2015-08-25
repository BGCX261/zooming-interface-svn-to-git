package testers;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MyComponentListener extends ComponentAdapter {
    public void componentResized(ComponentEvent e) {
        JComponent parent = (JComponent) e.getSource();
        int i = parent.getComponentCount() - 1;
        for (; i >= 0; i--) {
            JComponent comp = (JComponent) parent.getComponent(i);
            comp.setSize(parent.getSize());
        }
    }
}
