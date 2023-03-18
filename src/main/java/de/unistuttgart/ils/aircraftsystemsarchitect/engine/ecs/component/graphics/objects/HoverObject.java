package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects;

import java.awt.*;

public class HoverObject extends RenderObject {
    private final Color hoverColor;

    public HoverObject(Point location, Shape bounds, Color hoverColor) {
        super(location, bounds, Layer.HOVER);
        this.hoverColor = hoverColor;
        setHidden(true);
    }

    public HoverObject(Point location, Shape bounds, Color hoverColor, Layer layer) {
        super(location, bounds, layer);
        this.hoverColor = hoverColor;
        setHidden(true);
    }

    public Color getHoverColor() {
        return hoverColor;
    }
}
