package zi.models;

/**
 * Author: Olga Komaleva
 * Date: Apr 17, 2007
 */
public class Arrow {
    public final ZIItem parent;
    public final ZIItem child;

    public Arrow(ZIItem parent, ZIItem child) {
        this.parent = parent;
        this.child = child;
    }
}

