package classEx.item23;

public class DoNotUseTaggedClass {
    enum Shape { RECTANGLE, CIRCLE };

    final Shape shape;

    double length;
    double width;

    double radius;

    public DoNotUseTaggedClass(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    public DoNotUseTaggedClass(double length, double width) {
        shape = Shape.RECTANGLE;
        this.length = length;
        this.width = width;
    }

    double area() {
        switch (shape) {
            case RECTANGLE:
                return length * width;
            case CIRCLE:
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
}
