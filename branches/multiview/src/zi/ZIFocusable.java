package zi;

/**
 * @author BlackboX
 */
public interface ZIFocusable {
    public void gotZIFocus();
    public void lostZIFocus();
    public void giveUpMouse();
    public void takeBackMouse();
    public boolean canTakeFocus();
}
