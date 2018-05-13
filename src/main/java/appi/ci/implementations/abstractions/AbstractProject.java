package appi.ci.implementations.abstractions;

import appi.ci.interfaces.Project;
import core.implementations.GraphPage;
import core.implementations.GraphSinglePageScheme;

import java.io.File;
import java.util.List;

public abstract class AbstractProject<Scheme extends GraphSinglePageScheme<Page>, Page extends GraphPage> implements Project<Scheme, Page> {

    protected Scheme scheme;
    protected File file;

    public AbstractProject(Scheme scheme) {
        this.scheme = scheme;
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
}
