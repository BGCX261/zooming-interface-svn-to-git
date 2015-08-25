package storage.ui;

import java.util.List;

/**
 * Element, whose state could be not identical to saved one.
 *
 * @author www
 */
public interface ZIFalseElement {
    /**
     * Marks element as correct or not identical to saved one.
     *
     * @param isFalse <code>true</code>, if element's final state doesn't matched achieved one;
     *                <code>false</code> otherwise.
     */
    public void mark(boolean isFalse);

    /**
     * Whether element's final state doesn't matched achieved one.
     *
     * @return <code>true</code>, if element's final state doesn't matched achieved one;
     *         <code>false</code> otherwise.
     */
    public boolean isMarked();

    /**
     * Sets found problems list.
     *
     * @param text list containing found problems description.
     */
    public void setText(List<String> text);

    /**
     * Returns the canonical runtime class of an object.
     *
     * @return The <code>java.lang.Class</code> object that represents
     *         the canonical runtime class of the object.
     */
    public Class getCanonicalClass();
}
