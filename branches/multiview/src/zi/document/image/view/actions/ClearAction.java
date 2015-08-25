package zi.document.image.view.actions;

import zi.document.image.view.ImageRenderer;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Fedor Tsarev
 * Date: 24.02.2007
 * Time: 20:26:53
 * 
 */
public class ClearAction extends AbstractAction {

    private final ImageRenderer imageRenderer;

    public ClearAction(ImageRenderer pd) {
        putValue(NAME, "Clear");
        imageRenderer = pd;

    }


    public void actionPerformed(ActionEvent e) {
        imageRenderer.clear();
    }
}
