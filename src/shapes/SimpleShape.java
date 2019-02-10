package shapes;

import CGProject.ShapeType;
import CGProject.Vector2d;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class SimpleShape extends BaseShape implements Serializable {
    private transient Shape shape;
    private ShapeType shapeType;
    private transient Bounds boundsInParent, boundsInLocal;
    protected boolean isPartOfComplex = false;

    public SimpleShape (Shape shape, ShapeType shapeType, Color strokeColor, Color fillColor, int lineWidth) {
        super.addTransforms(shape);
        this.boundsInParent = shape.getBoundsInParent();
        this.boundsInLocal = shape.getBoundsInLocal();

        this.shape = shape;
        this.shapeType = shapeType;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.lineWidth = lineWidth;
        complex = false;
    }

    public Shape getShape () {
        return shape;
    }

    public ShapeType getShapeType () {
        return shapeType;
    }

    public Vector2d getCenterPoint () {
//        return new Vector2d((this.boundsInParent.getMinX() + (this.boundsInLocal.getWidth() / 2)),
//                                (this.boundsInParent.getMinY() + (this.boundsInLocal.getHeight() / 2)));
        return new Vector2d((this.boundsInParent.getMinX() + ((this.boundsInParent.getMaxX() - this.boundsInParent.getMinX()) / 2)),
                (this.boundsInParent.getMinY() + ((this.boundsInParent.getMaxY() - this.boundsInParent.getMinY()) / 2 )));
    }

    public void translate (double x, double y) {
        Translate currentTranslate = (Translate)shape.getTransforms().get(0);
        Translate translate = new Translate((currentTranslate.getX() + x), (currentTranslate.getY() + y));
        shape.getTransforms().set(0, translate);
    }

    /**
     * Rotates this shape by specified angle in degrees
     */
    public void rotate (double angle) {
        Rotate rot = new Rotate(angle, getCenterPoint().x, getCenterPoint().y);
        Rotate currentRotation = (Rotate) shape.getTransforms().get(1);
        Rotate newRotation = new Rotate((currentRotation.getAngle() + rot.getAngle()), rot.getPivotX(), rot.getPivotY());
        shape.getTransforms().set(1, newRotation);
    }

    public void scale (double scaleFactor) {
        Scale scale = new Scale(scaleFactor, scaleFactor, getCenterPoint().x, getCenterPoint().y);
        Scale currentScale = (Scale) shape.getTransforms().get(2);
        Scale newScale = new Scale((currentScale.getX() + scaleFactor), (currentScale.getY() + scaleFactor), getCenterPoint().x, getCenterPoint().y);
        shape.getTransforms().set(2, newScale);
    }

    public void updateBounds() {
        this.boundsInLocal = this.shape.getBoundsInLocal();
        this.boundsInParent = this.shape.getBoundsInParent();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(shapeType.getValue());
        /*
        s.writeDouble(boundsInParent.getMinX());
        s.writeDouble(boundsInParent.getMinY());
        s.writeDouble(boundsInParent.getMaxX());
        s.writeDouble(boundsInParent.getMaxY());
        s.writeDouble(boundsInParent.getWidth());
        s.writeDouble(boundsInParent.getHeight());
        s.writeDouble(boundsInLocal.getMinX());
        s.writeDouble(boundsInLocal.getMinY());
        s.writeDouble(boundsInLocal.getMaxX());
        s.writeDouble(boundsInLocal.getMaxY());
        s.writeDouble(boundsInLocal.getWidth());
        s.writeDouble(boundsInLocal.getHeight());
         */
        s.writeBoolean(isPartOfComplex);
        switch (shapeType) {
            case LINE:
                s.writeDouble(((Line)getShape()).getStartX());
                s.writeDouble(((Line)getShape()).getStartY());
                s.writeDouble(((Line)getShape()).getEndX());
                s.writeDouble(((Line)getShape()).getEndY());
                break;
            case RECTANGLE:
                s.writeDouble(((Rectangle)getShape()).getX());
                s.writeDouble(((Rectangle)getShape()).getY());
                s.writeDouble(((Rectangle)getShape()).getWidth());
                s.writeDouble(((Rectangle)getShape()).getHeight());
                break;
            case ELLIPSE:
                s.writeDouble(((Ellipse)getShape()).getCenterX());
                s.writeDouble(((Ellipse)getShape()).getCenterY());
                s.writeDouble(((Ellipse)getShape()).getRadiusX());
                s.writeDouble(((Ellipse)getShape()).getRadiusY());
                break;
            case CIRCLE:
                s.writeDouble(((Circle)getShape()).getCenterX());
                s.writeDouble(((Circle)getShape()).getCenterY());
                s.writeDouble(((Circle)getShape()).getRadius());
                break;
            case TRIANGLE:
                ObservableList trianglePoints = ((Polygon)getShape()).getPoints();
                s.writeDouble(((Polygon)getShape()).getPoints().get(0));
                s.writeDouble(((Polygon)getShape()).getPoints().get(1));
                s.writeDouble(((Polygon)getShape()).getPoints().get(2));
                s.writeDouble(((Polygon)getShape()).getPoints().get(3));
                s.writeDouble(((Polygon)getShape()).getPoints().get(4));
                s.writeDouble(((Polygon)getShape()).getPoints().get(5));
                break;
        }
        Translate translation = (Translate)shape.getTransforms().get(0);
        Rotate rotation = (Rotate)shape.getTransforms().get(1);
        Scale scale = (Scale)shape.getTransforms().get(2);
        s.writeDouble(translation.getX());
        s.writeDouble(translation.getY());
        s.writeDouble(rotation.getAngle());
        s.writeDouble(rotation.getPivotX());
        s.writeDouble(rotation.getPivotY());
        s.writeDouble(scale.getX());
        s.writeDouble(scale.getY());
        s.writeDouble(scale.getPivotX());
        s.writeDouble(scale.getPivotY());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        shapeType = ShapeType.fromValue(s.readInt());
        //Skip bounds (12 double numbers (double = 64bit = 8byte))
        //s.skipBytes(12 * 8);
        /*
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        s.readDouble();
        */
        this.isPartOfComplex = s.readBoolean();
        switch (shapeType) {
            case LINE:
                this.shape = new Line(s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble());
                break;
            case RECTANGLE:
                this.shape = new Rectangle(s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble());
                break;
            case ELLIPSE:
                this.shape = new Ellipse(s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble());
                break;
            case CIRCLE:
                this.shape = new Circle(s.readDouble(), s.readDouble(), s.readDouble());
                break;
            case TRIANGLE:
                this.shape = new Polygon(s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble());
                break;
        }

        Translate translation = new Translate(s.readDouble(), s.readDouble());
        Rotate rotation = new Rotate(s.readDouble(), s.readDouble(), s.readDouble());
        Scale scale = new Scale(s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble());
        this.shape.getTransforms().add(translation);
        this.shape.getTransforms().add(rotation);
        this.shape.getTransforms().add(scale);

        this.boundsInParent = this.shape.getBoundsInParent();
        this.boundsInLocal = this.shape.getBoundsInLocal();
    }
}
