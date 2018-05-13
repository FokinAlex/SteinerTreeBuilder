package core.implementations.algorithms;

import core.implementations.abstractions.AbstractSteinerTreeAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBExactAlgorithm;
import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.util.Pair;
import utils.IdUtils;
import utils.iou.JsonUtils;

import java.util.*;

public class IncrementalOptimizationAlgorithm<Graph extends EuclideanGraph, Result extends EuclideanGraph> extends AbstractSteinerTreeAlgorithm<Graph, Result> implements STBExactAlgorithm<Graph, Result> {

    private List<EuclideanTerminal> sortedVertexes;

    public IncrementalOptimizationAlgorithm(Graph graph) {
        super(graph);
        if (this.argument.getAllVertexes().size() == 0) return;
        double meanX = (double) this.argument.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().getXProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / this.argument.getAllVertexes().size();
        double meanY = (double) this.argument.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().getYProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / this.argument.getAllVertexes().size();
        Map<STBTerminal, Double> tempMap = new HashMap<>();
        this.argument.getAllVertexes().forEach(vertex -> tempMap.put((STBTerminal) vertex, getDistance((STBTerminal) vertex, meanX, meanY)));
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
        if (this.argument.getAllVertexes().size() > 2) {
            currentTree = new EuclideanGraph(){{
                this.vertexes.add(sortedVertexes.get(0));
                this.vertexes.add(sortedVertexes.get(1));
                this.vertexes.add(sortedVertexes.get(2));
            }};
            localOptimization(currentTree, this.sortedVertexes.get(0), this.sortedVertexes.get(1), this.sortedVertexes.get(2));
            for (int i = 3; i < this.sortedVertexes.size(); i++) {
                bestTree = new EuclideanGraph() { @Override public double getWeight() { return Double.POSITIVE_INFINITY; }};
                Iterator<EuclideanEdge> iterator = currentTree.getAllEdges().iterator();
                while (iterator.hasNext()) {
                    EuclideanEdge edge = iterator.next();
                    subCurrentTree = clone(currentTree);
                    localOptimization(
                            subCurrentTree,
                            edge.getFirstEndpoint().getId(),
                            edge.getSecondEndpoint().getId(),
                            sortedVertexes.get(i)
                    );
                    if (subCurrentTree.getWeight() < bestTree.getWeight()) bestTree = clone(subCurrentTree);
                }
                currentTree = clone(bestTree);
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

    private static EuclideanGraph clone(EuclideanGraph graph) {
        try {
            return graph.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void localOptimization(EuclideanGraph graph, long point1_id, long point2_id, EuclideanTerminal newPoint) {
        final EuclideanTerminal[] points = new EuclideanTerminal[2];
        graph.getAllVertexes().forEach(terminal -> {
            if (((EuclideanTerminal) terminal).getId() == point1_id) points[0] = (EuclideanTerminal) terminal;
            if (((EuclideanTerminal) terminal).getId() == point2_id) points[1] = (EuclideanTerminal) terminal;
        });
        EuclideanTerminal point1 = points[0];
        EuclideanTerminal point2 = points[1];
        Iterator<EuclideanEdge> iterator = graph.getAllEdges().iterator();
        while (iterator.hasNext()) {
            EuclideanEdge edge = iterator.next();
            if (edge.getFirstEndpoint().equals(point1) && edge.getSecondEndpoint().equals(point2)) {
                iterator.remove();
                break;
            }
        }
        graph.addVertex(newPoint);
        localOptimization(graph, point1, point2, newPoint);
    }

    private static void localOptimization(EuclideanGraph graph, EuclideanTerminal point1, EuclideanTerminal point2, EuclideanTerminal point3) {
        EuclideanTerminal steinerPoint = steinerPoint(point1, point2, point3);
        if (point1.equals(steinerPoint)) {
            graph.addEdge(new EuclideanEdge(point2, point1));
            graph.addEdge(new EuclideanEdge(point3, point1));
        } else if (point2.equals(steinerPoint)) {
            graph.addEdge(new EuclideanEdge(point1, point2));
            graph.addEdge(new EuclideanEdge(point3, point2));
        } else if (point3.equals(steinerPoint)) {
            graph.addEdge(new EuclideanEdge(point1, point3));
            graph.addEdge(new EuclideanEdge(point2, point3));
        } else {
            graph.addVertex(steinerPoint);
            graph.addEdge(new EuclideanEdge(point1, steinerPoint));
            graph.addEdge(new EuclideanEdge(point2, steinerPoint));
            graph.addEdge(new EuclideanEdge(point3, steinerPoint));
        }
    }

    private static EuclideanTerminal steinerPoint(EuclideanTerminal point1, EuclideanTerminal point2, EuclideanTerminal point3) {
        if (angle(point2, point1, point3) > 120) return point1;
        if (angle(point1, point2, point3) > 120) return point2;
        if (angle(point2, point3, point1) > 120) return point3;
        Pair<Double, Double> extraPoint1 = getThirdEquilateralTrianglePoint(point1, point2, point3);
        Pair<Double, Double> extraPoint2 = getThirdEquilateralTrianglePoint(point2, point3, point1);
        Pair<Double, Double> intersection = getIntersection(
                extraPoint1.getKey(), extraPoint1.getValue(),
                point3.getLocation().getXProperty().get(), point3.getLocation().getYProperty().get(),
                extraPoint2.getKey(), extraPoint2.getValue(),
                point1.getLocation().getXProperty().get(), point1.getLocation().getYProperty().get()
        );
        // TODO: get id
        EuclideanTerminal steinerPoint = new EuclideanTerminal(new EuclideanLocation(intersection.getKey(), intersection.getValue()), IdUtils.getTerminalId());
        steinerPoint.type = STBTerminalType.STEINER_TERMINAL;
        return steinerPoint;
    }

    private static Pair<Double, Double> getIntersection(double x11, double y11, double x12, double y12, double x21, double y21, double x22, double y22) {
        double a1 = y11 - y12;
        double b1 = x12 - x11;
        double a2 = y21 - y22;
        double b2 = x22 - x21;
        double d = a1 * b2 - a2 * b1;
        if (d != 0) {
            double c1 = y12 * x11 - x12 * y11;
            double c2 = y22 * x21 - x22 * y21;
            double x = (b1 * c2 - b2 * c1) / d;
            double y = (a2 * c1 - a1 * c2) / d;
            return new Pair<>(x, y);
        }
        return null;
    }

    private static Pair<Double, Double> getThirdEquilateralTrianglePoint(STBTerminal basis1, STBTerminal basis2, STBTerminal point) {
        Pair<Double, Double> tempPoint1 = counterclockwiseRotation(basis1, basis2, 60);
        double tempDistance1 = getDistance(point, tempPoint1.getKey(), tempPoint1.getValue());
        Pair<Double, Double> tempPoint2 = counterclockwiseRotation(basis1, basis2, -60);
        double tempDistance2 = getDistance(point, tempPoint2.getKey(), tempPoint2.getValue());
        return tempDistance1 > tempDistance2 ? tempPoint1 : tempPoint2;
    }

    private static Pair<Double, Double> counterclockwiseRotation(STBTerminal point, STBTerminal regularPoint, double angle) {
        double x = - Math.sin(Math.toRadians(angle)) * (point.getLocation().getYProperty().get() - regularPoint.getLocation().getYProperty().get()) +
                Math.cos(Math.toRadians(angle)) * (point.getLocation().getXProperty().get() - regularPoint.getLocation().getXProperty().get()) + regularPoint.getLocation().getXProperty().get();
        double y = Math.cos(Math.toRadians(angle)) * (point.getLocation().getYProperty().get() - regularPoint.getLocation().getYProperty().get()) +
                Math.sin(Math.toRadians(angle)) * (point.getLocation().getXProperty().get() - regularPoint.getLocation().getXProperty().get()) + regularPoint.getLocation().getYProperty().get();
        return new Pair<>(x, y);
    }

    private static double angle(STBTerminal point1, STBTerminal point2, STBTerminal point3) {
        double a = getDistance(point2, point3);
        double b = getDistance(point1, point3);
        double c = getDistance(point1, point2);
        return Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2 * a * c)));
    }

    private static double getDistance(STBTerminal point1, STBTerminal point2) {
        return getDistance(point1.getLocation().getXProperty().get(), point1.getLocation().getYProperty().get(), point2.getLocation().getXProperty().get(), point2.getLocation().getYProperty().get());
    }

    private static double getDistance(STBTerminal point, double x, double y) {
        return getDistance(point.getLocation().getXProperty().get(), point.getLocation().getYProperty().get(), x, y);
    }

    private static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
