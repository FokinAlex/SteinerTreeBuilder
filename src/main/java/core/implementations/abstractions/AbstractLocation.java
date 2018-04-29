package core.implementations.abstractions;

import core.interfaces.STBLocation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class AbstractLocation implements STBLocation {

    protected DoubleProperty xProperty;
    protected DoubleProperty yProperty;

    public AbstractLocation(double x, double y) {
        xProperty = new SimpleDoubleProperty(x);
        yProperty = new SimpleDoubleProperty(y);
    }

    @Override
    public DoubleProperty getXProperty() {
        return this.xProperty;
    }

    @Override
    public DoubleProperty getYProperty() {
        return this.yProperty;
    }
}
