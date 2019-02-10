package CGProject;

import javafx.geometry.Point2D;

public class Vector2d extends Point2D {

    public double x = getX();
    public double y = getY();

    public Vector2d(double x, double y) {
        super(x, y);
    }

    public Vector2d(Vector2d v) {
        super(v.x, v.y);
    }

    public Vector2d() {
        super(0.0, 0.0);
        this.x = 0.0;
        this.y = 0.0;
    }

    //Static methods

    public static Vector2d add (Vector2d v1, Vector2d v2) {
        return new Vector2d((v1.x + v2.x), (v1.y + v2.y));
    }

    public static Vector2d sub (Vector2d v1, Vector2d v2) {
        return new Vector2d((v1.x - v2.x), (v1.y - v2.y));
    }

    public static Vector2d multiply (Vector2d v, float m) {
        return new Vector2d((v.x * m), (v.y * m));
    }

    public static Vector2d devide (Vector2d v, float m) {
        return new Vector2d((v.x / m), (v.y / m));
    }

    public static double length (Vector2d v) {
        return Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
    }

    public static Vector2d normalize (Vector2d v) {
        double d = length(v);

        return new Vector2d((v.x / d), (v.y / d));
    }

    public static Vector2d oppositeVector (Vector2d v) {
        return new Vector2d(-v.x, -v.y);
    }

    public static Vector2d oppositeVector (Vector2d v, Vector2d origin) {
        Vector2d vec = Vector2d.sub(v, origin);
        vec.negate();
        vec = Vector2d.add(origin, vec);
        return new Vector2d(vec.x, vec.y);
    }

    public static Vector2d rotateCW (Vector2d v) {
        Vector2d rotatedVector = new Vector2d(v);
        rotatedVector.rotateCW();
        return rotatedVector;
    }

    public static Vector2d rotateCCW (Vector2d v) {
        Vector2d rotatedVector = new Vector2d(v);
        rotatedVector.rotateCCW();
        return rotatedVector;
    }

    public static double angleBetween(Vector2d v1, Vector2d v2) {
        Point2D p1 = new Point2D(v1.x, v1.y);
        Point2D p2 = new Point2D(v2.x, v2.y);

        return p1.angle(p2);
    }

    public static double angleBetween(Vector2d v1, Vector2d v2, Vector2d origin) {
        Point2D p1 = new Point2D((v1.x - origin.x), (v1.y - origin.y));
        Point2D p2 = new Point2D((v2.x - origin.x), (v2.y - origin.y));

        return p1.angle(p2);
    }
    //Non-static methods

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void negate () {
        double origX = this.x;
        double origY = this.y;
        this.x = -origX;
        this.y = -origY;
    }

    public void negate (Vector2d origin) {
        Vector2d vec = Vector2d.sub(this, origin);
        vec.negate();
        vec = Vector2d.add(origin, vec);
        this.x = vec.x;
        this.y = vec.y;
    }

    public void rotateCW () {
        double origX = this.x;
        double origY = this.y;
        this.x = origY;
        this.y = -origX;
    }

    public void rotateCCW () {
        rotateCW();
        negate();
    }
}
