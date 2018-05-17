package appi.ci.implementations.abstractions;

import appi.ci.interfaces.Project;
import core.implementations.GraphMultiPageScheme;
import core.implementations.GraphPage;

import java.io.File;
import java.util.List;

public abstract class AbstractProject<Scheme extends GraphMultiPageScheme<Page>, Page extends GraphPage> implements Project<Scheme, Page> {

    protected Scheme scheme;
    protected File file;

    public AbstractProject(Scheme scheme, String name) {
        this.scheme = scheme;
        this.setName(name);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Deprecated
    @Override
    public List<Page> getPages() {
        return this.scheme.getPages();
    }

    @Override
    public boolean addPage(Page page) {
        return this.scheme.addPage(page);
    }

    @Override
    public boolean removePage(Page page) {
        return this.scheme.removePage(page);
    }

    @Override
    public Page getCurrentPage() {
        return this.scheme.getCurrentPage();
    }

    @Override
    public boolean setCurrentPage(Page page) {
        return this.scheme.setCurrentPage(page);
    }

    @Override
    public String getName() {
        return this.scheme.getName();
    }

    @Override
    public void setName(String name) {
        this.scheme.setName(name);
    }
}
