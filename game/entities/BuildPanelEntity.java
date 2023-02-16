package game.entities;

import engine.Game;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.HoverObject;
import engine.ecs.component.graphics.objects.ImageObject;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.entity.Entity;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.intent.HoverIntent;
import engine.graphics.elements.ToolTip;
import engine.resource.colorpalettes.Bit8;
import game.components.*;
import game.handler.simulation.SimulationType;

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
        renderComponent.addRenderObject(new ImageObject(new Point(x, y), bounds, Layer.GAMELAYER2, img));
        renderComponent.addRenderObject(new HoverObject(new Point(x, y), bounds, HOVER_COLOR));
        this.addComponent(renderComponent);
        renderComponent.setEntity(this);

        // define CollisionComponent
        CollisionComponent collider = new CollisionComponent();
        collider.setCollisionBox(bounds);
        collider.setEntity(this);
        this.addComponent(collider);

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
