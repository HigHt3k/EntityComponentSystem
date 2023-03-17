package de.unistuttgart.ils.skylogic.entities.simulation;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.skyengine.ecs.component.collision.CollisionObject;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.HoverObject;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.skyengine.ecs.entity.Entity;
import de.unistuttgart.ils.skyengine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.skyengine.resource.fonts.FontCollection;
import de.unistuttgart.ils.skylogic.components.BuildComponent;
import de.unistuttgart.ils.skylogic.components.TooltipComponent;
import de.unistuttgart.ils.skylogic.handler.simulation.SimulationType;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * SimulationEntity:
 *         |_ GraphicsComponent
 *                  |_ ToolTip
 *         |_ CollisionComponent
 *         |_ BuildComponent
 *         |_ IntentComponent
 *               |_ HoverIntent
 *               |_ BuildIntent
 *         |_ addIntent();
 */
public class BuildPanelEntity extends Entity {
    private final Color HOVER_COLOR = Bit8.setAlpha(Bit8.HEATHERED_GREY, 80);
    private final Color TEXT_COLOR = Bit8.DARK_GREY;

    public BuildPanelEntity(String name, int id,
                            int x, int y, int width, int height,
                            BufferedImage img, int tileId,
                            int amount, float failureRatio,
                            SimulationType simulationType,
                            int correctSignalsNeeded, int outOfControlSignalsAccepted,
                            String description, float failureDetectionRatio) {
        super(name, id);

        // define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), bounds, Layer.UI_FRONT, img));
        HoverObject hover = new HoverObject(new Point(x, y), bounds, HOVER_COLOR, Layer.UI_HOVER);
        renderComponent.addRenderObject(hover);
        renderComponent.addRenderObject(new TextObject(new Point(x + bounds.width / 2 - 20, (int) (y + bounds.height * 1.035f)), bounds, Layer.UI_FRONT, "", FontCollection.bit8Font, TEXT_COLOR));
        this.addComponent(renderComponent);
        renderComponent.setEntity(this);

        ColliderComponent colliderComponent = new ColliderComponent();
        colliderComponent.addCollisionObject(new CollisionObject(bounds, hover));
        colliderComponent.setEntity(this);
        this.addComponent(colliderComponent);

        // define BuildComponent
        BuildComponent builder = new BuildComponent();
        builder.setAmount(amount);
        builder.setFailureRatio(failureRatio);
        builder.setEntity(this);
        builder.setSimulationType(simulationType);
        builder.setCorrectSignalsNeeded(correctSignalsNeeded);
        builder.setOutOfControlSignalsAccepted(outOfControlSignalsAccepted);
        builder.setTileId(tileId);
        builder.setFailureDetectionRatio(failureDetectionRatio);
        this.addComponent(builder);

        TooltipComponent toolTip = new TooltipComponent();
        toolTip.setTooltipText(Game.res().loadDescription(tileId));
        toolTip.setFailureRatio(String.valueOf(failureRatio));
        toolTip.setAcceptedOOCSignals(String.valueOf(outOfControlSignalsAccepted));
        toolTip.setCorrectInputSignals(String.valueOf(correctSignalsNeeded));
        toolTip.setEntity(this);
        toolTip.setType(String.valueOf(simulationType));
        this.addComponent(toolTip);
    }
}
