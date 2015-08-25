import storage.Logger;
import zi.ZIController;
import zi.ZIGlassPane;
import zi.ZIListener;
import zi.baseElements.RelLocation;
import zi.implementation.InformationPane;
import zi.implementation.ZIContainerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainForm extends JFrame {

    public void dispose() {
        super.dispose();
    }

    MainForm() {
        this.setSize(600, 600);
        double relX, relY, relWidth, relHeight;
        JComponent left = new JPanel();
        JComponent right = new JPanel();
        //left.setSize(300, 600);
        //right.setSize(300,600);
        //left.setLocation(0, 0);
        //right.setLocation(300, 0);
        //left.setVisible(true);
        //right.setVisible(true);
        //this.getContentPane().add(left);
        //this.getContentPane().add(right);
        
        ZIContainerImpl parent;
        ZIContainerImpl cont;
        ZIContainerImpl oldcont;

        int minLength = 10;
        int maxLength = ZIContainerImpl.INFINITE_ZOOMING;

        getContentPane().setSize(600, 600);
        final ZIController controller = new ZIController(this.getContentPane());
        ZIListener listener = new ZIListener(controller);
        InformationPane.get().addZIListener(listener);
        oldcont = new ZIContainerImpl(InformationPane.get(), minLength, maxLength,
                Color.GREEN, new RelLocation(0, 0, 0.5, 0.5));
        oldcont.addZIListener(listener);
        InformationPane.get().getContainer().add(oldcont);

        for (int i = 0; i < 10; i++) {
            parent = oldcont;
            relX = Math.random() * 0.5;
            relY = Math.random() * 0.5;
            relWidth = Math.random() * (1 - relX);
            relHeight = Math.random() * (1 - relY);
            float f[] = new float[3];
            Color.RGBtoHSB((int) (6000 * Math.random()),
                    (int) (6000 * Math.random()), (int) (6000 * Math.random()), f);
            Color color = Color.getHSBColor(f[0], f[1], f[2]);
            cont = new ZIContainerImpl(parent, minLength, maxLength,
                    color, new RelLocation(relX, relY, relWidth, relHeight));
            oldcont.getContainer().add(cont);
            cont.addZIListener(listener);
            oldcont = cont;
        }

        oldcont = new ZIContainerImpl(InformationPane.get(), minLength, maxLength,
                Color.BLACK, new RelLocation(0.6, 0.6, 0.3, 0.2));
        oldcont.addZIListener(listener);
        InformationPane.get().getContainer().add(oldcont);

        for (int i = 0; i < 10; i++) {
            parent = oldcont;
            relX = Math.random() * 0.5;
            relY = Math.random() * 0.5;
            relWidth = Math.random() * (1 - relX);
            relHeight = Math.random() * (1 - relY);
            float f[] = new float[3];
            Color.RGBtoHSB((int) (6000 * Math.random()), (int) (6000 * Math.random()), (int) (6000 * Math.random()), f);
            Color color = Color.getHSBColor(f[0], f[1], f[2]);
            cont = new ZIContainerImpl(parent, minLength, maxLength, color, new RelLocation(relX, relY, relWidth, relHeight));
            oldcont.getContainer().add(cont);
            cont.addZIListener(listener);
            oldcont = cont;

        }
        this.setGlassPane(ZIGlassPane.get());

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setVisible(true);

        ZIGlassPane.get().setSize(this.getWidth(), this.getHeight());
        ZIGlassPane.get().setLocation(0, 0);
        ZIGlassPane.get().setVisible(true);

        // Iconify this window to get XML dump
        final Logger LOG = new Logger();
        LOG.startRecording();
        addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is iconified.
             */
            public void windowIconified(WindowEvent e) {
                LOG.writeXML("sample.xml");
            }
        });
    }

    public static void main(String[] args) {
        new MainForm().setVisible(true);
    }
}
