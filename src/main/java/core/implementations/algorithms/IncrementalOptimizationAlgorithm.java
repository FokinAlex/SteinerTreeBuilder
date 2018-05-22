package core.implementations.algorithms;

import core.implementations.abstractions.AbstractSteinerTreeAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBExactAlgorithm;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import utils.AlgorithmsUtils;
import utils.IdUtils;

import java.util.*;

public class IncrementalOptimizationAlgorithm<Graph extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractSteinerTreeAlgorithm<Graph, Result> implements STBExactAlgorithm<Graph, Result> {

    private List<EuclideanTerminal> sortedVertexes;

    public IncrementalOptimizationAlgorithm(Graph graph) {
        super(graph);
        if (this.argument.getAllVertexes().size() == 0) return;
        double meanX = (double) this.argument.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().xProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / this.argument.getAllVertexes().size();
        double meanY = (double) this.argument.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().yProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / this.argument.getAllVertexes().size();
        Map<STBTerminal, Double> tempMap = new HashMap<>();
        this.argument.getAllVertexes().forEach(vertex -> {
            if (((STBTerminal) vertex).typeProperty().getValue().equals(STBTerminalType.SIMPLE_TERMINAL))
                tempMap.put((STBTerminal) vertex, AlgorithmsUtils.getDistance((STBTerminal) vertex, meanX, meanY));
        });
        List<Map.Entry<STBTerminal, Double>> tempList = new ArrayList(tempMap.entrySet());
        tempList.sort(Map.Entry.comparingByValue());
        this.sortedVertexes = new ArrayList<>();
        tempList.forEach(entry -> this.sortedVertexes.add((EuclideanTerminal) entry.getKey()));
    }

    @Override
    public void run() {
        EuclideanGraph currentTree;
        EuclideanGraph subCurrentTree;
        EuclideanGraph bestTree;
        IdUtils.setGraph(this.argument);
        this.inProgressProperty().set(true);
        if (this.sortedVertexes.size() > 2) {
            currentTree = new EuclideanGraph(){{
                this.vertexes.add(sortedVertexes.get(0));
                this.vertexes.add(sortedVertexes.get(1));
                this.vertexes.add(sortedVertexes.get(2));
            }};
            AlgorithmsUtils.localOptimization(currentTree, this.sortedVertexes.get(0), this.sortedVertexes.get(1), this.sortedVertexes.get(2));
            for (int i = 3; i < this.sortedVertexes.size(); i++) {
                bestTree = new EuclideanGraph() { @Override public double getWeight() { return Double.POSITIVE_INFINITY; }};
                Iterator<EuclideanEdge> iterator = currentTree.getAllEdges().iterator();
                while (iterator.hasNext()) {
                    EuclideanEdge edge = iterator.next();
                    subCurrentTree = AlgorithmsUtils.clone(currentTree);
                    AlgorithmsUtils.localOptimization(
                            subCurrentTree,
                            edge.getFirstEndpoint().getId(),
                            edge.getSecondEndpoint().getId(),
                            sortedVertexes.get(i)
                    );
                    if (subCurrentTree.getWeight() < bestTree.getWeight()) bestTree = AlgorithmsUtils.clone(subCurrentTree);
                }
                currentTree = AlgorithmsUtils.clone(bestTree);
            }
            result = (Result) currentTree;
        } else {
            if (this.argument.getAllVertexes().size() == 2 && this.argument.getAllEdges().size() == 0) {
                Iterator iterator = this.argument.getAllVertexes().iterator();
                this.argument.addEdge(new EuclideanEdge((EuclideanTerminal) iterator.next(), (EuclideanTerminal) iterator.next()));
            }
            this.result = (Result) this.argument;
        }
        this.inProgressProperty().set(false);
    }
}
