package appi.ci.implementations.abstractions;

import appi.ci.interfaces.Project;
import core.implementations.GraphMultiPageScheme;
import core.implementations.GraphPage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.List;

public abstract class AbstractProject<Scheme extends GraphMultiPageScheme<Page>, Page extends GraphPage> implements Project<Scheme, Page> {

    protected Scheme scheme;
    protected File file;
    protected StringProperty nameProperty;

    public AbstractProject(Scheme scheme, String name) {
        this.scheme = scheme;
        this.nameProperty = new SimpleStringProperty(name);
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
    public StringProperty nameProperty() {
        return nameProperty;
    }
}
