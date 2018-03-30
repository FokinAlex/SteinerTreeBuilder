package core.implementations.euclidean;

import core.exceptions.IllegalComponentException;
import core.implementations.abstractions.AbstractEdge;
import core.interfaces.STBEdge;

public class EuclideanEdge<Terminal extends EuclideanTerminal> extends AbstractEdge<Terminal> {

    // Default modifier here is for the package-private access
    EuclideanEdge(Terminal firstTerminal, Terminal secondTerminal) throws IllegalComponentException {
        setFirstEndpoint(firstTerminal);
        setSecondEndpoint(secondTerminal);
    }

    public double getLength() {
        return EuclideanMetric.METRIC.apply(
                this.getFirstEndpoint().getLocation(),
                this.getSecondEndpoint().getLocation());
    }

    @Override
    public void setFirstEndpoint(Terminal terminal) throws IllegalComponentException {
        super.setFirstEndpoint(terminal);
    }

    @Override
    public Terminal getFirstEndpoint() {
        return super.getFirstEndpoint();
    }

    @Override
    public void setSecondEndpoint(Terminal terminal) throws IllegalComponentException {
        super.setSecondEndpoint(terminal);
    }

    @Override
    public Terminal getSecondEndpoint() {
        return super.getSecondEndpoint();
    }
}
