package game.entities.ui;

import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.LineObject;
import engine.ecs.entity.Entity;
import engine.resource.colorpalettes.Bit8;

import java.awt.*;

public class ScaleEntity extends Entity {
    public ScaleEntity(String name, int id) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
        renderComponent.addRenderObject(
                new LineObject(
                        new Point(200, 200),
                        new Rectangle(200, 200, 500, 4),
                        Layer.UI_FRONT, new Point(200, 200),
                        new Point(700, 200),
                        Bit8.CHROME, 2
                )
        );
        renderComponent.addRenderObject(new LineObject(
                new Point(395, 195), new Rectangle(395, 195, 10, 10), Layer.UI_FRONT,
                new Point(395, 195), new Point(405, 205),
                Bit8.GREEN, 4
        ));
        renderComponent.addRenderObject(new LineObject(
                new Point(305, 195), new Rectangle(305, 195, 10, 10), Layer.UI_FRONT,
                new Point(305, 195), new Point(315, 205),
                Bit8.RED, 4
        ));
    }
}
