package core.implementations.euclidean;

import core.implementations.abstractions.AbstractMetric;
import core.interfaces.STBLocation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public final class EuclideanMetric<Location extends STBLocation, Result extends DoubleProperty> extends AbstractMetric<Location, Result> {

    public static final EuclideanMetric METRIC = new EuclideanMetric();

    private EuclideanMetric() {}

    /**
     * Returns <code>DoubleProperty</code> which contains square (!) of euclidean
     * metric.
     *
     * @param location1 the first edge's endpoint location.s
     * @param location2 the second edge's endpoint location.
     * @return square (!) of euclidean.
     */
    @Override
    public Result apply(Location location1, Location location2) {
        DoubleProperty temp = new SimpleDoubleProperty();
        temp.bind((location1.getXProperty()
                                .subtract(location2.getXProperty())
                        .multiply(location1.getXProperty()
                                .subtract(location2.getXProperty())))
                .add((location1.getYProperty()
                                .subtract(location2.getYProperty())
                        .multiply(location1.getYProperty()
                                .subtract(location2.getYProperty())))));
        DoubleProperty result = new SimpleDoubleProperty(Math.sqrt(temp.get()));
        temp.addListener((observable, oldValue, newValue) -> result.set(Math.sqrt(temp.get())));
        return (Result) result;
    }
}
