package core.interfaces;

public interface STBTerminal<T extends STBLocation> extends STBComponent, Cloneable{

    long getId();
    T getLocation();
}
