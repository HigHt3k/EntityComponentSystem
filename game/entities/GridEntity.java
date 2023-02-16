package game.entities;

import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.HoverObject;
import engine.ecs.component.graphics.objects.ImageObject;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.entity.Entity;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.intent.HoverIntent;
import game.components.GridComponent;

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
        renderComponent.addRenderObject(new HoverObject(new Point(x, y), bounds, HOVER_COLOR));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);

        // define GridComponent
        GridComponent grid = new GridComponent();
        grid.setGridLocation(new Point(xGrid, yGrid));
        grid.setEntity(this);
        this.addComponent(grid);

        // define CollisionComponent
        CollisionComponent collider = new CollisionComponent();
        collider.setCollisionBox(bounds);
        collider.setEntity(this);
        this.addComponent(collider);

        // define IntentComponent
        IntentComponent intents = new IntentComponent();
        intents.setEntity(this);
        this.addComponent(intents);

        // define HoverIntent by default
        HoverIntent hover = new HoverIntent();
        hover.setIntentComponent(intents);
        intents.addIntent(hover);
    }
}
