package appi.ci.implementations;

import appi.ci.implementations.abstractions.AbstractProject;
import core.implementations.GraphPage;
import core.implementations.GraphSinglePageScheme;

public class SimpleProject<Scheme extends GraphSinglePageScheme<Page>, Page extends GraphPage> extends AbstractProject<Scheme, Page> {

    public SimpleProject(Scheme scheme) {
        super(scheme);
    }
}
