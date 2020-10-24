package auto.util.wrapper;

/**
 * {@code Wrapper} is a proxy class that can substitute the concrete class group.
 * <p>
 * The class belongs to the group can have same responsibility with the other class implementing incompatible interface.
 * <p>
 *
 * @param <T> the real type of wrapper class.
 */
public abstract class Wrapper<T> {

    private T data;

    protected Wrapper(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}
