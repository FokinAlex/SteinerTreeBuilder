package utils;

import core.implementations.euclidean.EuclideanEdge;
import core.implementations.euclidean.EuclideanGraph;
import core.implementations.euclidean.EuclideanLocation;
import core.implementations.euclidean.EuclideanTerminal;
import core.interfaces.STBEdge;
import core.interfaces.STBGraph;
import core.interfaces.STBTerminal;
import core.types.STBTerminalType;
import javafx.util.Pair;
import utils.jdtriangulator.DelaunayTriangulator;
import utils.jdtriangulator.NotEnoughPointsException;
import utils.jdtriangulator.Vector2D;

import java.util.*;

public class AlgorithmsUtils {

    public static EuclideanGraph clone(EuclideanGraph graph) {
        try {
            return graph.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void triangleSteinerTree(EuclideanGraph graph, long point1_id, long point2_id, EuclideanTerminal newPoint) {
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
        triangleSteinerTree(graph, new Triad<>(point1, point2, newPoint));
    }

    public static void triangleSteinerTree(EuclideanGraph graph, Triad<EuclideanTerminal> triangle) {
        EuclideanTerminal steinerPoint = steinerPoint(graph, triangle);
        if (triangle.a.equals(steinerPoint)) {
            graph.addEdge(new EuclideanEdge(triangle.b, triangle.a));
            graph.addEdge(new EuclideanEdge(triangle.c, triangle.a));
        } else if (triangle.b.equals(steinerPoint)) {
            graph.addEdge(new EuclideanEdge(triangle.a, triangle.b));
            graph.addEdge(new EuclideanEdge(triangle.c, triangle.b));
        } else if (triangle.c.equals(steinerPoint)) {
            graph.addEdge(new EuclideanEdge(triangle.a, triangle.c));
            graph.addEdge(new EuclideanEdge(triangle.b, triangle.c));
        } else {
            graph.addVertex(steinerPoint);
            graph.addEdge(new EuclideanEdge(triangle.a, steinerPoint));
            graph.addEdge(new EuclideanEdge(triangle.b, steinerPoint));
            graph.addEdge(new EuclideanEdge(triangle.c, steinerPoint));
        }
    }

    public static EuclideanTerminal steinerPoint(STBGraph graph, Triad<EuclideanTerminal> triangle) {
        if (angle(triangle.b, triangle.a, triangle.c) > 120) return triangle.a;
        if (angle(triangle.a, triangle.b, triangle.c) > 120) return triangle.b;
        if (angle(triangle.b, triangle.c, triangle.a) > 120) return triangle.c;
        Pair<Double, Double> extraPoint1 = getThirdEquilateralTrianglePoint(triangle.a, triangle.b, triangle.c);
        Pair<Double, Double> extraPoint2 = getThirdEquilateralTrianglePoint(triangle.b, triangle.c, triangle.a);
        Pair<Double, Double> intersection = getIntersection(
                extraPoint1.getKey(), extraPoint1.getValue(),
                triangle.c.getLocation().xProperty().get(), triangle.c.getLocation().yProperty().get(),
                extraPoint2.getKey(), extraPoint2.getValue(),
                triangle.a.getLocation().xProperty().get(), triangle.a.getLocation().yProperty().get()
        );
        EuclideanTerminal steinerPoint = new EuclideanTerminal(new EuclideanLocation(intersection.getKey(), intersection.getValue()), IdUtils.getTerminalId());
        steinerPoint.typeProperty().setValue(STBTerminalType.STEINER_TERMINAL);
        return steinerPoint;
    }

    public static Pair<Double, Double> triangleLocalOptimization(Triad<EuclideanTerminal> triangle) {
        if (angle(triangle.b, triangle.a, triangle.c) > 120) return new Pair<>(triangle.a.getLocation().xProperty().get(), triangle.a.getLocation().yProperty().get());
        if (angle(triangle.a, triangle.b, triangle.c) > 120) return new Pair<>(triangle.b.getLocation().xProperty().get(), triangle.b.getLocation().yProperty().get());
        if (angle(triangle.b, triangle.c, triangle.a) > 120) return new Pair<>(triangle.c.getLocation().xProperty().get(), triangle.c.getLocation().yProperty().get());
        Pair<Double, Double> extraPoint1 = getThirdEquilateralTrianglePoint(triangle.a, triangle.b, triangle.c);
        Pair<Double, Double> extraPoint2 = getThirdEquilateralTrianglePoint(triangle.b, triangle.c, triangle.a);
        return getIntersection(
                extraPoint1.getKey(), extraPoint1.getValue(),
                triangle.c.getLocation().xProperty().get(), triangle.c.getLocation().yProperty().get(),
                extraPoint2.getKey(), extraPoint2.getValue(),
                triangle.a.getLocation().xProperty().get(), triangle.a.getLocation().yProperty().get()
        );
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

    public static Map<Pair<EuclideanTerminal, EuclideanTerminal>, Double> getCompleteStructure(EuclideanGraph graph) {
        Map<Pair<EuclideanTerminal, EuclideanTerminal>, Double> result = new HashMap<>();
        ArrayList tempList = new ArrayList(graph.getAllVertexes());
        Iterator iterator = tempList.iterator();
        while (iterator.hasNext()) {
            EuclideanTerminal terminal = (EuclideanTerminal) iterator.next();
            iterator.remove();
            tempList.forEach(otherTerminal -> result.put(new Pair(terminal, otherTerminal), getDistance(terminal, (STBTerminal) otherTerminal)));
        }
        return result;
    }

    public static boolean isTree(STBGraph graph) throws StackOverflowError {
        Map<STBTerminal, Boolean> terminals = new HashMap<>();
        graph.getAllVertexes().forEach(terminal -> terminals.put((STBTerminal) terminal, false));

        Iterator<STBTerminal> iterator = terminals.keySet().iterator();
        while (iterator.hasNext()) {
            STBTerminal terminal = iterator.next();
            if (hasCycle(terminal, terminals, null, graph)) return false;
            terminals.entrySet().forEach(entry -> entry.setValue(false));
        }
        return true;
    }

    private static boolean hasCycle(STBTerminal terminal, Map<STBTerminal, Boolean> terminals, STBTerminal parent, STBGraph graph) {
        if (terminals.get(terminal)) return true;
        terminals.put(terminal, true);
        Iterator<STBEdge> iterator = graph.getAllEdges().iterator();
        while (iterator.hasNext()) {
            STBEdge edge = iterator.next();
            if (edge.getFirstEndpoint().equals(terminal) && !edge.getSecondEndpoint().equals(parent)) {
                if (hasCycle(edge.getSecondEndpoint(), terminals, terminal, graph)) return true;
            }
            if (edge.getSecondEndpoint().equals(terminal) && !edge.getFirstEndpoint().equals(parent)) {
                if (hasCycle(edge.getFirstEndpoint(), terminals, terminal, graph)) return true;
            }
        }
        return false;
    }

    public static void clear(STBGraph graph) {
        Iterator iterator;
        iterator = graph.getAllEdges().iterator();
        while (iterator.hasNext() && null != iterator.next())
            iterator.remove();
        iterator = graph.getAllVertexes().iterator();
        while (iterator.hasNext())
            if (!STBTerminalType.SIMPLE_TERMINAL.equals(((STBTerminal) iterator.next()).typeProperty().getValue()))
                iterator.remove();
    }

    public static List<Triad<EuclideanTerminal>> triangulation(STBGraph graph) {
        Map<Vector2D, EuclideanTerminal> points = new HashMap<>();
        graph.getAllVertexes().forEach(vertex ->
                points.put(new Vector2D(((STBTerminal) vertex).getLocation().xProperty().get(), ((STBTerminal) vertex).getLocation().yProperty().get()), (EuclideanTerminal) vertex));

        DelaunayTriangulator triangulator = new DelaunayTriangulator(new ArrayList<>(points.keySet()));
        try {
            triangulator.triangulate();
        } catch (NotEnoughPointsException e) {
            return null;
        }

        ArrayList<Triad<EuclideanTerminal>> output = new ArrayList<>();
        triangulator.getTriangles().forEach(triangle -> output.add(new Triad<>(points.get(triangle.a), points.get(triangle.b), points.get(triangle.c))));

        return output;
    }

    public static int sing(double value) {
        return value == 0 ? 0 : value > 0 ? 1 : -1;
    }

    public static Pair<EuclideanTerminal, EuclideanTerminal> getSeparatingSide(EuclideanTerminal p1, EuclideanTerminal p2, Triad<EuclideanTerminal> p1Triangle) {
        Pair<EuclideanTerminal, EuclideanTerminal> ab = new Pair<>(p1Triangle.a, p1Triangle.b);
        Pair<EuclideanTerminal, EuclideanTerminal> bc = new Pair<>(p1Triangle.b, p1Triangle.c);
        Pair<EuclideanTerminal, EuclideanTerminal> ca = new Pair<>(p1Triangle.c, p1Triangle.a);
        if (isSeparating(p1, p2, ab)) return ab;
        if (isSeparating(p1, p2, bc)) return bc;
        if (isSeparating(p1, p2, ca)) return ca;
        return null;
    }

    private static boolean isSeparating(EuclideanTerminal p1, EuclideanTerminal p2, Pair<EuclideanTerminal, EuclideanTerminal> side) {
        double Ax = side.getKey().getLocation().xProperty().get();
        double Ay = side.getKey().getLocation().yProperty().get();
        double Bx = side.getValue().getLocation().xProperty().get();
        double By = side.getValue().getLocation().yProperty().get();
        double p1x = p1.getLocation().xProperty().get();
        double p1y = p1.getLocation().yProperty().get();
        double p2x = p2.getLocation().xProperty().get();
        double p2y = p2.getLocation().yProperty().get();
        double position1 = sing((Bx - Ax) * (p1y - Ay) - (By - Ay) * (p1x - Ax));
        double position2 = sing((Bx - Ax) * (p2y - Ay) - (By - Ay) * (p2x - Ax));
        return position1 != position2;
    }

    public static final double m = 10;
    public static final double M = 70;
    public static final double TIME = 1;
    public static Pair<Double, Double> getForce(EuclideanTerminal terminal1, EuclideanTerminal terminal2) {
        double t1Mass = terminal1.typeProperty().getValue().equals(STBTerminalType.SIMPLE_TERMINAL) ? M : m;
        double t2Mass = terminal2.typeProperty().getValue().equals(STBTerminalType.SIMPLE_TERMINAL) ? M : m;
        double dx = terminal2.getLocation().xProperty().get() - terminal1.getLocation().xProperty().get();
        double dy = terminal2.getLocation().yProperty().get() - terminal1.getLocation().yProperty().get();
        double force = t1Mass * t2Mass / (getDistance(terminal1, terminal2) * getDistance(terminal1, terminal2));
        double angle = Math.atan2(dy, dx);
        return new Pair<>(force * Math.cos(angle), force * Math.sin(angle));
    }

    public static Pair<Double, Double> getResultForce(EuclideanTerminal point, EuclideanTerminal forecer1, EuclideanTerminal forecer2, EuclideanTerminal forecer3) {
        Pair<Double, Double> force1 = getForce(point, forecer1);
        Pair<Double, Double> force2 = getForce(point, forecer2);
        Pair<Double, Double> force3 = getForce(point, forecer3);
        return new Pair<>(
                force1.getKey() + force2.getKey() + force3.getKey(),
                force1.getValue() + force2.getValue() + force3.getValue()
        );
    }
}
