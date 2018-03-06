package core.interfaces;

import core.exceptions.IllegalLocationException;

public interface STBTerminal<T extends STBLocation> extends STBComponent {

    T getLocation();
    void setLocation(T location) throws IllegalLocationException;
}
