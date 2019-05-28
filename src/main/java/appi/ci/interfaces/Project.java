package appi.ci.interfaces;

import core.interfaces.STBNamed;
import core.interfaces.STBPage;
import core.interfaces.STBScheme;

import java.io.File;
import java.util.List;

public interface Project<Scheme extends STBScheme<Page>, Page extends STBPage> extends STBNamed {

    // TODO: add boolean hasChanges();
    File getFile();
    void setFile(File file);

    List<Page> getPages();
    boolean addPage(Page page);
    boolean removePage(Page page);
    Page getCurrentPage();
    boolean setCurrentPage(Page page);
}
