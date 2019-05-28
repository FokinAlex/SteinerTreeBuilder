package appi.ci.implementations;

import appi.ci.implementations.abstractions.AbstractProject;
import core.implementations.GraphMultiPageScheme;
import core.implementations.GraphPage;

public class SimpleProject<Scheme extends GraphMultiPageScheme<Page>, Page extends GraphPage> extends AbstractProject<Scheme, Page> {

    public SimpleProject(Scheme scheme, String name) {
        super(scheme, name);
    }
}
