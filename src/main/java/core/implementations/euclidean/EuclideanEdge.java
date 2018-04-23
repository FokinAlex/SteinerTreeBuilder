package core.implementations.euclidean;

import core.exceptions.IllegalComponentException;
import core.implementations.abstractions.AbstractEdge;
import core.implementations.abstractions.AbstractWeightedEdge;
import core.interfaces.STBEdge;

public class EuclideanEdge<Terminal extends EuclideanTerminal, Weight extends Double> extends AbstractWeightedEdge<Terminal, Weight> {

    public EuclideanEdge(Terminal firstTerminal, Terminal secondTerminal) throws IllegalComponentException {
        setFirstEndpoint(firstTerminal);
        setSecondEndpoint(secondTerminal);
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

    @Override
    public Weight getWeight() {
        return (Weight) EuclideanMetric.METRIC.apply(
                this.getFirstEndpoint().getLocation(),
                this.getSecondEndpoint().getLocation());
    }
}
