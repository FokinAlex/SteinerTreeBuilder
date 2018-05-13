package core.implementations.abstractions;

import core.interfaces.STBEdge;
import core.interfaces.STBTerminal;
import core.types.STBEdgeType;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class AbstractEdge<Terminal extends STBTerminal> implements STBEdge<Terminal> {

    protected Terminal firstEndpoint;
    protected Terminal secondEndpoint;
    public STBEdgeType type = STBEdgeType.SIMPLE_EDGE;

    protected DoubleProperty lengthProperty;

    public AbstractEdge(Terminal firstEndpoint, Terminal secondEndpoint) {
        this.firstEndpoint = firstEndpoint;
        this.secondEndpoint = secondEndpoint;
        this.lengthProperty = new SimpleDoubleProperty();
    }

    @Override
    public Terminal getFirstEndpoint() {
        return this.firstEndpoint;
    }

    @Override
    public Terminal getSecondEndpoint() {
        return this.secondEndpoint;
    }

    @Override
    public DoubleProperty getLengthProperty() {
        return this.lengthProperty;
    }

    @Override
    protected abstract STBEdge clone() throws CloneNotSupportedException;
}
