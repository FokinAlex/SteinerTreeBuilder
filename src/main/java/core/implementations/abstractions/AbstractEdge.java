package core.implementations.abstractions;

import core.exceptions.IllegalComponentException;
import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
import core.types.STBEdgeType;

public abstract class AbstractEdge<Terminal extends STBTerminal> implements STBEdge<Terminal> {

    private Terminal firstEndpoint;
    private Terminal secondEndpoint;
    public STBEdgeType type = STBEdgeType.SIMPLE_EDGE;

    public void setFirstEndpoint(Terminal terminal) throws IllegalComponentException {
        if(terminal == null) throw new IllegalComponentException("Endpoint can not be null", this);
        this.firstEndpoint = terminal;
    }

    public Terminal getFirstEndpoint() {
        return this.firstEndpoint;
    }

    public void setSecondEndpoint(Terminal terminal) throws IllegalComponentException {
        if(terminal == null) throw new IllegalComponentException("Endpoint can not be null", this);
        this.secondEndpoint = terminal;
    }

    public Terminal getSecondEndpoint() {
        return this.secondEndpoint;
    }
}
