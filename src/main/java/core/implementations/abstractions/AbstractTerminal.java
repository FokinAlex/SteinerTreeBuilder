package core.implementations.abstractions;

import core.interfaces.STBLocation;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public abstract class AbstractTerminal<Location extends STBLocation> implements STBTerminal<Location> {

    protected long id;
    protected Location location;
    protected Property<STBTerminalType> type = new SimpleObjectProperty<>(STBTerminalType.SIMPLE_TERMINAL);

    public AbstractTerminal(Location location, long id) {
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
    public Property<STBTerminalType> typeProperty() {
        return this.type;
    }

    @Override
    protected abstract STBTerminal clone() throws CloneNotSupportedException;
}

