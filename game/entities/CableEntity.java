package game.entities;

import engine.Game;
import engine.ecs.entity.Entity;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.intent.DebugIntent;
import engine.ecs.intent.HoverIntent;
import game.components.CablePortsComponent;

import java.awt.*;

/**
 * CabelEntity
 *      |_ GraphicsComponent
 *      |_ GridComponent
 *      |_ CabelPortComponent
 *          |_ Port1: In
 *          |_ Port2: Out
 *       |_ CollisionComponent
 *      |_ IntentComponent
 *          |_ BuildIntent
 *          |_ HoverIntent
 */
public class CableEntity extends Entity {
    private final Color LINE_COLOR = new Color(150, 0, 0, 255);
    private Entity connectedEntityPort1;
    private Entity connectedEntityPort2;

    public CableEntity(String name, int id,
                       int x, int y, int width, int height,
                       Entity connectedEntityPort1, Entity connectedEntityPort2) {
        super(name, id);
        this.connectedEntityPort1 = connectedEntityPort1;
        this.connectedEntityPort2 = connectedEntityPort2;

        // set bounds
        Rectangle bounds = new Rectangle(x, y, width, height);

        // Graphics
        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setLineColor(LINE_COLOR);
        graphics.setThickness(5);
        // Cable rendering needs to be redesigned here
        graphics.setLine(new Point(x, y), new Point(x + 50, y + 50));
        graphics.setBounds(bounds);
        graphics.setEntity(this);
        this.addComponent(graphics);

        // CablePorts
        CablePortsComponent cablePorts = new CablePortsComponent();
        cablePorts.setEntity(this);
        this.addComponent(cablePorts);

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
