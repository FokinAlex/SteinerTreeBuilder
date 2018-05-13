package core.implementations;

import core.interfaces.STBScheme;

import java.util.List;

public class GraphSinglePageScheme<Page extends GraphPage> implements STBScheme<Page> {

    private Page page;

    public GraphSinglePageScheme() { }

    public GraphSinglePageScheme(Page page) {
        this.page = page;
    }

    @Deprecated
    @Override
    public List<Page> getPages() {
        return null;
    }

    @Deprecated
    @Override
    public boolean addPage(Page page) {
        // Nothing to do here
        return false;
    }

    @Deprecated
    @Override
    public boolean removePage(Page page) {
        // Nothing to do here
        return false;
    }

    @Override
    public Page getCurrentPage() {
        return this.page;
    }

    @Deprecated
    @Override
    public boolean setCurrentPage(Page page) {
        // Nothing to do here
        return false;
    }
}
