package core.implementations.algorithms.steiner;

import core.implementations.abstractions.AbstractSteinerTreeAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
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

public class GravitationalAlgorithm<Graph extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractSteinerTreeAlgorithm<Graph, Result> implements STBHeuristicAlgorithm<Graph, Result> {

    public GravitationalAlgorithm(Graph graph) {
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

        for (int i = 0; i < 10_000; i++) {
            triangles.forEach((triangle, steinerPoint) -> {
                final EuclideanTerminal[] nearestSteinerPoint = new EuclideanTerminal[1];
                final double[] distance = {Double.MAX_VALUE};
                triangles.values().forEach(otherSteinerPoint -> {
                    if ((!steinerPoint.equals(otherSteinerPoint)) && AlgorithmsUtils.getDistance(steinerPoint, otherSteinerPoint) < distance[0]) {
                        distance[0] = AlgorithmsUtils.getDistance(steinerPoint, otherSteinerPoint);
                        nearestSteinerPoint[0] = otherSteinerPoint;
                    }
                });
                Pair<EuclideanTerminal, EuclideanTerminal> separatingSide = AlgorithmsUtils.getSeparatingSide(steinerPoint, nearestSteinerPoint[0], triangle);
                if (null != separatingSide) {
                    Pair<Double, Double> resultForce = AlgorithmsUtils.getResultForce(steinerPoint, nearestSteinerPoint[0], separatingSide.getKey(), separatingSide.getValue());
                    steinerPoint.getLocation().xProperty().set(steinerPoint.getLocation().xProperty().get() + resultForce.getKey() / AlgorithmsUtils.m * AlgorithmsUtils.TIME * AlgorithmsUtils.TIME / 2);
                    steinerPoint.getLocation().yProperty().set(steinerPoint.getLocation().yProperty().get() + resultForce.getValue() / AlgorithmsUtils.m * AlgorithmsUtils.TIME * AlgorithmsUtils.TIME / 2);
                }
            });
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
        this.inProgress.set(false);
    }

    private  Map<Triad<EuclideanTerminal>, EuclideanTerminal> buildTriangulation() {
        List<Triad<EuclideanTerminal>> tempTriangles = AlgorithmsUtils.triangulation(this.result);
        if (null == tempTriangles) return null;
        Map<Triad<EuclideanTerminal>, EuclideanTerminal> triangles = new HashMap<>();
        tempTriangles.forEach(triangle -> {
            EuclideanTerminal steinerPoint = new EuclideanTerminal(new EuclideanLocation(
                    ((triangle.a.getLocation().xProperty().get() +
                            triangle.b.getLocation().xProperty().get() +
                            triangle.c.getLocation().xProperty().get()) / 3),
                    ((triangle.a.getLocation().yProperty().get() +
                            triangle.b.getLocation().yProperty().get() +
                            triangle.c.getLocation().yProperty().get()) / 3)),
                    IdUtils.getTerminalId(this.result));
            steinerPoint.typeProperty().setValue(STBTerminalType.STEINER_TERMINAL);
            result.addVertex(steinerPoint);
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