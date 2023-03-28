package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Describes entities grid position
 */
public class GridComponent extends Component {
    private Point2D gridLocation;

    public void setGridLocation(Point gridLocation) {
        this.gridLocation = gridLocation;
    }

    public Point2D getGridLocation() {
        return gridLocation;
    }
}
