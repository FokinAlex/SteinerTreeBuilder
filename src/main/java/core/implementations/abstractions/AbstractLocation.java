package core.implementations.abstractions;

import core.interfaces.STBLocation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class AbstractLocation implements STBLocation {

    protected DoubleProperty xProperty;
    protected DoubleProperty yProperty;

    public AbstractLocation(double x, double y) {
        xProperty = new SimpleDoubleProperty(x);
        yProperty = new SimpleDoubleProperty(y);
    }

    @Override
    public DoubleProperty xProperty() {
        return this.xProperty;
    }

    @Override
    public DoubleProperty yProperty() {
        return this.yProperty;
    }

    @Override
    protected abstract STBLocation clone() throws CloneNotSupportedException;
}
