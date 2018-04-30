package core.implementations.algorithms;

import core.implementations.abstractions.AbstractAlgorithm;
import core.interfaces.STBExactAlgorithm;
import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;

public class TestAlgorithm<Graph extends STBGraph> extends AbstractAlgorithm<Graph> implements STBExactAlgorithm<Graph> {

    public TestAlgorithm(Graph graph) {
        super(graph);
    }

    @Override
    public void run() {
        this.inProgress.set(true);
        this.argument.getAllVertexes().forEach(terminal ->
                ((STBTerminal) terminal).getLocation().getXProperty().set(((STBTerminal) terminal).getLocation().getXProperty().get() + 10)
        );
        this.inProgress.set(false);
    }
}
