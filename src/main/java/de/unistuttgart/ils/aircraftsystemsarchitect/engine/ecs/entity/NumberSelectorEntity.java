package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity;

import java.awt.*;

// TODO: Refactor
public class NumberSelectorEntity extends Entity {


    private Entity change;

    /**
     * Create a new number selector
     * @param name
     * @param id
     */
    public NumberSelectorEntity(String name, int id,
                                int x, int y, int width, int height,
                                Font font, Color textColor,
                                Color fillColor, Color borderColor) {
        super(name, id);

        Rectangle leftShape = new Rectangle(x, y, width / 3, height);
        Rectangle middleShape = new Rectangle(x + width/3, y, width/3, height);
        Rectangle rightShape = new Rectangle(x + width/3*2, y, width/3, height);

        //TODO: add graphics and colliders

    }

    public Entity getChange() {
        return change;
    }

    public void setChange(Entity change) {
        this.change = change;
    }
}
