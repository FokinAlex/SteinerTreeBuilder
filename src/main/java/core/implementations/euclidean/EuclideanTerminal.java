package core.implementations.euclidean;

import core.exceptions.IllegalLocationException;
import core.implementations.abstractions.AbstractTerminal;

public class EuclideanTerminal<Location extends EuclideanLocation> extends AbstractTerminal<Location> {

    // Default modifier here is for the package-private access
    EuclideanTerminal(Location location) throws IllegalLocationException {
        this.setLocation(location);
    }

    @Override
    public Location getLocation() {
        return super.getLocation();
    }

    @Override
    public void setLocation(Location location) throws IllegalLocationException {
        super.setLocation(location);
    }
}
