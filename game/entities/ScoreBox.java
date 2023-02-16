package game.entities;

import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.ShapeObject;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;
import engine.resource.colorpalettes.Bit8;

import java.awt.*;

public class ScoreBox extends Entity {
    public ScoreBox(String name, int id, Font font, int score,
                    int x, int y, int width, int height, String text) {
        super(name, id);

        Rectangle r = new Rectangle(x, y, width, height);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.addRenderObject(new ShapeObject(new Point(x, y), r, Layer.GAMELAYER2, Bit8.PUMPKIN, Bit8.SCARLET, 1));
        renderComponent.addRenderObject(new TextObject(new Point(x, y), r, Layer.GAMELAYER3, text, font, Bit8.DARK_GREY));
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
    }
}
