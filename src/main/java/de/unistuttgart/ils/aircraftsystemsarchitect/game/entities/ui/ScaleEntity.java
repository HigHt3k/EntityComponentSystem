package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.LineObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;

import java.awt.*;

public class ScaleEntity extends Entity {
    public ScaleEntity(String name, int id,
                       int x, int y, int width, int height,
                       Marker marker1, Marker marker2, Marker marker3) {
        super(name, id);

        RenderComponent renderComponent = new RenderComponent();
        renderComponent.setEntity(this);
        this.addComponent(renderComponent);
        renderComponent.addRenderObject(
                new LineObject(
                        new Point(x, y),
                        new Rectangle(x, y, width, height),
                        Layer.UI_FRONT, new Point(x, y),
                        new Point(x + width, y),
                        Bit8.WHITE, 4
                )
        );
        renderComponent.addRenderObject(marker1.lineObject1);
        renderComponent.addRenderObject(marker2.lineObject1);
        renderComponent.addRenderObject(marker3.lineObject1);
    }
}
