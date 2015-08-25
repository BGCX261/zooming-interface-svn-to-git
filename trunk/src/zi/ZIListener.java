package zi;

import zi.baseElements.Location;
import zi.baseElements.ZICompoundItem;
import zi.baseElements.ZIContainer;
import zi.implementation.InformationPane;
import zi.implementation.ZIContainerImpl;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;

/**
 * Author: Olga Komaleva
 * Date: Feb 25, 2007
 */
public class ZIListener implements MouseWheelListener, MouseMotionListener, MouseListener {
    private State currentState = new State();
    private ContainerAttachedState attachedState = null;
    private ZIController controller;

    public ZIListener(ZIController controller) {
        super();
        this.controller = controller;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        currentState.mouseWheelMoved(e);
    }

    public void mouseClicked(MouseEvent e) {
        currentState.mouseClicked(e);
    }

    public void mousePressed(MouseEvent e) {
        currentState.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        currentState.mouseReleased(e);
        if (attachedState != null) {
            attachedState.mouseReleased(e);
        }
    }

    public void mouseEntered(MouseEvent e) {
        currentState.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        currentState.mouseExited(e);
    }

    public void mouseDragged(MouseEvent e) {
        currentState.mouseDragged(e);
        if (attachedState != null) {
            attachedState.mouseMoved(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        currentState.mouseMoved(e);
        if (attachedState != null) {
            attachedState.mouseMoved(e);
        }
    }

    private void setState(State state) {
        currentState = state;
    }

    class State extends MouseInputAdapter implements MouseWheelListener {

        public void mouseWheelMoved(MouseWheelEvent e) {
            int rotation = e.getWheelRotation();

            if (rotation < 0) {
                controller.zoom(1);
            } else {
                controller.zoom(-1);
            }
        }

        public void mousePressed(MouseEvent e) {
            if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
                setState(new CenterDraggedState());
            }
        }
            
        public void mouseClicked(MouseEvent e) {
            if (((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
                    && ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == InputEvent.CTRL_DOWN_MASK)) {
                ZIContainer container = (ZIContainer) e.getSource();
                if (container != null) {
                    if (container != InformationPane.get()) {
                        container.changeActivityState();
                    }
                }
            } else if (((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
                    && ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != InputEvent.CTRL_DOWN_MASK)) {
                if (e.getSource() != InformationPane.get()) {
                    controller.zoom((ZICompoundItem) e.getSource());
                } else {
                    controller.zoom(e.getPoint());
                }
            } else if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                if ((attachedState == null) && ((e.getSource() != InformationPane.get()) && (e.getSource() != controller.getCurrentContainer()))) {
                    attachedState = new ContainerAttachedState((JComponent) e.getSource(), e.getPoint());
                } else {
                    if (attachedState.attContainer == null) {
                        attachedState = null;
                    }
                }
            }
        }
    }

    class ContainerAttachedState {

        private ZICompoundItem attContainer = null;

        private JComponent shadow = new JComponent() {
            protected void paintComponent(Graphics g) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        public ContainerAttachedState(JComponent container, Point point) {
            attContainer = (ZICompoundItem) container;

            Point p = SwingUtilities.convertPoint(container, point, ZIGlassPane.get());
            attContainer.getItem().getMyParent().getContainer().remove(attContainer);

            ZIGlassPane.get().add(container);
            ZIGlassPane.get().add(shadow);

            shadow.setLocation(p.x + 5, p.y + 5);
            shadow.setSize(container.getWidth(), container.getHeight());
            container.setLocation(p.x + 1, p.y + 1);

            ZIGlassPane.get().repaint();
        }

        public void mouseReleased(MouseEvent e) {
            ZIContainer parent = (ZIContainer) e.getSource();

            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                if ((parent != null) && (parent != attContainer)) {
                    JComponent component = (JComponent) parent;
                    JComponent attComp = (JComponent) attContainer;

                    boolean couldItBeThrownHere;
                    attContainer.getItem().setMyParent(parent);
                    if (parent != InformationPane.get()) {
                        double x = ((double) e.getX()) / component.getWidth();
                        double y = (((double) e.getY())) / component.getHeight();
                        double width = ((double) attComp.getWidth()) / component.getWidth();
                        double height = ((double) attComp.getHeight()) / component.getHeight();
                        couldItBeThrownHere = attContainer.getItem().setRelBorder(x, y, width, height);
                    } else {
                        Location location = InformationPane.get().getRelLocation();
                        double x = (e.getX() - location.getX()) / location.getWidth();
                        double y = (e.getY() - location.getY()) / location.getHeight();
                        double width = attComp.getWidth() / location.getWidth();
                        double height = attComp.getHeight() / location.getHeight();
                        couldItBeThrownHere = attContainer.getItem().setRelBorder(x, y, width, height);
                    }

                    if (couldItBeThrownHere) {
                        ZIGlassPane.get().remove(attComp);
                        ZIGlassPane.get().remove(shadow);
                        parent.getContainer().add(attContainer);
                        ((JComponent) parent).setComponentZOrder(attComp, 0);
                        ZIGlassPane.get().repaint();
                        attContainer = null;
                    }
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            Point p = SwingUtilities.convertPoint((JComponent) e.getSource(), e.getPoint(), ZIGlassPane.get());
            shadow.setLocation(p.x + 5, p.y + 5);
            ((JComponent) attContainer).setLocation(p.x + 1, p.y + 1);
            ZIGlassPane.get().repaint();
        }
    }

    class CenterDraggedState extends State {
        protected int oldX = -1;
        protected int oldY = -1;

        public void mouseDragged(MouseEvent e) {
            Point p = SwingUtilities.convertPoint((JComponent) e.getSource(), e.getPoint(), ZIGlassPane.get());

            if (oldX != -1) {
                controller.moveCenter(p.x - oldX, p.y - oldY);
            }
            oldX = p.x;
            oldY = p.y;
        }

        public void mouseReleased(MouseEvent e) {
            if (((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)
                    && ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != InputEvent.CTRL_DOWN_MASK)) {
                setState(new State());
                //super.mouseReleased(e); // important!
            }
        }
    }
}
