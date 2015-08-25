package storage;

/**
 * Serializability of a ZI object is enabled by the {@link Logger} class.
 * The serialization interface serves to identify the semantics of being
 * serializable as well as provides methods for saving / setting up
 * characteristic features of ZI World's objects.
 * <p/>
 * Because the Visitor object has one principal function (manifested in
 * a plurality of specialized methods) and that function is called <code>visit()</code>,
 * the Visitor can be readily identified as a potential function object or functor.
 * Likewise, the <code>accept()</code> function can be identified as a function applicator,
 * a mapper, which knows how to traverse a particular type of object and apply
 * a function to its elements.
 * <p/>
 * Author: www
 */
public interface ISerializable {
    /**
     * Calls back the {@link ISerializer}'s <code>visit()</code> method for corresponding class so that
     * separate concrete visitor classes can then be written that perform some particular operations.
     *
     * @param serializer visitor instance.
     */
    void accept(ISerializer serializer);
}