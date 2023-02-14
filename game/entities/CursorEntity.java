package game.entities;

import engine.ecs.component.CursorComponent;
import engine.ecs.component.graphics.GraphicsComponent;
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

        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setEntity(this);
        graphics.setBounds(new Rectangle(0, 0, 0, 0));
        try {
            graphics.setImage(ImageIO.read(new File("game/res/cursor.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.addComponent(graphics);

        CursorComponent cursor = new CursorComponent();
        cursor.setCursorPosition(new Point(-1, -1));
        cursor.setEntity(this);
        this.addComponent(cursor);
    }
}