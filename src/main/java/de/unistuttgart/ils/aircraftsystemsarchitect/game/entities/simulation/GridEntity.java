package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.CollisionObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.HoverObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.GridComponent;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The entity representing the grid in the game.
 */
public class GridEntity extends Entity {
    private final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    /**
     * Constructor for the GridEntity class.
     *
     * @param name   the name of the entity
     * @param id     the ID of the entity
     * @param x      the x-coordinate of the entity
     * @param y      the y-coordinate of the entity
     * @param width  the width of the entity
     * @param height the height of the entity
     * @param xGrid  the x-coordinate of the grid
     * @param yGrid  the y-coordinate of the grid
     * @param img    the image used to render the grid
     */
    public GridEntity(String name, int id, int x, int y, int width, int height, int xGrid, int yGrid, BufferedImage img) {
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
