package CGProject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.w3c.dom.css.Rect;
import shapes.BaseShape;
import shapes.ComplexShape;
import shapes.SimpleShape;

import java.io.*;
import java.util.ArrayList;

public class Controller {

/*    @FXML
    private Canvas canvas;*/

    @FXML
    private Pane drawingPane;

    @FXML
    private Slider strokeOpacitySlider;

    @FXML
    private Slider fillOpacitySlider;

    @FXML
    private ChoiceBox<String> lineWidthChoiceBox;

    @FXML
    private ColorPicker strokeColorPicker;

    @FXML
    private ColorPicker fillColorPicker;

    public void setTool(Tools tool) {
        this.tool = tool;
    }

    private Tools tool;
    private ArrayList<BaseShape> shapes = new ArrayList<>();
    private BaseShape selectedShape;

    protected Color strokeColor;
    protected Color fillColor;
    protected int lineWidth;

    private Vector2d mouseBeginLocation = new Vector2d();
    private Vector2d mouseEndLocation = new Vector2d();
    private Vector2d mouseLastLocation = new Vector2d();
    private boolean mouseLastLocationSet = false;

    public void initialize() {
        strokeColorPicker.setValue(new Color(0, 0, 0, 1.0));
        fillColorPicker.setValue(new Color(0, 0, 0, 0.0));
        drawingPane.setOnMousePressed(e -> {
            mouseBeginLocation.set(e.getX(), e.getY());
            switch (tool) {
                case LINE:
                    Line line = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    shapes.add(new SimpleShape(line, ShapeType.LINE, strokeColor, fillColor, lineWidth));
                    break;
                case RECTANGLE:
                    Rectangle rectangle = new Rectangle(e.getX(), e.getY(), 0, 0);
                    shapes.add(new SimpleShape(rectangle, ShapeType.RECTANGLE, strokeColor, fillColor, lineWidth));
                    break;
                case ELLIPSE:
                    Ellipse ellipse = new Ellipse(e.getX(), e.getY(), 0, 0);
                    shapes.add(new SimpleShape(ellipse, ShapeType.ELLIPSE, strokeColor, fillColor, lineWidth));
                    break;
                case CIRCLE:
                    Circle circle = new Circle(e.getX(), e.getY(), 0);
                    shapes.add(new SimpleShape(circle, ShapeType.CIRCLE, strokeColor, fillColor, lineWidth));
                    break;
                case TRIANGLE:
                    Polygon polygon = new Polygon(mouseBeginLocation.x, mouseBeginLocation.y, mouseBeginLocation.x, mouseBeginLocation.y, mouseBeginLocation.x, mouseBeginLocation.y);
                    shapes.add(new SimpleShape(polygon, ShapeType.TRIANGLE, strokeColor, fillColor, lineWidth));
                    break;
                case COMPLEX1:
                    Rectangle rectangle1 = new Rectangle(e.getX(), e.getY(), 0, 0);
                    Line line1 = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    Line line2 = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    Line line3 = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    ArrayList<SimpleShape> simplShapes = new ArrayList<>();
                    simplShapes.add(new SimpleShape(rectangle1, ShapeType.RECTANGLE, strokeColor, fillColor, lineWidth));
                    simplShapes.add(new SimpleShape(line1, ShapeType.LINE, strokeColor, fillColor, lineWidth));
                    simplShapes.add(new SimpleShape(line2, ShapeType.LINE, strokeColor, fillColor, lineWidth));
                    simplShapes.add(new SimpleShape(line3, ShapeType.LINE, strokeColor, fillColor, lineWidth));
                    shapes.add(new ComplexShape(simplShapes));
                    break;
            }

            switch (tool) {
                case LINE:
                case RECTANGLE:
                case ELLIPSE:
                case CIRCLE:
                case TRIANGLE:
                case COMPLEX1:
                    selectedShape = shapes.get(shapes.size() - 1);
            }
            repaint();
        });

        drawingPane.setOnMouseDragged(e -> {
            mouseEndLocation.set(e.getX(), e.getY());

            if (!mouseLastLocationSet) {
                mouseLastLocation.set(mouseBeginLocation.x, mouseBeginLocation.y);
            }

/*            System.out.printf("begin: %f, %f    last: %f, %f       end: %f, %f \n", mouseBeginLocation.x, mouseBeginLocation.y, mouseLastLocation.x, mouseLastLocation.y,
                                                                                mouseEndLocation.x, mouseEndLocation.y);*/
            Vector2d shapeBoundsBeginLocation = new Vector2d(((e.getX() > mouseBeginLocation.x) ? mouseBeginLocation.x : e.getX()),
                    ((e.getY() > mouseBeginLocation.y) ? mouseBeginLocation.y : e.getY()));
            Vector2d shapeBoundsEndLocation = new Vector2d(((e.getX() > mouseBeginLocation.x) ? e.getX() : mouseBeginLocation.x),
                    ((e.getY() > mouseBeginLocation.y) ? e.getY() : mouseBeginLocation.y));
            switch (tool) {
                case MOVE:
                    if (selectedShape != null) {
                        if (!selectedShape.isComplex()) {
                            ((SimpleShape)selectedShape).translate((mouseEndLocation.x - mouseLastLocation.x), (mouseEndLocation.y - mouseLastLocation.y));
                        } else {
                            ((ComplexShape)selectedShape).translate((mouseEndLocation.x - mouseLastLocation.x), (mouseEndLocation.y - mouseLastLocation.y));
                        }
                    }
                    break;
                case ROTATE:
                    if (selectedShape != null) {
                        if (!selectedShape.isComplex()) {
                            SimpleShape selShape = (SimpleShape)selectedShape;
                            if (mouseLastLocationSet) {
                                double angle = calculateRotationAngle(selShape.getCenterPoint());
                                selShape.rotate(angle);
                            }
                        } else {
                            ComplexShape selShape = (ComplexShape)selectedShape;
                            double angle = calculateRotationAngle(selShape.getCenterPoint());
                            selShape.rotate(angle);
                        }
                    }
                    break;
                case SCALE:
                    if (selectedShape != null) {
                        if (!selectedShape.isComplex()) {
                            SimpleShape selShape = (SimpleShape)selectedShape;
                            double centerToMouseLastLoc = Vector2d.length(Vector2d.sub(mouseLastLocation, selShape.getCenterPoint()));
                            double centerToMouseEndLoc = Vector2d.length(Vector2d.sub(mouseEndLocation, selShape.getCenterPoint()));
                            if (centerToMouseEndLoc > centerToMouseLastLoc) {
                                selShape.scale(0.005);
                            } else {
                                selShape.scale(-0.005);
                            }
                        } else {
                            ComplexShape selShape = (ComplexShape)selectedShape;
                            double centerToMouseLastLoc = Vector2d.length(Vector2d.sub(mouseLastLocation, selShape.getCenterPoint()));
                            double centerToMouseEndLoc = Vector2d.length(Vector2d.sub(mouseEndLocation, selShape.getCenterPoint()));
                            if (centerToMouseEndLoc > centerToMouseLastLoc)
                                selShape.scale(0.005);
                            else
                                selShape.scale(-0.005);
                        }
                    }
                    break;
                case LINE:
                    Line line = new Line(mouseBeginLocation.x, mouseBeginLocation.y, mouseEndLocation.x, mouseEndLocation.y);
                    shapes.set((shapes.size() - 1), new SimpleShape(line, ShapeType.LINE, strokeColor, fillColor, lineWidth));
                    break;
                case RECTANGLE:
                    Rectangle rectangle = new Rectangle(shapeBoundsBeginLocation.x, shapeBoundsBeginLocation.y,
                            (shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x), (shapeBoundsEndLocation.y - shapeBoundsBeginLocation.y));
                    shapes.set((shapes.size() - 1), new SimpleShape(rectangle, ShapeType.RECTANGLE, strokeColor, fillColor, lineWidth));
                    break;
                case ELLIPSE:
                    double centerX = (shapeBoundsBeginLocation.x +((shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x) / 2));
                    double centerY = (shapeBoundsBeginLocation.y + ((shapeBoundsEndLocation.y - shapeBoundsBeginLocation.y) / 2));
                    double radiusX = centerX - shapeBoundsBeginLocation.x;
                    double radiusY = centerY - shapeBoundsBeginLocation.y;
                    Ellipse ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
                    shapes.set((shapes.size() - 1), new SimpleShape(ellipse, ShapeType.ELLIPSE, strokeColor, fillColor, lineWidth));
                    break;
                case CIRCLE:
                    double xDiameter = shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x;
                    double yDiameter = shapeBoundsEndLocation.y - shapeBoundsBeginLocation.y;
                    double radius = (xDiameter > yDiameter) ? (xDiameter / 2) : (yDiameter / 2);
                    centerX = shapeBoundsBeginLocation.x + radius;
                    centerY = shapeBoundsBeginLocation.y + radius;

                    Circle circle = new Circle(centerX, centerY, radius);
                    shapes.set((shapes.size() - 1), new SimpleShape(circle, ShapeType.CIRCLE, strokeColor, fillColor, lineWidth));
                    break;
                case TRIANGLE:
                    Vector2d centerToTip = Vector2d.sub(mouseEndLocation, mouseBeginLocation);
                    Vector2d centerToBase = Vector2d.oppositeVector(centerToTip);
                    Vector2d base = Vector2d.add(mouseBeginLocation, centerToBase);
                    double height = Vector2d.length(Vector2d.multiply(centerToBase, 2.f));
                    double triangleSideLength = Math.sqrt((4 * Math.pow(height, 2.0)) / 3);

                    Vector2d temp1 = Vector2d.normalize(Vector2d.rotateCW(centerToBase));
                    Vector2d temp2 = Vector2d.normalize(Vector2d.rotateCCW(centerToBase));
                    Vector2d tmp1 = Vector2d.rotateCW(centerToBase);
                    Vector2d tmp2 = Vector2d.rotateCCW(centerToBase);
                    System.out.printf("%f %f \n", tmp1.x, tmp2.x);
                    Vector2d leftCorner = (Vector2d.add(base, Vector2d.multiply(temp1, (float)triangleSideLength)));
                    Vector2d rightCorner = (Vector2d.add(base, Vector2d.multiply(temp2, (float)triangleSideLength)));
                    double[] points = new double[]{
                            leftCorner.x, leftCorner.y,
                            rightCorner.x, rightCorner.y,
                            e.getX(), e.getY()
                    };
                    Polygon polygon = new Polygon(points);
                    shapes.set((shapes.size() - 1), new SimpleShape(polygon, ShapeType.TRIANGLE, strokeColor, fillColor, lineWidth));

                    break;
                case COMPLEX1:
                    Rectangle rectangle1 = new Rectangle(shapeBoundsBeginLocation.x, shapeBoundsBeginLocation.y,
                            (shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x), (shapeBoundsEndLocation.y - shapeBoundsBeginLocation.y));

                    Line line1 = new Line(shapeBoundsBeginLocation.x, shapeBoundsBeginLocation.y,
                            (shapeBoundsBeginLocation.x + ((shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x) / 2)),
                            (shapeBoundsBeginLocation.y + ((shapeBoundsEndLocation.y  - shapeBoundsBeginLocation.y) / 2)));
                    Line line2 = new Line((shapeBoundsBeginLocation.x + ((shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x) / 2)),
                            (shapeBoundsBeginLocation.y + ((shapeBoundsEndLocation.y - shapeBoundsBeginLocation.y) / 2)),
                            shapeBoundsEndLocation.x, (shapeBoundsEndLocation.y - (shapeBoundsEndLocation.y - shapeBoundsBeginLocation.y)));
                    Line line3 = new Line((shapeBoundsBeginLocation.x + ((shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x) / 2)),
                            (shapeBoundsBeginLocation.y + ((shapeBoundsEndLocation.y - shapeBoundsBeginLocation.y) / 2)),
                            (shapeBoundsBeginLocation.x + ((shapeBoundsEndLocation.x - shapeBoundsBeginLocation.x) / 2)),
                            shapeBoundsEndLocation.y
                    );


                    ArrayList<SimpleShape> simplShapes = new ArrayList<>();
                    simplShapes.add(new SimpleShape(rectangle1, ShapeType.RECTANGLE, strokeColor, fillColor, lineWidth));
                    simplShapes.add(new SimpleShape(line1, ShapeType.LINE, strokeColor, fillColor, lineWidth));
                    simplShapes.add(new SimpleShape(line2, ShapeType.LINE, strokeColor, fillColor, lineWidth));
                    simplShapes.add(new SimpleShape(line3, ShapeType.LINE, strokeColor, fillColor, lineWidth));

                    shapes.set((shapes.size() - 1), new ComplexShape(simplShapes));
                    break;
            }
            repaint();

            mouseLastLocation.set(mouseEndLocation.x, mouseEndLocation.y);
            mouseLastLocationSet = true;
        });

        drawingPane.setOnMouseReleased(e -> {
            mouseLastLocationSet = false;
            if (this.tool == Tools.SELECT) {
                System.out.println("select");
            } else {
                if (selectedShape.isComplex()) {
                    ((ComplexShape) selectedShape).updateBounds();
                } else {
                    ((SimpleShape) selectedShape).updateBounds();
                }
            }
        });
/*        drawingPane.setOnMouseReleased(e -> {
            if (tool == Tools.SELECT) {
                for (int i = (shapes.size() - 1); i >= 0; i--) {
                    if (shapes.get(i).isComplex()) {

                    } else {
                    }
                }
            }
        });*/
    }

    private double calculateRotationAngle(Vector2d centerPoint) {
        double sAngle = Math.atan2((mouseLastLocation.y - centerPoint.y), (mouseLastLocation.x - centerPoint.x));
        double pAngle = Math.atan2((mouseEndLocation.y - centerPoint.y), (mouseEndLocation.x - centerPoint.x));
        double angle = (((pAngle - sAngle) * 180) / Math.PI);

        return angle;
    }

    private void repaint() {
        drawingPane.getChildren().clear();
        for (BaseShape baseShape : shapes) {
            if (baseShape.isComplex()) {
                ComplexShape complexShape = (ComplexShape)baseShape;
                for (SimpleShape simpleShape : complexShape.getShapes()) {
                    drawShape(simpleShape, baseShape);
                }
            } else {
                SimpleShape simpleShape = (SimpleShape)baseShape;
                drawShape(simpleShape, baseShape);
            }
        }
    }

    private void drawShape(SimpleShape simpleShape, BaseShape baseShape) {
        Shape shape = simpleShape.getShape();

        //Add mouse event handler to the shape so it can be selected
        shape.setOnMouseClicked(e -> {
            if (tool == Tools.SELECT) {
                selectedShape = baseShape;
                lineWidthChoiceBox.setValue(Integer.toString(selectedShape.getLineWidth()));
                strokeColorPicker.setValue(selectedShape.getStrokeColor());
                fillColorPicker.setValue(selectedShape.getFillColor());
            }
        });

        shape.setStroke(simpleShape.getStrokeColor());
        shape.setFill(simpleShape.getFillColor());
        shape.setStrokeWidth(simpleShape.getLineWidth());

        drawingPane.getChildren().add(shape);

        //Debug
        /*
        Rectangle rect = new Rectangle(simpleShape.getCenterPoint().getX(), simpleShape.getCenterPoint().getY(), 10, 10);
        Rectangle rect2 = new Rectangle(simpleShape.getShape().getBoundsInParent().getMinX(), simpleShape.getShape().getBoundsInParent().getMinY(),
                (simpleShape.getShape().getBoundsInParent().getMaxX() - simpleShape.getShape().getBoundsInParent().getMinX()),
                (simpleShape.getShape().getBoundsInParent().getMaxY() - simpleShape.getShape().getBoundsInParent().getMinY()));
        rect2.setStroke(Color.BLACK);
        rect2.setFill(null);
        drawingPane.getChildren().add(rect);
        drawingPane.getChildren().add(rect2);
         */
    }

    public void onSave() {
        try {
            FileOutputStream fos = new FileOutputStream("javaobjfile");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(shapes);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLoad() {
        try {
            FileInputStream fis = new FileInputStream("javaobjfile");
            ObjectInputStream ois = new ObjectInputStream(fis);
            shapes = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        repaint();
    }

    public void onExit() {
        Platform.exit();
    }

    public void handleSelectButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.SELECT;
    }

    public void handleMoveButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.MOVE;
    }

    public void handleRotateButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.ROTATE;
    }

    public void handleScaleButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.SCALE;
    }

    public void handleLineButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.LINE;
    }

    public void handleRectangleButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.RECTANGLE;
    }

    public void handleEllipseButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.ELLIPSE;
    }

    public void handleCircleButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.CIRCLE;
    }

    public void handleTriangleButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.TRIANGLE;
    }

    public void handleComplexOneButtonAction(ActionEvent actionEvent) {
        this.tool = Tools.COMPLEX1;
    }

    public void handleLineWidthChoiceBox(ActionEvent actionEvent) {
        this.lineWidth = Integer.valueOf(lineWidthChoiceBox.getValue());
        if (this.selectedShape != null)
            this.selectedShape.setLineWidth(this.lineWidth);
    }

    public void handleStrokeColorPicker(ActionEvent actionEvent) {
        this.strokeColor = strokeColorPicker.getValue();
        if (this.selectedShape != null)
            this.selectedShape.setStrokeColor(this.strokeColor);
    }

    public void handleFillColorPicker(ActionEvent actionEvent) {
        this.fillColor = fillColorPicker.getValue();
        if (this.selectedShape != null)
            this.selectedShape.setFillColor(this.fillColor);
    }

    public void handleStrokeOpacitySlider(DragEvent dragEvent) {
        int opacity = (int) strokeOpacitySlider.getValue();
        this.strokeColor = new Color(this.strokeColor.getRed(), this.strokeColor.getGreen(), this.strokeColor.getBlue(), ((double) opacity / 100.0));
        if (this.selectedShape != null)
            this.selectedShape.setStrokeColor(this.strokeColor);
    }

    public void handleFillOpacitySlider(DragEvent dragEvent) {
        int opacity = (int) fillOpacitySlider.getValue();
        this.fillColor = new Color(this.fillColor.getRed(), this.fillColor.getGreen(), this.fillColor.getBlue(), ((double) opacity / 100.0));
        if (this.selectedShape != null)
            this.selectedShape.setFillColor(this.fillColor);
    }
}
