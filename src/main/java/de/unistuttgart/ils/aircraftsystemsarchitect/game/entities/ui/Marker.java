package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.LineObject;

import java.awt.*;

public class Marker {
    LineObject lineObject1;

    public Marker(Point position, Color color) {
        lineObject1 = new LineObject(position, new Rectangle(position.x, position.y, 5, 50), Layer.UI_FRONT,
                new Point(position.x, position.y - 20),
                new Point(position.x, position.y + 20),
                color, 10
        );
    }
}
