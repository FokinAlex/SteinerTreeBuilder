package core.implementations.algorithms.other;

import core.implementations.abstractions.AbstractAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanTerminal;
import javafx.util.Pair;
import utils.AlgorithmsUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KruskallAlgorithm<Graph extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractAlgorithm<Graph,Result> {

    private Result result;

    public KruskallAlgorithm(Graph graph) {
        super(graph);
        this.result = (Result) AlgorithmsUtils.clone(this.argument);
//        AlgorithmsUtils.clear(this.result);
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public void run() {
        this.inProgress.set(true);

        Map<Pair<EuclideanTerminal, EuclideanTerminal>, Double> map = AlgorithmsUtils.getCompleteStructure(this.result);

        List<Map.Entry<Pair<EuclideanTerminal, EuclideanTerminal>, Double>> tempList = new ArrayList(map.entrySet());
        tempList.sort(Map.Entry.comparingByValue());

        ArrayList<Pair<EuclideanTerminal, EuclideanTerminal>> sortedVertexes = new ArrayList<>();
        tempList.forEach(entry -> sortedVertexes.add(entry.getKey()));

        sortedVertexes.forEach(pair -> {
            EuclideanEdge edge = new EuclideanEdge(pair.getKey(), pair.getValue());
            this.result.addEdge(edge);
            if (!AlgorithmsUtils.isTree(this.result)) this.result.removeEdge(edge);
        });

        this.inProgress.set(false);
    }
}
