package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects;


import java.awt.*;

public class RenderObject {
    private Point location;
    private Shape bounds;
    private final Layer layer;
    private boolean hidden = false;

    public RenderObject(Point location, Shape bounds, Layer layer) {
        this.location = location;
        this.bounds = bounds;
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

    public Point getLocation() {
        return location;
    }

    public Shape getBounds() {
        return bounds;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setBounds(Shape bounds) {
        this.bounds = bounds;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
