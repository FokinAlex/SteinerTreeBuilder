package core.implementations.abstractions;

import core.interfaces.STBLocation;
import core.interfaces.STBMetric;
import javafx.beans.property.Property;

public abstract class AbstractMetric<Location extends STBLocation, Result extends Property> implements STBMetric<Location, Result> {
}
