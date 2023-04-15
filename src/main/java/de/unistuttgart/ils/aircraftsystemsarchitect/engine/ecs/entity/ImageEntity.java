package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.AnimationObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The ImageEntity is an archetype which implements simply a RenderComponent to show an image on the screen
 */
public class ImageEntity extends Entity {

    /**
     * Create a new image for the renderer on a custom layer
     * @param name: entity name
     * @param id: entity id
     * @param img: image to render
     * @param x: x screen coordinate in px
     * @param y: y screen coordinate in px
     * @param width: width of image in px
     * @param height: height of image in px
     * @param layer: layer to render the image to
     */
    public ImageEntity(String name, int id, BufferedImage img, int x, int y, int width, int height, Layer layer) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), new Rectangle(x, y, width, height), layer, img));
        this.addComponent(renderComponent);
        addComponent(renderComponent);
    }

    /**
     * Create a new animation (gif) for the renderer
     * @param name: entity name
     * @param id: entity id
     * @param img: gif animation
     * @param x: x coordinate on screen in px
     * @param y: y coordinate on screen in px
     * @param width: width of image in px
     * @param height: height of image in px
     * @param layer: layer to render the image to
     * @param randomize: randomize the animation pauses
     * @param pauseFrames: amount of frames between two animation rendering cycles
     */
    public ImageEntity(String name, int id, Image img, int x, int y, int width, int height, Layer layer, boolean randomize, long pauseFrames) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new AnimationObject(new Point(x, y), new Rectangle(x, y, width, height), layer, img, randomize, pauseFrames));
        this.addComponent(renderComponent);
        addComponent(renderComponent);
    }
}
