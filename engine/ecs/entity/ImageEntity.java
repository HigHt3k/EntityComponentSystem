package engine.ecs.entity;

import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.ImageObject;
import engine.ecs.component.graphics.objects.Layer;

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
}
