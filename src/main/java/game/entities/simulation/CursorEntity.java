package game.entities.simulation;

import engine.ecs.component.CursorComponent;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.ImageObject;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.entity.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CursorEntity extends Entity {

    /**
     * create a new cursor entity that can be used by Controller/Keyboard to select and interact within the game
     * @param name: name of the entity
     * @param id: id of the entity
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
