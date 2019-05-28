package core.implementations.algorithms.other;

import core.implementations.abstractions.AbstractAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBEdge;
import utils.AlgorithmsUtils;
import utils.Triad;

import java.util.List;

public class DelaunayTriangulationAlgorithm<Graph extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractAlgorithm<Graph,Result> {

    private Result result;

    public DelaunayTriangulationAlgorithm(Graph graph) {
        super(graph);
        this.result = (Result) AlgorithmsUtils.clone(this.argument);
        AlgorithmsUtils.clear(this.result);
    }

    @Override
    public Result getResult() {
        return this.result;
    }

    @Override
    public void run() {
        this.inProgress.set(true);

        List<Triad<EuclideanTerminal>> triangles = AlgorithmsUtils.triangulation(this.result);

        if (null == triangles) {
            this.inProgress.set(false);
            return;
        }

        triangles.forEach(triangle -> {
            final boolean[] falgs = {true, true, true};
            this.result.getAllEdges().forEach(edge -> {
                if ((((STBEdge) edge).getFirstEndpoint().equals(triangle.a) &&
                        ((STBEdge) edge).getSecondEndpoint().equals(triangle.b)) ||
                        (((STBEdge) edge).getSecondEndpoint().equals(triangle.a) &&
                        ((STBEdge) edge).getFirstEndpoint().equals(triangle.b))) {
                    falgs[0] = false;
                }
                if ((((STBEdge) edge).getFirstEndpoint().equals(triangle.b) &&
                        ((STBEdge) edge).getSecondEndpoint().equals(triangle.c)) ||
                        (((STBEdge) edge).getSecondEndpoint().equals(triangle.b) &&
                                ((STBEdge) edge).getFirstEndpoint().equals(triangle.c))) {
                    falgs[1] = false;
                }
                if ((((STBEdge) edge).getFirstEndpoint().equals(triangle.a) &&
                        ((STBEdge) edge).getSecondEndpoint().equals(triangle.c)) ||
                        (((STBEdge) edge).getSecondEndpoint().equals(triangle.a) &&
                                ((STBEdge) edge).getFirstEndpoint().equals(triangle.c))) {
                    falgs[2] = false;
                }
            });
            if (falgs[0]) this.result.addEdge(new EuclideanEdge(triangle.a, triangle.b));
            if (falgs[1]) this.result.addEdge(new EuclideanEdge(triangle.b, triangle.c));
            if (falgs[2]) this.result.addEdge(new EuclideanEdge(triangle.c, triangle.a));
        });

        this.inProgress.set(false);
    }
}
