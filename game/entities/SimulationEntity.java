package game.entities;

import engine.Game;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.HoverObject;
import engine.ecs.component.graphics.objects.ImageObject;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.ShapeObject;
import engine.ecs.entity.Entity;
import engine.resource.colorpalettes.Bit8;
import game.components.*;
import game.handler.simulation.SimulationState;
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
    private final Color HOVER_COLOR = Bit8.setAlpha(Bit8.HEATHERED_GREY, 80);

    public SimulationEntity(String name, int id,
                            int x, int y, int width, int height,
                            int xGrid, int yGrid,
                            BufferedImage img, int tileId,
                            float failureRatio, SimulationType type,
                            int correctSignalsNeeded, int outOfControlSignalsAccepted,
                            int[] cablePortIdsIn, int[] cablePortIdsOut,
                            boolean removable, float failureDetectionRatio) {
        super(name, id);
        this.setRemovable(removable);

        // define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), bounds, Layer.GAMELAYER2, img));
        HoverObject hover = new HoverObject(new Point(x, y), bounds, HOVER_COLOR);
        renderComponent.addRenderObject(hover);
        renderComponent.addRenderObject(new ShapeObject(new Point(x, y), bounds, Layer.GAMELAYER3, Bit8.GREEN, null, 1));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);

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
        sim.setFailureRecognitionRatio(failureDetectionRatio);
        sim.setTileId(tileId);
        if (type == SimulationType.SENSOR) {
            sim.setSimulationState(SimulationState.CORRECT);
        }
        sim.setEntity(this);
        this.addComponent(sim);

        // define CollisionComponent
        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.addCollisionObject(new CollisionObject(bounds, hover));
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);

        // define CablePortsComponent
        CablePortsComponent cablePorts = new CablePortsComponent();
        cablePorts.setIds(cablePortIdsIn, cablePortIdsOut);
        for (int portId : cablePortIdsIn) {
            cablePorts.addCablePort(new CablePort(portId, CablePortType.IN, CablePortPosition.LEFT));
        }
        for (int portId : cablePortIdsOut) {
            cablePorts.addCablePort(new CablePort(portId, CablePortType.OUT, CablePortPosition.RIGHT));
        }
        cablePorts.setEntity(this);
        this.addComponent(cablePorts);

        // define HoverIntent by default

        TooltipComponent toolTip = new TooltipComponent();
        toolTip.setTooltipText(Game.res().loadDescription(tileId));
        toolTip.setFailureRatio(String.valueOf(failureRatio));
        toolTip.setAcceptedOOCSignals(String.valueOf(outOfControlSignalsAccepted));
        toolTip.setCorrectInputSignals(String.valueOf(correctSignalsNeeded));
        toolTip.setEntity(this);
        this.addComponent(toolTip);

    }
}
