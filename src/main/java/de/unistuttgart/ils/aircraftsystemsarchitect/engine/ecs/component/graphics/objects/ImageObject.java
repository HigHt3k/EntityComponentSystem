package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageObject extends RenderObject {
    private BufferedImage image;

    public ImageObject(Point location, Shape bounds, Layer layer, BufferedImage image) {
        super(location, bounds, layer);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
