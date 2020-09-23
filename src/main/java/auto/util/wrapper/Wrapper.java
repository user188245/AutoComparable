package auto.util.wrapper;

public abstract class Wrapper<T> {

    private T data;

    protected Wrapper(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}
