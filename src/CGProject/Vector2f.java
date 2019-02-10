package CGProject;

import com.sun.javafx.geom.Vec2d;
import com.sun.javafx.geom.Vec2f;

import java.io.Serializable;

public class Vector2f extends Vec2f implements Serializable{

    public Vector2f(float x, float y) {
        super(x, y);
    }

    public Vector2f(Vec2f v) {
        super(v);
    }

    public Vector2f(Vec2d v) {
        super(new Vec2f((float)v.x, (float)v.y));
    }

    public Vector2f() {
        super();
    }

    public static Vector2f add (Vector2f v1, Vector2f v2) {
        return new Vector2f((v1.x + v2.x), (v1.y + v2.y));
    }

    public static Vector2f sub (Vector2f v1, Vector2f v2) {
        return new Vector2f((v1.x - v2.x), (v1.y + v2.y));
    }

    public static Vector2f multiply (Vector2f v, float m) {
        return new Vector2f((v.x * m), (v.y * m));
    }

    public static Vector2f devide (Vector2f v, float m) {
        return new Vector2f((v.x / m), (v.y / m));
    }

    public static double length (Vector2f v) {
        return Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
    }

    public void negate () {
        this.x = -this.x;
        this.y = -this.y;
    }

    public void negate (Vector2f origin) {
        Vector2f vec = Vector2f.sub(this, origin);
        vec.negate();
        vec = Vector2f.add(origin, vec);
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vector2f oppositeVector () {
        return new Vector2f(-this.x, -this.y);
    }

    public Vector2f oppositeVector (Vector2f origin) {
        Vector2f vec = Vector2f.sub(this, origin);
        vec.negate();
        vec = Vector2f.add(origin, vec);
        return new Vector2f(vec.x, vec.y);
    }
}

