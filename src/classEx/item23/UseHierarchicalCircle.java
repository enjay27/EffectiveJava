package classEx.item23;

class UseHierarchicalCircle extends BasedClass {
    final double radius;

    public UseHierarchicalCircle(double radius) {
        this.radius = radius;
    }

    @Override
    double area(){
        return Math.PI * (radius * radius);
    }
}

class UseHierarchicalRectangle extends BasedClass {

    private final double length;
    private final double width;

    public UseHierarchicalRectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area(){
        return length * width;
    }
}

class Square extends UseHierarchicalRectangle {
    public Square(double side) {
        super(side, side);
    }
}

abstract class BasedClass {
    abstract double area();
}
