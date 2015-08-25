package zi;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

/**
 * Author: Olga Komaleva
 * Date: Feb 18, 2007
 */
public class ZIGlassPane extends JComponent {
    private static ZIGlassPane glassPane = null;
    
    public static ZIGlassPane get() {
        if (glassPane == null) {
            glassPane = new ZIGlassPane();
        }
        return glassPane;
    }
}


