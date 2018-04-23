package core.exceptions;

import core.interfaces.STBComponent;

public class IllegalComponentException extends STBCoreException {

    private STBComponent component;

    /**
     * Constructs an <code>IllegalComponentException</code> with the
     * specified detail message and component (which is saved for late
     * and available by {@link #getComponent()} method).
     *
     * @param message the detail message.
     * @param component the component which was the cause of the exception.
     */
    public IllegalComponentException(String message, STBComponent component) {
        super(message);
        this.component = component;
    }

    public STBComponent getComponent() {
        return this.component;
    }
}
