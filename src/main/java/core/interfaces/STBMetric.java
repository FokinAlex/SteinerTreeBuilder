package core.interfaces;

import java.util.function.BiFunction;

@FunctionalInterface
public interface STBMetric<Location extends STBLocation, Result> extends BiFunction<Location, Location, Result> {
}
