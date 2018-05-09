package core.implementations.abstractions;

import com.sun.istack.internal.NotNull;
import core.exceptions.IllegalLocationException;
import core.interfaces.STBLocation;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;

public abstract class AbstractTerminal<Location extends STBLocation> implements STBTerminal<Location> {

    protected long id;
    protected Location location;
    public STBTerminalType type = STBTerminalType.SIMPLE_TERMINAL;

    public AbstractTerminal(@NotNull Location location, long id) {
        this.location = location;
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    protected abstract STBTerminal clone() throws CloneNotSupportedException;
}

