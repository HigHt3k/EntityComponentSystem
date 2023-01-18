package game.entities;

import com.ecs.entity.Entity;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.HoverIntent;
import com.ecs.intent.Intent;
import game.components.*;
import game.handler.simulation.SimulationType;

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
                            int correctSignalsNeeded, int outOfControlSignalsAccepted,
                            int[] cablePortIdsIn, int[] cablePortIdsOut,
                            boolean removable) {
        super(name, id);
        this.setRemovable(removable);

        // define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // define GraphicsComponent
        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setBounds(bounds);
        graphics.setShape(bounds);
        graphics.setImage(img);
        graphics.setBorderColor(new Color(0, 0, 0, 0));
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
        sim.setCorrectSignalsNeeded(correctSignalsNeeded);
        sim.setOutOfControlSignalsAccepted(outOfControlSignalsAccepted);
        sim.setEntity(this);
        this.addComponent(sim);

        // define CollisionComponent
        CollisionComponent collider = new CollisionComponent();
        collider.setCollisionBox(bounds);
        collider.setEntity(this);
        this.addComponent(collider);

        // define CablePortsComponent
        CablePortsComponent cablePorts = new CablePortsComponent();
        cablePorts.setIds(cablePortIdsIn, cablePortIdsOut);
        for(int portId : cablePortIdsIn) {
            cablePorts.addCablePort(new CablePort(portId, CablePortType.IN, CablePortPosition.LEFT));
        }
        for(int portId : cablePortIdsOut) {
            cablePorts.addCablePort(new CablePort(portId, CablePortType.OUT, CablePortPosition.RIGHT));
        }
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

    }

    public void addIntent(Intent intent) {
        IntentComponent intents = this.getComponent(IntentComponent.class);
        intent.setIntentComponent(intents);
        intents.addIntent(intent);
    }
}
