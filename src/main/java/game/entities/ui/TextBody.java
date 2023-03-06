package game.entities.ui;

import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;

import java.awt.*;

public class TextBody extends Entity {
    public TextBody(String name, int id, int x, int y, int width, int height,
                    Font font, Color textColor, String text) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new TextObject(new Point(x, y), new Rectangle(x, y, width, height),
                Layer.UI_FRONT, text, font, textColor));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
