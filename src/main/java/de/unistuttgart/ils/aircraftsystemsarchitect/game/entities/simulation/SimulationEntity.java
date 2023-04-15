package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.CollisionObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.HoverObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ShapeObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.*;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable.CablePort;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable.CablePortPosition;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationState;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a simulation entity, which can be added to a game scene to simulate certain systems or sensors.
 */
public class SimulationEntity extends Entity {
    private final Color HOVER_COLOR = Bit8.setAlpha(Bit8.HEATHERED_GREY, 80);

    /**
     * Creates a new simulation entity.
     *
     * @param name                    The name of the simulation entity.
     * @param id                      The ID of the simulation entity.
     * @param x                       The x position of the simulation entity.
     * @param y                       The y position of the simulation entity.
     * @param width                   The width of the simulation entity.
     * @param height                  The height of the simulation entity.
     * @param xGrid                   The x position of the simulation entity on the grid.
     * @param yGrid                   The y position of the simulation entity on the grid.
     * @param img                     The image representing the simulation entity.
     * @param tileId                  The ID of the tile representing the simulation entity.
     * @param failureRatio            The failure ratio of the simulation entity.
     * @param type                    The simulation type.
     * @param correctSignalsNeeded    The number of correct signals needed for the simulation to recognize a signal.
     * @param outOfControlSignalsAccepted The number of out-of-control signals accepted by the simulation.
     * @param cablePortIdsIn          An array of IDs of cable ports the simulation entity has as inputs.
     * @param cablePortIdsOut         An array of IDs of cable ports the simulation entity has as outputs.
     * @param removable               Whether or not the simulation entity is removable.
     * @param failureDetectionRatio   The ratio at which the simulation entity can detect failures.
     */
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
        renderComponent.addRenderObject(new ShapeObject(new Point(x + 10, y + 10), new Rectangle(x + 10, y + 10, width-20, height-20), Layer.GAMELAYER2, null, null, 1));
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), bounds, Layer.GAMELAYER3, img));
        HoverObject hover = new HoverObject(new Point(x, y), bounds, HOVER_COLOR);
        renderComponent.addRenderObject(hover);
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
        toolTip.setType(String.valueOf(type));
        this.addComponent(toolTip);

    }
}
