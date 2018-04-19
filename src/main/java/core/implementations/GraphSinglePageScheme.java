package core.implementations;

import core.exceptions.IllegalPageException;
import core.interfaces.STBScheme;

import java.util.LinkedList;
import java.util.List;

public class GraphSinglePageScheme<Page extends GraphPage> implements STBScheme<Page> {

    private Page page;

    public GraphSinglePageScheme(Page page) throws IllegalPageException {
        if (page == null) throw new IllegalPageException("Page can not be null", this, null);
        this.page = page;
    }

    @Deprecated
    @Override
    public List<Page> getPages() {
        LinkedList<Page> tempList = new LinkedList<>();
        tempList.add(this.page);
        return tempList;
    }

    @Override
    public boolean addPage(Page page) {
        // Single-Page Scheme cant have more then one page
        return false;
    }

    @Override
    public boolean removePage(Page page) {
        // Scheme cant have less then one page
        return false;
    }

    @Override
    public Page getCurrentPage() {
        return this.page;
    }

    @Override
    public boolean setCurrentPage(Page page) {
        // Nothing to do here
        return true;
    }
}
