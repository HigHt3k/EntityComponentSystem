package engine.ecs.component.graphics.objects;

import java.awt.*;

public class LineObject extends RenderObject {
    private final Point p1;
    private final Point p2;
    private final Color color;
    private final int thickness;

    public LineObject(Point location, Shape bounds, Layer layer, Point p1, Point p2, Color color, int thickness) {
        super(location, bounds, layer);
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
        this.thickness = thickness;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public Color getColor() {
        return color;
    }

    public int getThickness() {
        return thickness;
    }
}
