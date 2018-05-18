package core.implementations.euclidean;

import core.implementations.abstractions.AbstractEdge;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class EuclideanEdge<Terminal extends EuclideanTerminal> extends AbstractEdge<Terminal> {

    private DoubleProperty euclideanSquare;

    public EuclideanEdge(Terminal firstEndpoint, Terminal secondEndpoint) {
        super(firstEndpoint, secondEndpoint);
        this.euclideanSquare = EuclideanSquareMetric.METRIC.apply(this.firstEndpoint.getLocation(), this.secondEndpoint.getLocation());
        this.euclideanSquare.addListener((observable, oldValue, newValue) -> this.lengthProperty.set(Math.sqrt((double) newValue)));
        this.lengthProperty = new SimpleDoubleProperty(Math.sqrt(euclideanSquare.get()));
    }

    @Override
    public Terminal getFirstEndpoint() {
        return super.getFirstEndpoint();
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

    @Override
    public EuclideanEdge clone() throws CloneNotSupportedException {
        return new EuclideanEdge(
                this.firstEndpoint.clone(),
                this.secondEndpoint.clone()
        );
    }
}
