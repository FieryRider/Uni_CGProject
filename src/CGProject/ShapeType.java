package CGProject;

public enum ShapeType {
    LINE(1), RECTANGLE(2), ELLIPSE(3), CIRCLE(4), TRIANGLE(5);

    private final int value;

    private ShapeType(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static ShapeType fromValue(int value) {
        for (ShapeType shapeType : values()) {
            if (shapeType.value == value) {
                return shapeType;
            }
        }
        return null;
    }
}
