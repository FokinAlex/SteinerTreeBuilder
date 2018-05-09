package core.implementations.euclidean;

import com.sun.istack.internal.NotNull;
import core.implementations.abstractions.AbstractEdge;
import javafx.beans.property.DoubleProperty;

public class EuclideanEdge<Terminal extends EuclideanTerminal> extends AbstractEdge<Terminal> {

    public EuclideanEdge(@NotNull Terminal firstEndpoint, @NotNull Terminal secondEndpoint) {
        super(firstEndpoint, secondEndpoint);
        this.lengthProperty = (EuclideanMetric.METRIC.apply(this.firstEndpoint.getLocation(), this.secondEndpoint.getLocation()));
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
