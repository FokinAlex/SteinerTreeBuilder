package core.interfaces;

public interface STBEdge<T extends STBTerminal> extends STBComponent {

    void setFirstEndpoint(T terminal);
    T getFirstEndpoint();

    void setSecondEndpoint(T terminal);
    T getSecondEndpoint();

}
