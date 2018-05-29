package dai.entities;

import core.implementations.euclidean.EuclideanLocation;

import java.util.HashSet;
import java.util.Set;

public class ORLibraryTask {

    public final int id;
    private Set<EuclideanLocation> points;

    public ORLibraryTask(int id) {
        this.id = id;
        this.points = new HashSet<>();
    }

    public boolean addPoint(double x, double y) {
        return this.points.add(new EuclideanLocation(x, y));
    }

    public Set<EuclideanLocation> getPoints() {
        return this.points;
    }
}
