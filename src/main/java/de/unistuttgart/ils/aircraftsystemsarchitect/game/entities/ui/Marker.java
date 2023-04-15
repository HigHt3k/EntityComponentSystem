package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.LineObject;

import java.awt.*;

/**
 * A marker object that represents a vertical line at a specified position.
 */
public class Marker {

    protected final LineObject lineObject;

    /**
     * Constructs a new Marker object with the specified position and color.
     *
     * @param position the position of the marker
     * @param color    the color of the marker
     */
    public Marker(Point position, Color color) {
        this.lineObject = new LineObject(
                position, // starting point
                new Rectangle(position.x, position.y, 5, 50), // bounding box
                Layer.UI_FRONT, // layer
                new Point(position.x, position.y - 20), // start of the line
                new Point(position.x, position.y + 20), // end of the line
                color, // color
                10 // thickness
        );
    }
}
