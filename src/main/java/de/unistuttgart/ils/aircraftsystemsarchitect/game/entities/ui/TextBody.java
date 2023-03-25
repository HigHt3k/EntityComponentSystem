package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.*;

public class TextBody extends Entity {
    public TextBody(String name, int id, int x, int y, int width, int height,
                    Font font, Color textColor, String text) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new TextObject(new Point(x, y), new Rectangle(x, y, width, height),
                Layer.UI, text, font, textColor));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
