package core.implementations.euclidean;

import core.implementations.abstractions.AbstractMetric;

public class EuclideanMetric<Location extends EuclideanLocation, Result extends Double> extends AbstractMetric<Location, Result> {

    public static final EuclideanMetric METRIC = new EuclideanMetric();

    private EuclideanMetric() {}

    @Override
    public Result apply(Location location1, Location location2) {
        return (Result) (Double) Math.sqrt(
            (location1.getX() - location2.getX()) * (location1.getX() - location2.getX()) +
            (location1.getY() - location2.getY()) * (location1.getY() - location2.getY())
        );
    }
}
