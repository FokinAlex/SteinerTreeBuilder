package core.implementations.abstractions;

import core.exceptions.IllegalComponentException;
import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
import core.types.STBEdgeType;
import javafx.beans.property.DoubleProperty;

public abstract class AbstractEdge<Terminal extends STBTerminal> implements STBEdge<Terminal> {

    protected Terminal firstEndpoint;
    protected Terminal secondEndpoint;
    public STBEdgeType type = STBEdgeType.SIMPLE_EDGE;

    protected DoubleProperty lengthProperty;

    @Override
    public void setFirstEndpoint(Terminal terminal) throws IllegalComponentException {
        if(terminal == null) throw new IllegalComponentException("Endpoint can not be null", this);
        this.firstEndpoint = terminal;
    }

    @Override
    public Terminal getFirstEndpoint() {
        return this.firstEndpoint;
    }

    @Override
    public void setSecondEndpoint(Terminal terminal) throws IllegalComponentException {
        if(terminal == null) throw new IllegalComponentException("Endpoint can not be null", this);
        this.secondEndpoint = terminal;
    }

    @Override
    public Terminal getSecondEndpoint() {
        return this.secondEndpoint;
    }

    @Override
    public DoubleProperty getLengthProperty() {
        return this.lengthProperty;
    }
}
