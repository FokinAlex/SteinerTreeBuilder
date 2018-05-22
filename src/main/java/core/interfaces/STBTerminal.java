package core.interfaces;

import core.types.STBTerminalType;
import javafx.beans.property.Property;

public interface STBTerminal<T extends STBLocation> extends STBComponent, Cloneable{

    long getId();
    T getLocation();
    Property<STBTerminalType> typeProperty();
}
