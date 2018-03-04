package core.interfaces;

public interface STBTerminal<T extends STBLocation> extends STBComponent {

    T getLocation();
    void setLocation(T location);

}
