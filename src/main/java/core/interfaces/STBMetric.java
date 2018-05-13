package core.interfaces;

import javafx.beans.property.Property;

import java.util.function.BiFunction;

@FunctionalInterface
public interface STBMetric<Location extends STBLocation, Result extends Property> extends BiFunction<Location, Location, Result> {
}
