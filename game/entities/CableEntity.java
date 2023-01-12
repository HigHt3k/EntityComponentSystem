package game.entities;

import com.Game;
import com.IdGenerator;
import com.ecs.entity.Entity;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.DebugIntent;
import com.ecs.intent.HoverIntent;
import com.graphics.scene.Scene;
import game.components.CablePortEntity;
import game.components.CablePortType;
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

        BuildIntent build = new BuildIntent();
        build.setIntentComponent(intents);
        intents.addIntent(build);

        if(Game.config().isDebug()) {
            DebugIntent debug = new DebugIntent();
            debug.setIntentComponent(intents);
            intents.addIntent(debug);
        }

    }

    public void initCablePorts(Scene s) {
        CablePortEntity portIn = new CablePortEntity(
                "CablePortIn_cable", IdGenerator.generateId(),
                0, 0, 0, 0, 0, CablePortType.IN
        );
        this.getComponent(CablePortsComponent.class).addCablePort(portIn);
        portIn.setConnectedEntity(connectedEntityPort1);
        s.addEntityToScene(portIn);

        CablePortEntity portOut = new CablePortEntity(
                "CablePortOut_cable", IdGenerator.generateId(),
                0, 0, 0, 0, 0, CablePortType.OUT
        );
        this.getComponent(CablePortsComponent.class).addCablePort(portOut);
        portOut.setConnectedEntity(connectedEntityPort2);
        s.addEntityToScene(portOut);
    }
}
