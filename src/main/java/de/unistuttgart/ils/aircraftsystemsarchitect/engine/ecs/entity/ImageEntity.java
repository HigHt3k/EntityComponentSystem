package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.AnimationObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageEntity extends Entity {

    public ImageEntity(String name, int id, BufferedImage img, int x, int y, int width, int height, Layer layer) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), new Rectangle(x, y, width, height), layer, img));
        this.addComponent(renderComponent);
        addComponent(renderComponent);
    }

    public ImageEntity(String name, int id, Image img, int x, int y, int width, int height, Layer layer, boolean randomize, long pauseFrames) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new AnimationObject(new Point(x, y), new Rectangle(x, y, width, height), layer, img, randomize, pauseFrames));
        this.addComponent(renderComponent);
        addComponent(renderComponent);
    }
}