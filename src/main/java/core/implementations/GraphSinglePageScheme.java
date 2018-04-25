package core.implementations;

import core.exceptions.IllegalPageException;
import core.interfaces.STBScheme;

import java.util.LinkedList;
import java.util.List;

public class GraphSinglePageScheme<Page extends GraphPage> implements STBScheme<Page> {

    private List<Page> pages = new LinkedList<>();

    public GraphSinglePageScheme() { }

    public GraphSinglePageScheme(Page page) throws IllegalPageException {
        this.pages.add(page);
    }

    @Deprecated
    @Override
    public List<Page> getPages() {
        return this.pages;
    }

    @Override
    public boolean addPage(Page page) {
        if(this.pages.get(0) == null) return pages.add(page);
        return false;
    }

    @Override
    public boolean removePage(Page page) {
        return this.pages.remove(page);
    }

    @Override
    public Page getCurrentPage() {
        return this.pages.get(0);
    }

    @Override
    public boolean setCurrentPage(Page page) {
        // Nothing to do here
        return true;
    }
}
