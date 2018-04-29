package core.implementations.euclidean;

import core.implementations.abstractions.AbstractMetric;

public final class EuclideanMetric<Location extends EuclideanLocation, Result extends Double> extends AbstractMetric<Location, Result> {

    public static final EuclideanMetric METRIC = new EuclideanMetric();

    private EuclideanMetric() {}

    @Override
    public Result apply(Location location1, Location location2) {
        return (Result) (Double) Math.sqrt(
            (location1.getXProperty().get() - location2.getXProperty().get()) * (location1.getXProperty().get() - location2.getXProperty().get()) +
            (location1.getYProperty().get() - location2.getYProperty().get()) * (location1.getYProperty().get() - location2.getYProperty().get())
        );
    }
}
