package game.entities;

import engine.Game;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.entity.Entity;
import engine.ecs.intent.DebugIntent;
import engine.ecs.intent.HoverIntent;
import game.components.CablePortsComponent;
import game.components.GridComponent;

import java.awt.*;

/**
 * CabelEntity
 *      |_ GraphicsComponent
 *      |_ GridComponent
 *      |_ CabelPortComponent
 *          |_ Port1: In
 *          |_ Port2: Out
 *      |_ CableCollidersComponent
*       |_ CollisionComponent
 *      |_ IntentComponent
 *          |_ BuildIntent
 *          |_ HoverIntent
 */
public class CableCombinerEntity extends Entity {
    private final Color LINE_COLOR = new Color(150, 0, 0, 255);

    public CableCombinerEntity(String name, int id,
                               int x, int y, int width, int height,
                               int xGrid, int yGrid) {
        super(name, id);

        // set bounds
        Rectangle bounds = new Rectangle(x, y, width, height);

        // Graphics
        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setBounds(bounds);
        graphics.setShape(bounds);
        graphics.setFillColor(new Color(120, 120, 120, 130));
        graphics.setBorderColor(Color.BLUE);
        graphics.setEntity(this);
        this.addComponent(graphics);

        // Grid
        GridComponent grid = new GridComponent();
        grid.setGridLocation(new Point(xGrid, yGrid));
        grid.setEntity(this);
        this.addComponent(grid);

        // CablePorts
        CablePortsComponent cablePorts = new CablePortsComponent();
        cablePorts.setEntity(this);
        this.addComponent(cablePorts);
        // Generate the ports


        //Collider
        CollisionComponent collider = new CollisionComponent();
        collider.setCollisionBox(bounds);
        collider.setEntity(this);
        this.addComponent(collider);

        // IntentHandler
        IntentComponent intents = new IntentComponent();
        intents.setEntity(this);
        this.addComponent(intents);

        HoverIntent hover = new HoverIntent();
        hover.setIntentComponent(intents);
        intents.addIntent(hover);


        if(Game.config().isDebug()) {
            DebugIntent debug = new DebugIntent();
            debug.setIntentComponent(intents);
            intents.addIntent(debug);
        }

    }
}
