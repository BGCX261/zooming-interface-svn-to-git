package storage;

import storage.ui.OpenFileAction;
import storage.ui.PlayPauseAction;
import storage.ui.ResetAction;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Bar with player controls.
 * <p/>
 * Author: www
 */
class ReplayControlBar extends JPanel {
    private final static int STATE_INITIAL = 0;
    private final static int STATE_RUNNING = 1;
    private final static int STATE_PAUSED = 2;
    private final static int STATE_SEEKING = 3;
    private final static int STATE_FINISHED = 4;
    private int state;
    private Point cursor;
    private int speed = 1;
    private int pos = 0;
    private double value = -1;

    private static final Cursor LEFT_CLICK = createLeftCursor();
    private static final Cursor RIGHT_CLICK = createRightCursor();
    private static final Cursor INVISIBLE = Toolkit.getDefaultToolkit().createCustomCursor((new ImageIcon(new byte[]{})).getImage(), new Point(16, 16), "");

    private JEditorPane statusPane;
    private EventBar eventBar;
    private final Component main;
    private Playback player;
    private Action open;
    private Action reset;
    private Action play;

    private final Playback.IExchange exchange = new Playback.IExchange() {
        public boolean isRunning() {
            return (state == STATE_RUNNING) || (state == STATE_SEEKING);
        }

        public void notifyPosition(final int newPos) {
            if (isRunning()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        pos = newPos;
                        eventBar.repaint();
                    }
                });
            }
        }

        public void cursorMoved(Point p) {
            cursor = p;
        }

        public void done() {
            setNewState(STATE_FINISHED);
        }

        public int getSpeed() {
            return speed;
        }


        public void leftClick() {
            if (state != STATE_SEEKING) {
                main.setCursor(LEFT_CLICK);
            }
        }

        public void rightClick() {
            if (state != STATE_SEEKING) {
                main.setCursor(RIGHT_CLICK);
            }
        }

        public void mouseReleased() {
            if (state != STATE_SEEKING) {
                main.setCursor(Cursor.getDefaultCursor());
            }
        }
    };

    private class EventBar extends JComponent {
        private final Color LIGHT_YELLOW = new Color(248, 255, 168);
        private final Color LIGHT_GREEN_DARKEN_X6 = new Color(94, 197, 9);
        private final Color ORANGE_DARKEN_X2 = new Color(255, 197, 130);
        private final Color LIGHT_BLUE = new Color(166, 229, 255);
        private final Color ALPHA_RED = new Color(255, 0, 0, 150);
        private final Color LIGHT_PURPLE = new Color(177, 105, 255, 150);

        private Object[][] events;
        private BufferedImage buffer;
        private Timer timer;

        public EventBar(Object[][] events) {
            this.events = events;

            addMouseListener(new MouseAdapter() {
                /**
                 * Invoked when the mouse has been clicked on a component.
                 */
                public void mouseClicked(MouseEvent e) {
                    value = (double) e.getPoint().x / getWidth();

                    if ((timer != null) && timer.isRunning()) {
                        timer.stop();
                    }
                    timer = new Timer(1000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            setNewState(STATE_SEEKING);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();

                    repaint();
                }
            });
        }

        int getValue() {
            return (int) (value * events.length);
        }

        protected void paintComponent(Graphics g) {
            double height = getHeight() - 1;
            double x;
            float thickness = (float) getWidth() / (events.length + 1);
            Stroke single = new BasicStroke(thickness);
            if (buffer == null) {
                buffer = new BufferedImage(getWidth(), getHeight(), 2);
                Graphics2D g2 = buffer.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Stroke doubly = new BasicStroke(2.0f * thickness);
                g2.setStroke(single);

                Color c;
                boolean doblyThickness = false;

                for (int i = 0; i < events.length; i++) {
                    switch ((Integer) events[i][1]) {
                        case MouseEvent.MOUSE_MOVED:
                            c = LIGHT_YELLOW;
                            break;
                        case MouseEvent.MOUSE_DRAGGED:
                            c = LIGHT_BLUE;
                            break;
                        case MouseEvent.MOUSE_PRESSED:
                        case MouseEvent.MOUSE_RELEASED:
                            c = LIGHT_GREEN_DARKEN_X6;
                            doblyThickness = true;
                            break;
                        case MouseEvent.MOUSE_WHEEL:
                            c = ORANGE_DARKEN_X2;
                            break;
                        default:
                            c = LIGHT_YELLOW;
                            break;
                    }
                    g2.setColor(c);
                    x = (double) i * thickness;
                    if (doblyThickness) {
                        g2.setStroke(doubly);
                    }
                    g2.draw(new Line2D.Double(x, 0, x, height));
                    if (doblyThickness) {
                        g2.setStroke(single);
                        doblyThickness = false;
                    }
                }
                g2.dispose();
                g.drawImage(this.buffer, 0, 0, null);
                g.drawImage(buffer, 0, 0, null);
            } else {
                g.drawImage(buffer, 0, 0, null);

                if (value != -1) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setStroke(new BasicStroke(10.0f * thickness));
                    x = value * thickness * events.length;
                    g2.setColor(ALPHA_RED);
                    g2.draw(new Line2D.Double(x, 0, x, height));
                }

                if (pos > 0) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(LIGHT_PURPLE);
                    g2.fill(new Rectangle2D.Double(0, 0, (double) getWidth() * pos / events.length, height + 1));
                }
            }
        }
    }

    public ReplayControlBar(final Component main, final Playback player) {
        this.main = main;
        this.player = player;
        setLayout(new FlowLayout());

        statusPane = new JEditorPane();
        statusPane.setEditable(false);
        statusPane.setEditorKit(new HTMLEditorKit());

        statusPane.setBackground(this.getBackground());
        Dimension size = new Dimension(240, 50);
        statusPane.setPreferredSize(size);
        statusPane.setMinimumSize(size);

        eventBar = new EventBar(player.getEventsData());
        final Dimension eventBarSize = new Dimension(690, 50);
        eventBar.setPreferredSize(eventBarSize);
        eventBar.setMinimumSize(eventBarSize);

        final JSlider speed = new JSlider(JSlider.VERTICAL, 1, 5, 1);
        speed.setToolTipText("Speed");
        speed.setPaintTicks(true);
        speed.setMajorTickSpacing(2);
        speed.setMinorTickSpacing(1);
        speed.setPaintLabels(true);
        speed.setSnapToTicks(true);
        speed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ReplayControlBar.this.speed = speed.getValue();
            }
        });
        Dimension speedSliderSize = new Dimension(50, 50);
        speed.setPreferredSize(speedSliderSize);
        speed.setMinimumSize(speedSliderSize);

        add(speed);
        add(statusPane);
        add(eventBar);

        open = new OpenFileAction() {
            public void processFile(String filename) {
                if (new File(filename).exists()) {
                    reset(filename);
                    remove(eventBar);
                    eventBar = new EventBar(player.getEventsData());
                    eventBar.setPreferredSize(eventBarSize);
                    eventBar.setMinimumSize(eventBarSize);
                    add(eventBar);
                    setNewState(STATE_INITIAL);
                } else {
                    JOptionPane.showMessageDialog(null, "File doesn't exists:\n " + filename, "Open XML file", JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        reset = new ResetAction() {
            public void actionPerformed(ActionEvent e) {
                value = -1;
                reset(null);
                setNewState(STATE_INITIAL);
            }
        };

        play = new PlayPauseAction() {
            public void actionPerformed(ActionEvent e) {
                switch (state) {
                    case STATE_INITIAL:
                        setNewState(STATE_RUNNING);
                        new Thread(new Runnable() {
                            public void run() {
                                player.replayRecord(0, player.getEventsCount(), exchange, cursor);
                            }
                        }).start();
                        break;
                    case STATE_RUNNING:
                        setNewState(STATE_PAUSED);
                        break;
                    case STATE_PAUSED:
                        setNewState(STATE_RUNNING);
                        new Thread(new Runnable() {
                            public void run() {
                                player.replayRecord(pos, player.getEventsCount(), exchange, cursor);
                            }
                        }).start();
                        break;
                    case STATE_FINISHED:
                        break;
                }
            }
        };

        setNewState(STATE_INITIAL);
    }

    private void reset(String filename) {
        for (JPanel viewport : player.getViewports()) {
            viewport.removeAll();
        }
        player.reloadViewports(filename);
        for (JPanel viewport : player.getViewports()) {
            viewport.repaint();
        }
        pos = 0;
        eventBar.repaint();
    }

    private void setNewState(int newState) {
        switch (newState) {
            case STATE_INITIAL:
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        statusPane.setText("<html>"
                                + "<font color=\"blue\" face=\"Helvetica,Arial,sans-serif\"><b>R</b></font>"
                                + "<font face=\"Helvetica,Arial,sans-serif\"> = reset</font>, "
                                + "<font color=\"blue\" face=\"Helvetica,Arial,sans-serif\"><b>P</b></font>"
                                + "<font face=\"Helvetica,Arial,sans-serif\"> = play / pause</font><br>"
                                + "<font size=\"-2\" face=\"Helvetica,Arial,sans-serif\">" + "Use slider to specify speed and spectrum bar to specify playing position" + "</font>"
                                + "</html>");
                        statusPane.repaint();
                    }
                });
                break;
            case STATE_RUNNING:
                setStatus("RUNNING");
                break;
            case STATE_PAUSED:
                setStatus("PAUSED");
                break;
            case STATE_FINISHED:
                setStatus("FINISHED");
                break;
            case STATE_SEEKING:
                setStatus("SEEKING");
                if (pos != 0) {
                    reset(null);
                }
                main.setCursor(INVISIBLE);
                final int oldSpeed = speed;
                speed = -1;
                new Thread(new Runnable() {
                    public void run() {
                        player.replayRecord(0, eventBar.getValue(), exchange, cursor);
                        speed = oldSpeed;
                        main.setCursor(Cursor.getDefaultCursor());
                        setNewState(STATE_PAUSED);
                    }
                }).start();
                break;
        }
        state = newState;
    }

    private void setStatus(final String status) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                statusPane.setText("<html>"
                        + "<font color=\"blue\" face=\"Helvetica,Arial,sans-serif\"><b>R</b></font>"
                        + "<font face=\"Helvetica,Arial,sans-serif\"> = reset</font>, "
                        + "<font color=\"blue\" face=\"Helvetica,Arial,sans-serif\"><b>P</b></font>"
                        + "<font face=\"Helvetica,Arial,sans-serif\"> = play / pause</font><br>"
                        + "<font size=\"+2\" color=\"red\" face=\"Helvetica,Arial,sans-serif\"><b>" + status + "</b></font>"
                        + "</html>");
                statusPane.repaint();
            }
        });
    }

    private static Cursor createLeftCursor() {
        ImageIcon r = new ImageIcon(new byte[]{(byte) 137, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 32, 0, 0, 0, 32, 8, 3, 0, 0, 0, 68, (byte) 164, (byte) 138, (byte) 198, 0, 0, 0, 3, 115, 66, 73, 84, 8, 8, 8, (byte) 219, (byte) 225, 79, (byte) 224, 0, 0, 0, (byte) 180, 80, 76, 84, 69, 0, 0, 0, (byte) 181, (byte) 180, (byte) 179, 0, (byte) 255, 0, (byte) 169, (byte) 168, (byte) 166, 90, 88, 85, (byte) 255, (byte) 255, (byte) 255, (byte) 222, 24, 8, 59, 56, 52, (byte) 136, (byte) 135, (byte) 133, (byte) 212, (byte) 211, (byte) 210, 41, 39, 35, 36, 33, 29, 112, 111, 108, 74, 72, 68, (byte) 232, (byte) 232, (byte) 232, (byte) 153, (byte) 153, (byte) 153, (byte) 194, (byte) 193, (byte) 192, 83, 81, 78, 47, 44, 40, 124, 122, 119, (byte) 248, (byte) 248, (byte) 247, (byte) 204, (byte) 204, (byte) 204, 64, 61, 58, (byte) 223, (byte) 222, (byte) 222, 97, 94, 92, (byte) 161, (byte) 160, (byte) 158, (byte) 147, (byte) 145, (byte) 143, 68, 65, 62, 51, 51, 51, 77, 74, 71, (byte) 239, (byte) 239, (byte) 239, 99, 97, 94, (byte) 191, (byte) 190, (byte) 189, (byte) 198, (byte) 197, (byte) 196, (byte) 128, 126, 124, (byte) 174, (byte) 173, (byte) 172, (byte) 142, (byte) 141, (byte) 139, (byte) 153, (byte) 153, (byte) 153, 86, 84, 81, 118, 116, 113, 48, 46, 42, 66, 64, 60, (byte) 230, (byte) 230, (byte) 222, (byte) 168, (byte) 167, (byte) 165, 41, 41, 41, 102, 102, 102, (byte) 197, (byte) 197, (byte) 189, 124, 123, 120, 39, 36, 32, (byte) 138, (byte) 137, (byte) 134, 84, 82, 79, (byte) 216, (byte) 215, (byte) 215, 79, 76, 73, 114, 112, 110, 94, 91, 89, (byte) 181, (byte) 173, (byte) 173, 71, 68, 65, (byte) 171, (byte) 170, (byte) 168, (byte) 162, (byte) 161, (byte) 159, (byte) 130, (byte) 128, 126, 99, 89, (byte) 173, 91, 0, 0, 0, 60, 116, 82, 78, 83, (byte) 255, (byte) 255, 0, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255, 17, (byte) 129, (byte) 178, (byte) 184, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 11, 18, 0, 0, 11, 18, 1, (byte) 210, (byte) 221, 126, (byte) 252, 0, 0, 0, 22, 116, 69, 88, 116, 67, 114, 101, 97, 116, 105, 111, 110, 32, 84, 105, 109, 101, 0, 48, 51, 47, 49, 57, 47, 48, 55, (byte) 241, 57, 14, 100, 0, 0, 0, 32, 116, 69, 88, 116, 83, 111, 102, 116, 119, 97, 114, 101, 0, 77, 97, 99, 114, 111, 109, 101, 100, 105, 97, 32, 70, 105, 114, 101, 119, 111, 114, 107, 115, 32, 77, 88, (byte) 187, (byte) 145, 42, 36, 0, 0, 1, 24, 73, 68, 65, 84, 120, (byte) 156, (byte) 149, (byte) 211, (byte) 209, 114, (byte) 130, 48, 16, 5, 80, 80, 18, 74, 110, 43, 20, 17, 16, (byte) 173, (byte) 136, 117, 48, (byte) 210, (byte) 135, (byte) 150, 10, (byte) 142, (byte) 218, (byte) 255, (byte) 255, (byte) 175, 118, 90, (byte) 165, (byte) 155, 4, (byte) 219, (byte) 241, 62, 102, (byte) 207, 76, (byte) 146, (byte) 217, 93, 107, (byte) 240, 79, (byte) 172, (byte) 155, 64, (byte) 208, 54, 115, (byte) 183, 92, 121, 87, 64, 49, (byte) 131, (byte) 220, (byte) 199, (byte) 243, 82, (byte) 192, 73, (byte) 250, (byte) 192, 29, 98, (byte) 206, 121, (byte) 250, (byte) 192, 70, 126, 40, (byte) 142, 38, (byte) 248, 64, (byte) 203, 127, 0, 99, 85, (byte) 137, (byte) 163, 14, 124, (byte) 140, 121, 7, 88, (byte) 228, (byte) 160, 80, 65, 34, (byte) 246, (byte) 156, 0, (byte) 182, (byte) 136, 99, 21, (byte) 164, (byte) 146, 43, (byte) 128, 5, 88, 83, (byte) 176, (byte) 195, 90, 3, 44, (byte) 151, 20, (byte) 188, 10, (byte) 174, (byte) 131, 0, 67, 2, (byte) 220, (byte) 173, 117, 78, 115, (byte) 168, (byte) 207, 9, 83, 2, (byte) 208, 23, (byte) 249, 11, 108, 20, (byte) 145, 17, 31, 85, 7, 78, 72, (byte) 152, 17, 27, 94, 7, 50, 44, 76, 80, 96, (byte) 249, 55, 8, 16, 116, (byte) 224, (byte) 165, (byte) 239, (byte) 138, 103, 114, (byte) 197, 18, (byte) 133, 9, 124, (byte) 208, 111, 102, 38, (byte) 200, 93, 2, 14, (byte) 142, 81, (byte) 143, 100, 75, 64, 109, 62, (byte) 194, (byte) 134, 77, (byte) 187, (byte) 249, (byte) 182, (byte) 210, (byte) 193, 99, (byte) 172, (byte) 180, (byte) 187, 21, (byte) 158, (byte) 254, (byte) 135, (byte) 169, 58, 81, (byte) 243, (byte) 167, (byte) 136, (byte) 214, 43, (byte) 217, 104, 35, 103, 35, 37, 98, 52, 11, 119, 26, 24, 76, (byte) 191, (byte) 134, (byte) 253, 82, (byte) 159, 108, (byte) 197, 101, 36, (byte) 201, (byte) 216, 15, (byte) 195, (byte) 176, (byte) 254, 38, (byte) 147, 13, (byte) 220, (byte) 174, (byte) 174, 108, 86, 14, (byte) 184, 101, 35, 33, (byte) 198, (byte) 228, 80, (byte) 221, (byte) 205, (byte) 236, (byte) 254, (byte) 221, (byte) 217, (byte) 156, (byte) 148, (byte) 163, (byte) 219, (byte) 150, (byte) 183, 47, (byte) 159, (byte) 165, (byte) 232, 31, 101, (byte) 184, 8, 109, (byte) 232, 0, 0, 0, 0, 73, 69, 78, 68, (byte) 174, 66, 96, (byte) 130,});
        return Toolkit.getDefaultToolkit().createCustomCursor(r.getImage(), new Point(16, 16), "");
    }

    private static Cursor createRightCursor() {
        ImageIcon r = new ImageIcon(new byte[]{71, 73, 70, 56, 57, 97, 32, 0, 32, 0, (byte) 213, 0, 0, 0, 0, 0, (byte) 181, (byte) 180, (byte) 179, 0, (byte) 255, 0, (byte) 169, (byte) 168, (byte) 166, 90, 88, 85, (byte) 222, 24, 8, (byte) 255, (byte) 255, (byte) 255, 59, 56, 52, (byte) 136, (byte) 135, (byte) 133, (byte) 223, (byte) 222, (byte) 222, 41, 39, 35, 36, 33, 29, 112, 111, 108, 74, 72, 68, (byte) 204, (byte) 204, (byte) 204, (byte) 153, (byte) 153, (byte) 153, (byte) 229, (byte) 228, (byte) 228, (byte) 194, (byte) 193, (byte) 192, 83, 81, 78, 47, 44, 40, 124, 122, 119, (byte) 239, (byte) 239, (byte) 239, 64, 61, 58, 97, 94, 92, (byte) 161, (byte) 160, (byte) 158, (byte) 147, (byte) 145, (byte) 143, (byte) 212, (byte) 211, (byte) 210, 68, 65, 62, 51, 51, 51, 77, 74, 71, (byte) 248, (byte) 248, (byte) 247, 97, 95, 92, (byte) 191, (byte) 190, (byte) 189, (byte) 198, (byte) 197, (byte) 196, (byte) 128, 126, 124, (byte) 174, (byte) 173, (byte) 172, (byte) 142, (byte) 141, (byte) 139, (byte) 153, (byte) 153, (byte) 153, 86, 84, 81, 118, 116, 113, 48, 46, 42, 66, 64, 60, (byte) 168, (byte) 167, (byte) 165, 41, 41, 41, 124, 123, 120, 102, 102, 102, 39, 36, 32, (byte) 138, (byte) 137, (byte) 134, 84, 82, 79, (byte) 216, (byte) 215, (byte) 215, 79, 76, 73, 114, 112, 110, 94, 91, 89, (byte) 181, (byte) 173, (byte) 173, (byte) 197, (byte) 197, (byte) 189, 71, 68, 65, (byte) 171, (byte) 170, (byte) 168, (byte) 162, (byte) 161, (byte) 159, (byte) 130, (byte) 128, 126, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, (byte) 249, 4, 5, 20, 0, 2, 0, 44, 0, 0, 0, 0, 32, 0, 32, 0, 0, 6, (byte) 255, 64, (byte) 129, 112, 72, 44, 26, (byte) 143, (byte) 200, (byte) 164, 114, (byte) 169, (byte) 140, (byte) 204, 100, (byte) 156, (byte) 131, 73, (byte) 164, 97, 42, 67, (byte) 155, 5, (byte) 138, 102, (byte) 225, (byte) 152, 20, 11, 66, (byte) 194, 106, 68, 44, 44, 56, (byte) 143, (byte) 225, (byte) 210, 40, (byte) 184, 39, 10, 28, 121, (byte) 168, 91, (byte) 204, 42, (byte) 134, 60, (byte) 219, (byte) 237, 54, 45, (byte) 228, 100, 15, 11, 20, 121, (byte) 133, 123, 124, 5, 4, 11, 33, 86, 9, 10, 52, (byte) 133, (byte) 134, 109, (byte) 136, 5, 22, 22, 86, 31, 40, 16, (byte) 145, 122, (byte) 147, (byte) 148, 11, 36, 75, 49, (byte) 160, (byte) 156, (byte) 157, (byte) 148, 124, 40, 75, 44, 10, (byte) 155, (byte) 165, (byte) 135, (byte) 167, 11, 3, 74, 7, 41, 47, (byte) 182, (byte) 183, (byte) 182, 50, 55, 0, (byte) 188, (byte) 189, (byte) 188, 19, 23, 74, 11, (byte) 195, (byte) 196, (byte) 197, (byte) 198, (byte) 199, (byte) 169, 72, 1, (byte) 139, 30, (byte) 205, (byte) 206, (byte) 207, (byte) 208, (byte) 206, (byte) 130, 16, 72, 57, 11, 9, (byte) 165, (byte) 217, (byte) 156, (byte) 203, 85, 71, 25, 11, 120, (byte) 218, (byte) 226, 33, 11, 32, 72, (byte) 223, (byte) 225, (byte) 226, (byte) 217, 17, 11, 17, 72, 42, (byte) 215, (byte) 234, (byte) 218, 35, 11, (byte) 221, 70, 32, (byte) 139, (byte) 242, (byte) 217, (byte) 130, (byte) 194, 25, (byte) 250, (byte) 165, 45, 14, 40, (byte) 185, 65, (byte) 224, 95, 36, 15, 40, 102, 40, 121, 17, (byte) 207, (byte) 160, (byte) 129, 101, 1, (byte) 150, (byte) 184, 16, (byte) 225, (byte) 208, (byte) 128, (byte) 132, 75, 75, 102, 40, (byte) 208, 96, (byte) 144, 30, 6, 43, 28, 58, (byte) 168, (byte) 145, 7, 1, (byte) 133, 12, 50, (byte) 203, 62, (byte) 140, (byte) 212, 6, 97, (byte) 195, (byte) 132, 24, 115, 48, (byte) 132, (byte) 193, (byte) 150, (byte) 205, 65, 10, 5, (byte) 140, (byte) 230, 8, 24, 48, 97, (byte) 194, 11, 33, (byte) 154, 121, 28, (byte) 156, 88, 112, 32, (byte) 167, 78, 33, 31, (byte) 134, 73, (byte) 145, (byte) 129, 98, (byte) 129, 2, 10, 71, (byte) 189, 49, (byte) 128, 65, (byte) 224, 68, (byte) 142, (byte) 168, 88, (byte) 179, 102, 13, 2, 0, 59});
        return Toolkit.getDefaultToolkit().createCustomCursor(r.getImage(), new Point(16, 16), "");
    }

    Action getOpenFileAction() {
        return open;
    }

    Action getResetAction() {
        return reset;
    }

    Action getPlayAction() {
        return play;
    }
}