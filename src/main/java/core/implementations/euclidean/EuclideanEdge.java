package core.implementations.euclidean;

import core.exceptions.IllegalComponentException;
import core.implementations.abstractions.AbstractEdge;
import javafx.beans.property.DoubleProperty;

public class EuclideanEdge<Terminal extends EuclideanTerminal> extends AbstractEdge<Terminal> {

    public EuclideanEdge(Terminal firstTerminal, Terminal secondTerminal) throws IllegalComponentException {
        setFirstEndpoint(firstTerminal);
        setSecondEndpoint(secondTerminal);
        this.lengthProperty = EuclideanMetric.METRIC.apply(firstTerminal.getLocation(), secondTerminal.getLocation());
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
    public DoubleProperty getLengthProperty() {
        return super.getLengthProperty();
    }

    @Override
    public double getLength() {
        return getLengthProperty().get();
    }
}
