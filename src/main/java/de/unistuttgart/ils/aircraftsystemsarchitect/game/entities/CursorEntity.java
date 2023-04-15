package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.CursorComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * An entity that represents the cursor that can be used to select and interact with the game, when using keyboard or gamepad.
 */
public class CursorEntity extends Entity {

    /**
     * Creates a new CursorEntity with the given name and ID.
     *
     * @param name the name of the entity
     * @param id the ID of the entity
     */
    public CursorEntity(String name, int id) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        try {
            renderComponent.addRenderObject(new ImageObject(new Point(1920 / 2, 1080 / 2), new Rectangle(1920 / 2, 1080 / 2, 50, 50),
                    Layer.CURSOR, ImageIO.read(new File("res/cursor.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);

        CursorComponent cursor = new CursorComponent();
        cursor.setCursorPosition(new Point(1920 / 2, 1080 / 2));
        cursor.setEntity(this);
        this.addComponent(cursor);
    }
}
