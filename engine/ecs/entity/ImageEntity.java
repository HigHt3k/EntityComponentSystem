package engine.ecs.entity;

import engine.ecs.component.graphics.GraphicsComponent;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageEntity extends Entity {

    public ImageEntity(String name, int id, BufferedImage img, int x, int y, int width, int height, int layer) {
        super(name, id);

        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setBounds(new Rectangle(x, y, width, height));
        graphics.setImage(img);
        graphics.setEntity(this);
        graphics.setLayer(layer);
        this.addComponent(graphics);
    }
}
