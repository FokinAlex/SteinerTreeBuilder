package core.implementations.algorithms.steiner;

import core.implementations.abstractions.AbstractSteinerTreeAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBHeuristicAlgorithm;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.util.Pair;
import utils.AlgorithmsUtils;
import utils.IdUtils;
import utils.Triad;

import java.util.*;

public class IncrementalOptimizationAlgorithm<Graph extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractSteinerTreeAlgorithm<Graph, Result> implements STBHeuristicAlgorithm<Graph, Result> {

    private List<EuclideanTerminal> sortedVertexes;

    public IncrementalOptimizationAlgorithm(Graph graph) {
        super(graph);
        this.result = (Result) AlgorithmsUtils.clone(this.argument);
        this.sortedVertexes = new ArrayList<>();
        if (this.result.getAllVertexes().size() == 0) return;
        double meanX = (double) this.result.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().xProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / this.result.getAllVertexes().size();
        double meanY = (double) this.result.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().yProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / this.result.getAllVertexes().size();
        Map<STBTerminal, Double> tempMap = new HashMap<>();
        this.result.getAllVertexes().forEach(vertex -> {
            if (((STBTerminal) vertex).typeProperty().getValue().equals(STBTerminalType.SIMPLE_TERMINAL))
                tempMap.put((STBTerminal) vertex, AlgorithmsUtils.getDistance((STBTerminal) vertex, meanX, meanY));
        });
        List<Map.Entry<STBTerminal, Double>> tempList = new ArrayList(tempMap.entrySet());
        tempList.sort(Map.Entry.comparingByValue());
        tempList.forEach(entry -> this.sortedVertexes.add((EuclideanTerminal) entry.getKey()));
    }

    @Override
    public void run() {
        this.inProgress.set(true);
        IdUtils.setGraph(this.result);

        if (sortedVertexes.size() < 3) {
            this.buildMST();
            this.inProgress.set(false);
            return;
        }

        EuclideanGraph currentTree;
        EuclideanGraph subCurrentTree;
        EuclideanGraph bestTree;

        currentTree = new EuclideanGraph(){{
            this.vertexes.add(sortedVertexes.get(0));
            this.vertexes.add(sortedVertexes.get(1));
            this.vertexes.add(sortedVertexes.get(2));
        }};

        AlgorithmsUtils.triangleSteinerTree(currentTree, new Triad<>(this.sortedVertexes.get(0), this.sortedVertexes.get(1), this.sortedVertexes.get(2)));

        for (int i = 3; i < this.sortedVertexes.size(); i++) {
            bestTree = new EuclideanGraph() {
                @Override
                public double getWeight() {
                    return Double.POSITIVE_INFINITY;
                }
            };
            Iterator<EuclideanEdge> iterator = currentTree.getAllEdges().iterator();
            while (iterator.hasNext()) {
                EuclideanEdge edge = iterator.next();
                subCurrentTree = AlgorithmsUtils.clone(currentTree);
                AlgorithmsUtils.triangleSteinerTree(
                        subCurrentTree,
                        edge.getFirstEndpoint().getId(),
                        edge.getSecondEndpoint().getId(),
                        sortedVertexes.get(i)
                );
                if (subCurrentTree.getWeight() < bestTree.getWeight())
                    bestTree = AlgorithmsUtils.clone(subCurrentTree);
            }
            currentTree = AlgorithmsUtils.clone(bestTree);
        }

        this.result = (Result) currentTree;
        this.inProgress.set(false);
    }

    private void buildMST() {
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
    }
}
