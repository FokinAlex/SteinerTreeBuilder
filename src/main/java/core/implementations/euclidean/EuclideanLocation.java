package core.implementations.euclidean;

import core.interfaces.STBLocation;

public class EuclideanLocation implements STBLocation {

    private double x;
    private double y;

    public EuclideanLocation(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return this.x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }
}
