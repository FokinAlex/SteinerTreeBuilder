package core.exceptions;

import core.interfaces.STBPage;
import core.interfaces.STBScheme;

public class IllegalPageException extends STBCoreException {

    private STBScheme scheme;
    private STBPage page;

    /**
     * Constructs an <code>IllegalPageException</code> with the
     * specified detail message, scheme (which is saved for late
     * and available by {@link #getScheme()} method) and page
     * (which is saved for late and available by {@link #getPage()} method)
     *
     * @param message the detail message.
     * @param scheme the scheme which was the cause of the exception.
     * @param page the page which was the cause of the exception.
     */
    public IllegalPageException(String message, STBScheme scheme, STBPage page) {
        super(message);
        this.scheme = scheme;
        this.page = page;
    }

    public STBPage getPage() {
        return page;
    }

    public STBScheme getScheme() {
        return scheme;
    }
}
