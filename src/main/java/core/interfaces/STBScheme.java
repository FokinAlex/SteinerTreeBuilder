package core.interfaces;

import java.util.List;

public interface STBScheme<Page extends STBPage> {

    List<Page> getPages();
    boolean addPage(Page page);
    boolean removePage(Page page);
    Page getCurrentPage();
    boolean setCurrentPage(Page page);

    /* TODO: Configuration already must be here:
    Configuration - is part of conf-module (stb-conf)
    Map<ConfigurationElement>
    // */
}
