package engine.ecs.component.graphics.objects;

import java.awt.*;

public class ShapeObject extends RenderObject {
    public Color fillColor;
    public Color borderColor;
    private final int thickness;

    public ShapeObject(Point location, Shape bounds, Layer layer, Color fillColor, Color borderColor, int thickness) {
        super(location, bounds, layer);
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.thickness = thickness;
    }

    public int getThickness() {
        return thickness;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}
