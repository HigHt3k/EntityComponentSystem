package engine.ecs.component.graphics.objects;

import java.awt.*;

public class HoverObject extends RenderObject {
    private Color hoverColor;

    public HoverObject(Point location, Shape bounds, Color hoverColor) {
        super(location, bounds, Layer.HOVER);
        this.hoverColor = hoverColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }
}
