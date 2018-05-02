package core.implementations.algorithms;

import core.exceptions.IllegalComponentException;
import core.exceptions.IllegalLocationException;
import core.implementations.abstractions.AbstractAlgorithm;
import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBExactAlgorithm;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.beans.property.Property;
import javafx.util.Pair;

import java.util.*;

public class IncrementalOptimizationAlgorithm<Graph extends EuclideanGraph> extends AbstractAlgorithm<Graph> implements STBExactAlgorithm<Graph> {

    private List<EuclideanTerminal> sortedVertexes;
    private EuclideanGraph currentTree;
    private EuclideanGraph subCurrentTree;
    private EuclideanGraph bestTree;

    public IncrementalOptimizationAlgorithm(Graph graph) {
        super(graph);
        double meanX = (double) graph.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().getXProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / graph.getAllVertexes().size();
        double meanY = (double) graph.getAllVertexes().stream()
                .map(vertex -> ((STBTerminal)vertex).getLocation().getYProperty().get())
                .reduce((a, b) -> (double) a + (double) b)
                .get() / graph.getAllVertexes().size();
        Map<STBTerminal, Double> tempMap = new HashMap<>();
        graph.getAllVertexes().forEach(vertex -> tempMap.put((STBTerminal) vertex, getDistance((STBTerminal) vertex, meanX, meanY)));
        List<Map.Entry<STBTerminal, Double>> tempList = new ArrayList(tempMap.entrySet());
        tempList.sort(Map.Entry.comparingByValue());
        this.sortedVertexes = new ArrayList<>();
        tempList.forEach(entry -> this.sortedVertexes.add((EuclideanTerminal) entry.getKey()));
    }

    @Override
    public void run() {
        this.inProgressProperty().set(true);
        if (sortedVertexes.size() > 2) {
            this.currentTree = localOptimization(this.sortedVertexes.get(0), this.sortedVertexes.get(1), this.sortedVertexes.get(2));
            for (int i = 3; i < this.sortedVertexes.size(); i++) {
                int finalI = i;
                this.bestTree = new EuclideanGraph() { @Override public double getWeight() { return Double.MAX_VALUE; }};
                Iterator<EuclideanEdge> iterator = currentTree.getAllEdges().iterator();
                while (iterator.hasNext()) {
                    EuclideanEdge edge = iterator.next();
                    this.subCurrentTree = clone(currentTree);
                    localOptimization(
                            this.subCurrentTree,
                            edge.getFirstEndpoint().getLocation().getXProperty().get(),
                            edge.getFirstEndpoint().getLocation().getYProperty().get(),
                            edge.getSecondEndpoint().getLocation().getXProperty().get(),
                            edge.getSecondEndpoint().getLocation().getYProperty().get(),
                            sortedVertexes.get(finalI)
                    );
                    if (this.subCurrentTree.getWeight() < this.bestTree.getWeight()) bestTree = clone(subCurrentTree);
                }
                this.currentTree = clone(bestTree);
            }
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

    private static EuclideanGraph localOptimization(EuclideanGraph graph, double x1, double y1, double x2, double y2, EuclideanTerminal newPoint) {
        final EuclideanTerminal[] points = new EuclideanTerminal[2];
        graph.getAllVertexes().forEach(terminal -> {
            if (((EuclideanTerminal) terminal).getLocation().getXProperty().get() == x1 &&
                    ((EuclideanTerminal) terminal).getLocation().getYProperty().get() == y1) points[0] = (EuclideanTerminal) terminal;
            if (((EuclideanTerminal) terminal).getLocation().getXProperty().get() == x2 &&
                    ((EuclideanTerminal) terminal).getLocation().getYProperty().get() == y2) points[1] = (EuclideanTerminal) terminal;
        });
        EuclideanTerminal point1 = points[0];
        EuclideanTerminal point2 = points[1];
        EuclideanTerminal steinerPoint = steinerPoint(points[0], points[1], newPoint);
        Iterator<EuclideanEdge> iterator = graph.getAllEdges().iterator();
        while (iterator.hasNext()) {
            EuclideanEdge edge = iterator.next();
            if (edge.getFirstEndpoint().equals(point1) && edge.getSecondEndpoint().equals(point2)) iterator.remove();
        }
        graph.addVertex(newPoint);
        try {
            if (point1.equals(steinerPoint)) {
                graph.addEdge(new EuclideanEdge(point2, point1));
                graph.addEdge(new EuclideanEdge(newPoint, point1));
            } else if (point2.equals(steinerPoint)) {
                graph.addEdge(new EuclideanEdge(point1, point2));
                graph.addEdge(new EuclideanEdge(newPoint, point2));
            } else if (newPoint.equals(steinerPoint)) {
                graph.addEdge(new EuclideanEdge(point1, newPoint));
                graph.addEdge(new EuclideanEdge(point2, newPoint));
            } else {
                graph.addEdge(new EuclideanEdge(point1, steinerPoint));
                graph.addEdge(new EuclideanEdge(point2, steinerPoint));
                graph.addEdge(new EuclideanEdge(newPoint, steinerPoint));
                graph.addVertex(steinerPoint);
            }
            return graph;
        } catch (IllegalComponentException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static EuclideanGraph localOptimization(EuclideanTerminal point1, EuclideanTerminal point2, EuclideanTerminal point3) {
        EuclideanTerminal steinerPoint = steinerPoint(point1, point2, point3);
        EuclideanGraph result = new EuclideanGraph();
        result.addVertex(point1);
        result.addVertex(point2);
        result.addVertex(point3);
        try {
            if (point1.equals(steinerPoint)) {
                result.addEdge(new EuclideanEdge(point2, point1));
                result.addEdge(new EuclideanEdge(point3, point1));
            } else if (point2.equals(steinerPoint)) {
                result.addEdge(new EuclideanEdge(point1, point2));
                result.addEdge(new EuclideanEdge(point3, point2));
            } else if (point3.equals(steinerPoint)) {
                result.addEdge(new EuclideanEdge(point1, point3));
                result.addEdge(new EuclideanEdge(point2, point3));
            } else {
                result.addEdge(new EuclideanEdge(point1, steinerPoint));
                result.addEdge(new EuclideanEdge(point2, steinerPoint));
                result.addEdge(new EuclideanEdge(point3, steinerPoint));
                result.addVertex(steinerPoint);
            }
            return result;
        } catch (IllegalComponentException e) {
            e.printStackTrace();
            return null;
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
        try {
            EuclideanTerminal steinerPoint = new EuclideanTerminal(new EuclideanLocation(intersection.getKey(), intersection.getValue()));
            steinerPoint.type = STBTerminalType.STEINER_TERMINAL;
            return steinerPoint;
        } catch (IllegalLocationException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
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