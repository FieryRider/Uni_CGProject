package shapes;

import CGProject.Vector2d;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.io.Serializable;
import java.util.ArrayList;

public class ComplexShape extends BaseShape implements Serializable {
    private ArrayList<SimpleShape> simpleShapes;

    public ComplexShape(ArrayList<SimpleShape> simpleShapes) {
        for (SimpleShape simpleShape : simpleShapes) {
            simpleShape.isPartOfComplex = true;
            super.addTransforms(simpleShape.getShape());
        }

        this.simpleShapes = simpleShapes;
        this.complex = true;
    }

    public ArrayList<SimpleShape> getShapes() {
        return simpleShapes;
    }

    public void updateBounds() {
        for (SimpleShape simpleShape : simpleShapes) {
            simpleShape.updateBounds();
        }
    }
    public Vector2d getCenterPoint () {
        Vector2d shapesTopLeftPoint = new Vector2d(simpleShapes.get(0).getShape().getBoundsInLocal().getMinX(), simpleShapes.get(0).getShape().getBoundsInLocal().getMinY());
        Vector2d shapesTopRightPoint = new Vector2d(simpleShapes.get(0).getShape().getBoundsInLocal().getMaxX(), simpleShapes.get(0).getShape().getBoundsInLocal().getMinY());
        Vector2d shapesBottomLeftPoint = new Vector2d(simpleShapes.get(0).getShape().getBoundsInLocal().getMinX(), simpleShapes.get(0).getShape().getBoundsInLocal().getMaxY());
        Vector2d shapesBottomRightPoint = new Vector2d(simpleShapes.get(0).getShape().getBoundsInLocal().getMaxX(), simpleShapes.get(0).getShape().getBoundsInLocal().getMaxY());

        for (SimpleShape simpleShape : simpleShapes) {
            Vector2d tempTopLeftPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMinX(), simpleShape.getShape().getBoundsInLocal().getMinY());
            Vector2d tempTopRightPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMaxX(), simpleShape.getShape().getBoundsInLocal().getMinY());
            Vector2d tempBottomLeftPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMinX(), simpleShape.getShape().getBoundsInLocal().getMaxY());
            Vector2d tempBottomRightPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMaxX(), simpleShape.getShape().getBoundsInLocal().getMaxY());

            //check
            shapesTopLeftPoint.set(((tempTopLeftPoint.x < shapesTopLeftPoint.x) ? tempTopLeftPoint.x : shapesTopLeftPoint.x), ((tempTopLeftPoint.y < shapesTopLeftPoint.y) ? tempTopLeftPoint.y : shapesTopLeftPoint.y));
            shapesTopRightPoint.set(((tempTopRightPoint.x > shapesTopRightPoint.x) ? tempTopRightPoint.x : shapesTopRightPoint.x), ((tempTopRightPoint.y < shapesTopRightPoint.y) ? tempTopRightPoint.y : shapesTopRightPoint.y));
            shapesBottomLeftPoint.set(((tempBottomLeftPoint.x < shapesBottomLeftPoint.x) ? tempBottomLeftPoint.x : shapesBottomLeftPoint.x), ((tempBottomLeftPoint.y > shapesBottomLeftPoint.y) ? tempBottomLeftPoint.y : shapesBottomLeftPoint.y));
            shapesBottomRightPoint.set(((tempBottomRightPoint.x > shapesBottomRightPoint.x) ? tempBottomRightPoint.x : shapesBottomRightPoint.x), ((tempBottomRightPoint.y > shapesBottomRightPoint.y) ? tempBottomRightPoint.y : shapesBottomRightPoint.y));
        }

        double minX, maxX, minY, maxY;
        minX = ((shapesTopLeftPoint.x < shapesBottomLeftPoint.x) ? shapesTopLeftPoint.x : shapesBottomLeftPoint.x);
        maxX = ((shapesBottomRightPoint.x > shapesTopRightPoint.x) ? shapesBottomRightPoint.x : shapesTopRightPoint.x);
        minY = ((shapesTopLeftPoint.y < shapesTopRightPoint.y) ? shapesTopLeftPoint.y : shapesTopRightPoint.y);
        maxY = ((shapesBottomLeftPoint.y > shapesBottomRightPoint.y) ? shapesBottomLeftPoint.y : shapesBottomRightPoint.y);
        Vector2d topLeftPoint = new Vector2d(minX, minY);
        Vector2d topRightPoint = new Vector2d(maxX, minY);
        Vector2d bottomLeftPoint = new Vector2d(minX, maxY);
        Vector2d bottomRightPoint = new Vector2d(maxX, maxY);

        Vector2d centerPoint = new Vector2d(((maxX - minX) / 2) + minX, ((maxY - minY) / 2) + minY);

        return centerPoint;
    }

    public void translate (double x, double y) {
        for (SimpleShape simpleShape : simpleShapes) {
            Shape shape = simpleShape.getShape();
            Translate currentTranslate = (Translate)shape.getTransforms().get(3);
            Translate translate = new Translate((currentTranslate.getX() + x), (currentTranslate.getY() + y));
            shape.getTransforms().set(3, translate);
        }
    }

    /**
     * Rotates this shape by specified angle in degrees
     */
    public void rotate (double angle) {
        for (SimpleShape simpleShape : simpleShapes) {
            Shape shape = simpleShape.getShape();

            Rotate rot = new Rotate(angle, getCenterPoint().x, getCenterPoint().y);
            Rotate currentRotation = (Rotate) shape.getTransforms().get(4);
            Rotate newRotation = new Rotate((currentRotation.getAngle() + rot.getAngle()), rot.getPivotX(), rot.getPivotY());
            shape.getTransforms().set(4, newRotation);
        }
    }

    public void scale (double scaleFactor) {
		for (SimpleShape simpleShape : simpleShapes) {
			Shape shape = simpleShape.getShape();
			Scale scale = new Scale(scaleFactor, scaleFactor, getCenterPoint().x, getCenterPoint().y);
			Scale currentScale = (Scale) shape.getTransforms().get(5);
			Scale newScale = new Scale((currentScale.getX() + scaleFactor), (currentScale.getY() + scaleFactor), getCenterPoint().x, getCenterPoint().y);
			shape.getTransforms().set(5, newScale);
		}
/*        Scale scale = new Scale(scaleFactor, scaleFactor, getCenterPoint().x, getCenterPoint().y);
        Scale currentScale = (Scale) shape.getTransforms().get(2);
        Scale newScale = new Scale((currentScale.getX() + scaleFactor), (currentScale.getY() + scaleFactor), getCenterPoint().x, getCenterPoint().y);
        shape.getTransforms().set(2, newScale);*/
    }
}
