package core.implementations.euclidean;

import core.implementations.abstractions.AbstractMetric;
import core.interfaces.STBLocation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public final class EuclideanSquareMetric<Location extends STBLocation, Result extends DoubleProperty> extends AbstractMetric<Location, Result> {

    public static final EuclideanSquareMetric METRIC = new EuclideanSquareMetric();

    private EuclideanSquareMetric() {}

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
        DoubleProperty result = new SimpleDoubleProperty();
        result.bind((location1.xProperty()
                                .subtract(location2.xProperty())
                        .multiply(location1.xProperty()
                                .subtract(location2.xProperty())))
                .add((location1.yProperty()
                                .subtract(location2.yProperty())
                        .multiply(location1.yProperty()
                                .subtract(location2.yProperty())))));
        return (Result) result;
    }
}
