package core.interfaces;

import core.exceptions.IllegalComponentException;

public interface STBEdge<T extends STBTerminal> extends STBComponent {

    void setFirstEndpoint(T terminal) throws IllegalComponentException;
    T getFirstEndpoint();

    void setSecondEndpoint(T terminal) throws IllegalComponentException;
    T getSecondEndpoint();
}
