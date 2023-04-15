package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Describes entities grid position
 */
public class GridComponent extends Component {
    private Point2D gridLocation;

    /**
     * Sets the grid location of the entity
     *
     * @param gridLocation The new grid location of the entity
     */
    public void setGridLocation(Point gridLocation) {
        this.gridLocation = gridLocation;
    }

    /**
     * Gets the grid location of the entity
     *
     * @return The grid location of the entity
     */
    public Point2D getGridLocation() {
        return gridLocation;
    }
}
