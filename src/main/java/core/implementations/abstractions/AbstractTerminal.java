package core.implementations.abstractions;

import core.exceptions.IllegalLocationException;
import core.interfaces.STBLocation;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;

public abstract class AbstractTerminal<Location extends STBLocation> implements STBTerminal<Location> {

    private Location location;
    public STBTerminalType type = STBTerminalType.SIMPLE_TERMINAL;

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) throws IllegalLocationException {
        if(location == null) throw new IllegalLocationException("Location can not be null");
        this.location = location;
    }
}

