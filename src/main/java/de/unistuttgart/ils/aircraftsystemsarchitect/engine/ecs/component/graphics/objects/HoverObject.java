package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects;

import java.awt.*;

/**
 * HoverObjects can be used to display a transparent / non-transparent shape at a boundary at the hovering layer
 */
public class HoverObject extends RenderObject {
    private final Color hoverColor;

    /**
     * Create new HoverObject at given location and size, with color
     * @param location: the location to place the object
     * @param bounds: the boundaries of the object
     * @param hoverColor: the color (alpha needs to be set before)
     */
    public HoverObject(Point location, Shape bounds, Color hoverColor) {
        super(location, bounds, Layer.HOVER);
        this.hoverColor = hoverColor;
        setHidden(true);
    }

    /**
     * Create new HoverObject at given location and size, with color and at custom rendering layer
     * @param location: the location to place the object
     * @param bounds: the boundaries of the object
     * @param hoverColor: the color (alpha needs to be set before)
     * @param layer: rendering layer
     */
    public HoverObject(Point location, Shape bounds, Color hoverColor, Layer layer) {
        super(location, bounds, layer);
        this.hoverColor = hoverColor;
        setHidden(true);
    }

    /**
     * Get the color of the hover object
     * @return hover color
     */
    public Color getHoverColor() {
        return hoverColor;
    }
}
