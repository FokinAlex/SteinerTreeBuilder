package core.interfaces;

public interface STBPage<T extends STBGraph> {

    T getData();
    boolean setData(T data);

}
