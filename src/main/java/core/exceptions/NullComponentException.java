package core.exceptions;

import core.interfaces.STBComponent;

public class NullComponentException extends IllegalComponentException {

    /**
     * Constructs an <code>NullComponentException</code> with the
     * specified detail message.
     *
     * @param message the detail message.
     *
     * @see IllegalComponentException
     */
    public NullComponentException(String message) {
        super(message, null);
    }
}
