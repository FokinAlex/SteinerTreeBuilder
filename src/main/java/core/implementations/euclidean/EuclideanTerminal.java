package core.implementations.euclidean;

import core.implementations.abstractions.AbstractTerminal;

public class EuclideanTerminal<Location extends EuclideanLocation> extends AbstractTerminal<Location> {

    public EuclideanTerminal(Location location, long id) {
        super(location, id);
    }

    @Override
    public Location getLocation() {
        return super.getLocation();
    }

    @Override
    public EuclideanTerminal clone() throws CloneNotSupportedException {
        EuclideanTerminal clone = new EuclideanTerminal(this.location.clone(), this.id);
        clone.type.setValue(this.type.getValue());
        return clone;
    }
}
