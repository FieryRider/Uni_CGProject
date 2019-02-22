package shapes;

import CGProject.Group;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class BaseShape implements Serializable {
    protected boolean complex = false;
    protected boolean partOfGroup = false;

    protected transient Color strokeColor;
    protected transient Color fillColor;
    protected int lineWidth;

    protected Group group;

    public boolean isComplex() {
        return complex;
    }

    public BaseShape() { }

    public boolean isPartOfGroup() {
        return partOfGroup;
    }

    public void setPartOfGroup(boolean partOfGroup) {
        this.partOfGroup = partOfGroup;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    protected void addTransforms(Shape shape) {
        //SimleShapeTransforms
        shape.getTransforms().add(new Translate(0, 0));
        shape.getTransforms().add(new Rotate(0, 0, 0));
        shape.getTransforms().add(new Scale(1, 1, 0, 0));
        //ComplexShapeTransforms
        shape.getTransforms().add(new Translate(0, 0));
        shape.getTransforms().add(new Rotate(0, 0, 0));
        shape.getTransforms().add(new Scale(1, 1, 0, 0));
        //GroupTransforms
        shape.getTransforms().add(new Translate(0, 0));
        shape.getTransforms().add(new Rotate(0, 0, 0));
        shape.getTransforms().add(new Scale(1, 1, 0, 0));
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (s.readBoolean())
            this.strokeColor = new Color(s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble());
        if (s.readBoolean())
            this.fillColor = new Color(s.readDouble(), s.readDouble(), s.readDouble(), s.readDouble());
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        if (strokeColor != null) {
            s.writeBoolean(true);
            s.writeDouble(this.strokeColor.getRed());
            s.writeDouble(this.strokeColor.getGreen());
            s.writeDouble(this.strokeColor.getBlue());
            s.writeDouble(this.strokeColor.getOpacity());
        } else {
            s.writeBoolean(false);
        }
        if (fillColor != null) {
            s.writeBoolean(true);
            s.writeDouble(this.fillColor.getRed());
            s.writeDouble(this.fillColor.getGreen());
            s.writeDouble(this.fillColor.getBlue());
            s.writeDouble(this.fillColor.getOpacity());
        } else {
            s.writeBoolean(false);
        }
    }
}
