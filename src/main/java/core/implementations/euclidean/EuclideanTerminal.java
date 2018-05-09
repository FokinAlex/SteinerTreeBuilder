package core.implementations.euclidean;

import com.sun.istack.internal.NotNull;
import core.implementations.abstractions.AbstractTerminal;

public class EuclideanTerminal<Location extends EuclideanLocation> extends AbstractTerminal<Location> {

    public EuclideanTerminal(@NotNull Location location, long id) {
        super(location, id);
    }

    @Override
    public Location getLocation() {
        return super.getLocation();
    }

    @Override
    public EuclideanTerminal clone() throws CloneNotSupportedException {
        return new EuclideanTerminal(this.location.clone(), this.id);
    }
}
