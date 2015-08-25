package zi.baseElements;

import zi.ZIListener;

/**
 * Author: Olga Komaleva
 * Date: Feb 26, 2007
 */
public interface ZIContainer {
    public ZIContainerAdapter getContainer();
    public void addZIListener(ZIListener listener);
    public void changeActivityState();
}
