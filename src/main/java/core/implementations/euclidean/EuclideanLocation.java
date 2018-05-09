package core.implementations.euclidean;

import core.implementations.abstractions.AbstractLocation;

public class EuclideanLocation extends AbstractLocation {

    public EuclideanLocation(double x, double y) {
        super(x, y);
    }

    @Override
    public EuclideanLocation clone() throws CloneNotSupportedException {
        return new EuclideanLocation(this.xProperty.get(), this.yProperty.get());
    }
}
