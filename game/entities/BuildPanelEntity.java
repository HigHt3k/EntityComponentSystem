package game.entities;

import engine.Game;
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

        // define GraphicsComponent
        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setBounds(bounds);
        graphics.setImage(img);
        graphics.setHoverColor(HOVER_COLOR);
        graphics.setTextColor(TEXT_COLOR);
        graphics.setEntity(this);
        this.addComponent(graphics);

        // ToolTip
        ToolTip tt = new ToolTip();
        tt.setFont(graphics.getFont());
        tt.setText(description);
        graphics.setToolTip(tt);
        graphics.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        if(amount > 100) {
            graphics.addText("");
        } else {
            graphics.addText(String.valueOf(amount));
        }
        graphics.addLocation(new Point(x, y + height));

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
