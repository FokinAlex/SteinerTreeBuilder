package utils;

import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.util.Pair;
import utils.IdUtils;

import java.util.Iterator;

public class AlgorithmsUtils {

    public static EuclideanGraph clone(EuclideanGraph graph) {
        try {
            return graph.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void localOptimization(EuclideanGraph graph, long point1_id, long point2_id, EuclideanTerminal newPoint) {
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

    public static void localOptimization(EuclideanGraph graph, EuclideanTerminal point1, EuclideanTerminal point2, EuclideanTerminal point3) {
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

    public static EuclideanTerminal steinerPoint(EuclideanTerminal point1, EuclideanTerminal point2, EuclideanTerminal point3) {
        if (angle(point2, point1, point3) > 120) return point1;
        if (angle(point1, point2, point3) > 120) return point2;
        if (angle(point2, point3, point1) > 120) return point3;
        Pair<Double, Double> extraPoint1 = getThirdEquilateralTrianglePoint(point1, point2, point3);
        Pair<Double, Double> extraPoint2 = getThirdEquilateralTrianglePoint(point2, point3, point1);
        Pair<Double, Double> intersection = getIntersection(
                extraPoint1.getKey(), extraPoint1.getValue(),
                point3.getLocation().xProperty().get(), point3.getLocation().yProperty().get(),
                extraPoint2.getKey(), extraPoint2.getValue(),
                point1.getLocation().xProperty().get(), point1.getLocation().yProperty().get()
        );
        EuclideanTerminal steinerPoint = new EuclideanTerminal(new EuclideanLocation(intersection.getKey(), intersection.getValue()), IdUtils.getTerminalId());
        steinerPoint.typeProperty().setValue(STBTerminalType.STEINER_TERMINAL);
        return steinerPoint;
    }

    public static Pair<Double, Double> getIntersection(double x11, double y11, double x12, double y12, double x21, double y21, double x22, double y22) {
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

    public static Pair<Double, Double> getThirdEquilateralTrianglePoint(STBTerminal basis1, STBTerminal basis2, STBTerminal point) {
        Pair<Double, Double> tempPoint1 = counterclockwiseRotation(basis1, basis2, 60);
        double tempDistance1 = getDistance(point, tempPoint1.getKey(), tempPoint1.getValue());
        Pair<Double, Double> tempPoint2 = counterclockwiseRotation(basis1, basis2, -60);
        double tempDistance2 = getDistance(point, tempPoint2.getKey(), tempPoint2.getValue());
        return tempDistance1 > tempDistance2 ? tempPoint1 : tempPoint2;
    }

    public static Pair<Double, Double> counterclockwiseRotation(STBTerminal point, STBTerminal regularPoint, double angle) {
        double x = - Math.sin(Math.toRadians(angle)) * (point.getLocation().yProperty().get() - regularPoint.getLocation().yProperty().get()) +
                Math.cos(Math.toRadians(angle)) * (point.getLocation().xProperty().get() - regularPoint.getLocation().xProperty().get()) + regularPoint.getLocation().xProperty().get();
        double y = Math.cos(Math.toRadians(angle)) * (point.getLocation().yProperty().get() - regularPoint.getLocation().yProperty().get()) +
                Math.sin(Math.toRadians(angle)) * (point.getLocation().xProperty().get() - regularPoint.getLocation().xProperty().get()) + regularPoint.getLocation().yProperty().get();
        return new Pair<>(x, y);
    }

    public static double angle(STBTerminal point1, STBTerminal point2, STBTerminal point3) {
        double a = getDistance(point2, point3);
        double b = getDistance(point1, point3);
        double c = getDistance(point1, point2);
        return Math.toDegrees(Math.acos((a * a + c * c - b * b) / (2 * a * c)));
    }

    public static double getDistance(STBTerminal point1, STBTerminal point2) {
        return getDistance(point1.getLocation().xProperty().get(), point1.getLocation().yProperty().get(), point2.getLocation().xProperty().get(), point2.getLocation().yProperty().get());
    }

    public static double getDistance(STBTerminal point, double x, double y) {
        return getDistance(point.getLocation().xProperty().get(), point.getLocation().yProperty().get(), x, y);
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
