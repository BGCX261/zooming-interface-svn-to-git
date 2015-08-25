package zi.models.actions;

import zi.models.ZIContainer;
import zi.models.RelLocation;
import zi.models.ZIInformationPlane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Author: Olga Komaleva
 * Date: Apr 16, 2007
 */
public class CreateContainerAction extends AbstractAction {
        private final ZIContainer container;

        public CreateContainerAction(ZIContainer container) {
            putValue(NAME, "add");
            this.container = container;
        }

        public void actionPerformed(ActionEvent e) {
            double relWidth = Math.random()*0.5;
            double relHeight = Math.random()*0.5;
            double relX = Math.random()*(1 - relWidth);
            double relY = Math.random()*(1 - relHeight);

            container.add(new ZIContainer(container, 10, -1,
                    new RelLocation(relX, relY, relWidth, relWidth/relHeight), Color.green));
        }
    }
