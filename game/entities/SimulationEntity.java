package game.entities;

import com.IdGenerator;
import com.ecs.entity.Entity;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.HoverIntent;
import com.ecs.intent.Intent;
import com.graphics.scene.Scene;
import game.components.*;
import game.intent.BuildIntent;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * SimulationEntity:
 *         |_ GraphicsComponent
 *               |_ ToolTip
 *         |_ CollisionComponent
 *         |_ SimulationComponent
 *         |_ CablePortsComponent
 *         |_ GridComponent
 *         |_ IntentComponent
 *               |_ HoverIntent
 *               |_ BuildIntent
 *         |_ addIntent();
 */
public class SimulationEntity extends Entity {
    private static final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    public SimulationEntity(String name, int id,
                            int x, int y, int width, int height,
                            int xGrid, int yGrid,
                            BufferedImage img,
                            float failureRatio, SimulationType type,
                            int cablePortAmount, boolean removable) {
        super(name, id);
        this.setRemovable(removable);

        // define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // define GraphicsComponent
        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setBounds(bounds);
        graphics.setImage(img);
        graphics.setHoverColor(HOVER_COLOR);
        graphics.setEntity(this);
        this.addComponent(graphics);

        // define GridComponent
        GridComponent grid = new GridComponent();
        grid.setGridLocation(new Point(xGrid, yGrid));
        grid.setEntity(this);
        this.addComponent(grid);

        // define SimulationComponent
        SimulationComponent sim = new SimulationComponent();
        sim.setSimulationType(type);
        sim.setFailureRatio(failureRatio);
        sim.setEntity(this);
        this.addComponent(sim);

        // define CollisionComponent
        CollisionComponent collider = new CollisionComponent();
        collider.setCollisionBox(bounds);
        collider.setEntity(this);
        this.addComponent(collider);

        // define CablePortsComponent
        CablePortsComponent cablePorts = new CablePortsComponent();
        cablePorts.setEntity(this);
        this.addComponent(cablePorts);

        // define IntentComponent
        IntentComponent intents = new IntentComponent();
        intents.setEntity(this);
        this.addComponent(intents);

        // define HoverIntent by default
        HoverIntent hover = new HoverIntent();
        hover.setIntentComponent(intents);
        intents.addIntent(hover);

        // define BuildIntent by default
        BuildIntent build = new BuildIntent();
        build.setIntentComponent(intents);
        intents.addIntent(build);
    }

    public void addIntent(Intent intent) {
        IntentComponent intents = this.getComponent(IntentComponent.class);
        intent.setIntentComponent(intents);
        intents.addIntent(intent);
    }

    public void initCablePorts(Scene s) {
        for(int i = 0; i < 4; i++) {
            CablePortEntity port = new CablePortEntity(
                    "CablePortIn" + i + this.getName(), IdGenerator.generateId(),
                    this.getComponent(GraphicsComponent.class).get_BOUNDS().x, this.getComponent(GraphicsComponent.class).get_BOUNDS().y + (i+1) * this.getComponent(GraphicsComponent.class).get_BOUNDS().height/5 - this.getComponent(GraphicsComponent.class).get_BOUNDS().height/10,
                    this.getComponent(GraphicsComponent.class).get_BOUNDS().width/5, this.getComponent(GraphicsComponent.class).get_BOUNDS().height/5, i, CablePortType.IN
            );
            s.addEntityToScene(port);
            this.getComponent(CablePortsComponent.class).addCablePort(port);

            CablePortEntity portOut = new CablePortEntity(
                    "CablePortOut" + i + this.getName(), IdGenerator.generateId(),
                    this.getComponent(GraphicsComponent.class).get_BOUNDS().x +
                            this.getComponent(GraphicsComponent.class).get_BOUNDS().width -
                            this.getComponent(GraphicsComponent.class).get_BOUNDS().width/5,
                    this.getComponent(GraphicsComponent.class).get_BOUNDS().y + (i+1) *
                            this.getComponent(GraphicsComponent.class).get_BOUNDS().height/5 -
                            this.getComponent(GraphicsComponent.class).get_BOUNDS().height/10,
                    this.getComponent(GraphicsComponent.class).get_BOUNDS().width/5, this.getComponent(GraphicsComponent.class).get_BOUNDS().height/5, i, CablePortType.OUT
            );
            s.addEntityToScene(portOut);
            this.getComponent(CablePortsComponent.class).addCablePort(portOut);
        }
    }

    public void removeCablePorts(Scene s) {
        for(CablePortEntity cpe : this.getComponent(CablePortsComponent.class).getCablePorts()) {
            s.removeEntityFromScene(cpe);
        }
    }
}
