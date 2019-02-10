package CGProject;

import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import shapes.BaseShape;
import shapes.ComplexShape;
import shapes.SimpleShape;

import java.util.ArrayList;

public class Group {
    private ArrayList<BaseShape> shapes = new ArrayList<>();

    public Group (ArrayList<BaseShape> shapes) {
        this.shapes = shapes;

        for (BaseShape baseShape : shapes) {
            if (baseShape.isComplex()) {
                ComplexShape complexShape = (ComplexShape)baseShape;
                complexShape.setPartOfGroup(true);
                complexShape.setGroup(this);

                for (SimpleShape simpleShape : complexShape.getShapes()) {
                    simpleShape.setPartOfGroup(true);
                    simpleShape.setGroup(this);
                }
            } else {
                baseShape.setPartOfGroup(true);
                baseShape.setGroup(this);
            }
        }
    }

    public Vector2d getCenterPoint () {
        Double[] points = new Double[4];
        for (BaseShape shape : shapes) {
            if (shape.isComplex()) {
                ArrayList<SimpleShape> simpleShapes = ((ComplexShape) shape).getShapes();

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

                if (points[0] != null) {
                    points[0] = (minX < points[0]) ? minX : points[0];
                    points[1] = (maxX > points[1]) ? maxX : points[1];
                    points[2] = (minY < points[2]) ? minY : points[2];
                    points[3] = (maxY > points[3]) ? maxY : points[3];
                } else {
                    points[0] = minX;
                    points[1] = maxX;
                    points[2] = minY;
                    points[3] = maxY;
                }
            } else {
                SimpleShape simpleShape = (SimpleShape) shape;
                Vector2d tempTopLeftPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMinX(), simpleShape.getShape().getBoundsInLocal().getMinY());
                Vector2d tempTopRightPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMaxX(), simpleShape.getShape().getBoundsInLocal().getMinY());
                Vector2d tempBottomLeftPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMinX(), simpleShape.getShape().getBoundsInLocal().getMaxY());
                Vector2d tempBottomRightPoint = new Vector2d(simpleShape.getShape().getBoundsInLocal().getMaxX(), simpleShape.getShape().getBoundsInLocal().getMaxY());

                double minX, minY, maxX, maxY;
                minX = ((tempTopLeftPoint.x < tempBottomLeftPoint.x) ? tempTopLeftPoint.x : tempBottomLeftPoint.x);
                maxX = ((tempBottomRightPoint.x > tempTopRightPoint.x) ? tempBottomRightPoint.x : tempTopRightPoint.x);
                minY = ((tempTopLeftPoint.y < tempTopRightPoint.y) ? tempTopLeftPoint.y : tempTopRightPoint.y);
                maxY = ((tempBottomLeftPoint.y > tempBottomRightPoint.y) ? tempBottomLeftPoint.y : tempBottomRightPoint.y);

                if (points[0] != null) {
                    points[0] = (minX < points[0]) ? minX : points[0];
                    points[1] = (maxX > points[1]) ? maxX : points[1];
                    points[2] = (minY < points[2]) ? minY : points[2];
                    points[3] = (maxY > points[3]) ? maxY : points[3];
                } else {
                    points[0] = minX;
                    points[1] = maxX;
                    points[2] = minY;
                    points[3] = maxY;
                }
            }
        }

        Vector2d centerPoint = new Vector2d(((points[1] - points[0]) / 2) + points[0], ((points[3] - points[2]) / 2) + points[2]);

        return centerPoint;
    }

    public void translate (double x, double y) {
        for (BaseShape shape : shapes) {
            if (shape.isComplex()) {
                ComplexShape complexShape = (ComplexShape) shape;
                complexShape.translate(x, y);
            } else {
                SimpleShape simpleShape = (SimpleShape) shape;
                simpleShape.translate(x, y);
            }
        }
    }

    public void rotate(double angle) {
        for (BaseShape baseShape : shapes) {
            if (baseShape.isComplex()) {
                for (SimpleShape simpleShape : ((ComplexShape) baseShape).getShapes()) {
                    Shape shape = simpleShape.getShape();
                    Rotate newRotation = new Rotate(angle, getCenterPoint().x, getCenterPoint().y);
                    shape.getTransforms().add(newRotation);
                }
            } else {
                SimpleShape simpleShape = (SimpleShape) baseShape;
                Shape shape = simpleShape.getShape();
                Rotate newRotation = new Rotate(angle, getCenterPoint().x, getCenterPoint().y);
                shape.getTransforms().add(newRotation);
            }
        }
    }
}
