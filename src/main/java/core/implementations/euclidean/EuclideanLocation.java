package core.implementations.euclidean;

import core.implementations.abstractions.AbstractLocation;

public class EuclideanLocation extends AbstractLocation {

    public EuclideanLocation(double x, double y) {
        super(x, y);
    }

    public void setX(double x) {
        this.xProperty.set(x);
    }

    public void setY(double y) {
        this.yProperty.set(y);
    }
}
