package game.entities;

import com.Game;
import com.ecs.entity.Entity;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.DebugIntent;
import com.ecs.intent.HoverIntent;
import game.components.CablePortsComponent;
import game.intent.BuildIntent;

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

    public CableEntity(String name, int id,
                       int x, int y, int width, int height,
                       Entity connectedEntityPort1, Entity connectedEntityPort2) {
        super(name, id);

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
        // correlation always between port 0 - 1, 2 - 3, 4 - 5, 6 - 7
        cablePorts.setCablePortAmount(2);
        cablePorts.generateCablePorts();
        cablePorts.getCablePort(0).setConnectedEntity(connectedEntityPort1);
        cablePorts.getCablePort(1).setConnectedEntity(connectedEntityPort2);
        cablePorts.setEntity(this);
        this.addComponent(cablePorts);

        // IntentHandler
        IntentComponent intents = new IntentComponent();
        intents.setEntity(this);
        this.addComponent(intents);

        HoverIntent hover = new HoverIntent();
        hover.setIntentComponent(intents);
        intents.addIntent(hover);

        BuildIntent build = new BuildIntent();
        build.setIntentComponent(intents);
        intents.addIntent(build);

        if(Game.config().isDebug()) {
            DebugIntent debug = new DebugIntent();
            debug.setIntentComponent(intents);
            intents.addIntent(debug);
        }

    }
}
