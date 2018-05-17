package core.implementations;

import core.interfaces.STBScheme;

import java.util.ArrayList;
import java.util.List;

public class GraphMultiPageScheme<Page extends GraphPage> implements STBScheme<Page> {

    private List<Page> pages;
    private Page currentPage;
    private String name;

    public GraphMultiPageScheme() {
        this.pages = new ArrayList<>();
        this.name = this.getClass().getSimpleName() + this.hashCode();
    }

    @Override
    public List<Page> getPages() {
        return this.pages;
    }

    @Override
    public boolean addPage(Page page) {
        return this.pages.add(page);
    }

    @Override
    public boolean removePage(Page page) {
        return this.pages.remove(page);
    }

    @Override
    public Page getCurrentPage() {
        return this.currentPage;
    }

    @Override
    public boolean setCurrentPage(Page page) {
        this.currentPage = page;
        return this.pages.contains(page);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
