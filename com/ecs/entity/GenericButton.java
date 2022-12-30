package com.ecs.entity;

import com.ecs.Entity;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.HoverIntent;
import com.ecs.intent.Intent;

import java.awt.*;

/**
 * GenericButton:
 *         |_ GraphicsComponent
 *         |_ CollisionComponent
 *         |_ IntentComponent
 *               |_ HoverIntent
 *         |_ addIntent();
 */
public class GenericButton extends Entity {
    private static final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private static final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private static final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private static final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    /**
     * Create a generic functional button. addIntent needs to be used to add a purpose other than hovering to this button.
     * @param name: Name of the entity
     * @param id: Id of the entity
     * @param x: x position by design
     * @param y: y position by design
     * @param width: design width
     * @param height: design height
     * @param text: text to display (Centered) at the button
     * @param font: Font to use for this button
     */
    public GenericButton(String name, int id, int x, int y, int width, int height, String text, Font font) {
        super(name, id);

        // Define the size
        Rectangle bounds = new Rectangle(x, y, width, height);

        // Graphics Component properties
        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setEntity(this);
        graphics.setBounds(bounds);
        graphics.setFont(font);
        graphics.setTextColor(TEXT_COLOR);
        graphics.setBorderColor(BOX_BORDER_COLOR);
        graphics.setFillColor(BOX_COLOR);
        graphics.setHoverColor(HOVER_COLOR);
        // --- always centered by default
        graphics.addText(text);
        graphics.addLocation(new Point(x, y));
        this.addComponent(graphics);

        // Collider
        CollisionComponent collider = new CollisionComponent();
        collider.setEntity(this);
        collider.setCollisionBox(bounds);
        this.addComponent(collider);

        // Intent Handler
        IntentComponent intents = new IntentComponent();
        intents.setEntity(this);
        // Add Hovering
        HoverIntent hover = new HoverIntent();
        hover.setIntentComponent(intents);
        intents.addIntent(hover);
        this.addComponent(intents);
    }

    public void addIntent(Intent intent) {
        IntentComponent intents = this.getComponent(IntentComponent.class);
        intent.setIntentComponent(intents);
        intents.addIntent(intent);
    }
}
