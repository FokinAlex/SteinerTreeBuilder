package core.implementations.algorithms.steiner;

import core.implementations.abstractions.AbstractSteinerTreeAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBEdge;
import core.interfaces.STBHeuristicAlgorithm;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.util.Pair;
import utils.AlgorithmsUtils;
import utils.IdUtils;
import utils.Triad;

import java.util.*;

public class SmithLeeLiebmanAlgorithm<Graph extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractSteinerTreeAlgorithm<Graph, Result> implements STBHeuristicAlgorithm<Graph, Result> {

    public SmithLeeLiebmanAlgorithm(Graph graph) {
        super(graph);
        this.result = (Result) AlgorithmsUtils.clone(this.argument);
        AlgorithmsUtils.clear(this.result);
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public void run() {
        this.inProgress.set(true);
        IdUtils.setGraph(this.result);

        Map<Triad<EuclideanTerminal>, EuclideanTerminal> triangles = this.buildTriangulation();

        if (null == triangles || triangles.size() < 1) {
            this.buildMST();
            inProgress.set(false);
            return;
        }

        this.buildMST();

        boolean hasExcess = true;
        while (hasExcess) {
            Set<EuclideanTerminal> excess = new HashSet<>();
            this.result.getAllVertexes().forEach(terminal -> {
                if (((STBTerminal) terminal).typeProperty().getValue().equals(STBTerminalType.STEINER_TERMINAL)) {
                    final int[] counter = {0};
                    this.result.getAllEdges().forEach(edge -> {
                        if (((STBEdge) edge).getFirstEndpoint().equals(terminal) || ((STBEdge) edge).getSecondEndpoint().equals(terminal))
                            counter[0]++;
                    });
                    if (counter[0] < 3 || counter[0] > 4) excess.add((EuclideanTerminal) terminal);
                }
            });
            hasExcess = !excess.isEmpty();
            this.result.removeVertexes(excess);
            this.buildMST();
        }


        this.result.getAllVertexes().forEach(terminal -> {
            if (((STBTerminal) terminal).typeProperty().getValue().equals(STBTerminalType.STEINER_TERMINAL)) {
                if (((STBTerminal) terminal).typeProperty().getValue().equals(STBTerminalType.STEINER_TERMINAL)) {
                    final int[] counter = {0};
                    final EuclideanTerminal[] values = new EuclideanTerminal[3];
                    this.result.getAllEdges().forEach(edge -> {
                        if (((STBEdge) edge).getFirstEndpoint().equals(terminal)) values[counter[0]++] = (EuclideanTerminal) ((STBEdge) edge).getSecondEndpoint();
                        if (((STBEdge) edge).getSecondEndpoint().equals(terminal)) values[counter[0]++] = (EuclideanTerminal) ((STBEdge) edge).getFirstEndpoint();
                    });
                    if (counter[0] == 3) {
                        Pair<Double, Double> newLocation = AlgorithmsUtils.triangleLocalOptimization(new Triad<>(values[0], values[1], values[2]));
                        ((STBTerminal) terminal).getLocation().xProperty().set(newLocation.getKey());
                        ((STBTerminal) terminal).getLocation().yProperty().set(newLocation.getValue());
                    }
                }
            }
        });
        this.inProgress.set(false);
    }

    private  Map<Triad<EuclideanTerminal>, EuclideanTerminal> buildTriangulation() {
        List<Triad<EuclideanTerminal>> tempTriangles = AlgorithmsUtils.triangulation(this.result);
        if (null == tempTriangles) return null;
        Map<Triad<EuclideanTerminal>, EuclideanTerminal> triangles = new HashMap<>();
        tempTriangles.forEach(triangle -> {
            EuclideanTerminal steinerPoint = AlgorithmsUtils.steinerPoint(this.result, triangle);
            this.result.addVertex(steinerPoint);
            triangles.put(triangle, steinerPoint);
        });
        return triangles;
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