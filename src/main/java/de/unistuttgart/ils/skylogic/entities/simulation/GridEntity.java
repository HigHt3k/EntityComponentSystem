package de.unistuttgart.ils.skylogic.entities.simulation;

import de.unistuttgart.ils.skyengine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.skyengine.ecs.component.collision.CollisionObject;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.HoverObject;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.skyengine.ecs.entity.Entity;
import de.unistuttgart.ils.skylogic.components.GridComponent;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * SimulationEntity:
 *         |_ GraphicsComponent
 *         |_ CollisionComponent
 *         |_ GridComponent
 *         |_ IntentComponent
 *               |_ HoverIntent
 *         |_ addIntent();
 */
public class GridEntity extends Entity {
    private final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    public GridEntity(String name, int id,
                      int x, int y, int width, int height,
                      int xGrid, int yGrid,
                      BufferedImage img) {
        super(name, id);
        // define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // define GraphicsComponent
        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), bounds, Layer.GAMELAYER1, img));
        HoverObject hover = new HoverObject(new Point(x, y), bounds, HOVER_COLOR);
        renderComponent.addRenderObject(hover);
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);

        // define GridComponent
        GridComponent grid = new GridComponent();
        grid.setGridLocation(new Point(xGrid, yGrid));
        grid.setEntity(this);
        this.addComponent(grid);

        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.addCollisionObject(new CollisionObject(bounds, hover));
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);
    }
}
