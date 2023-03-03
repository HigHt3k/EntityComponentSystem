package game.components;

import engine.ecs.component.Component;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Describes entities grid position
 */
public class GridComponent extends Component {
    private Point2D gridLocation;

    @Override
    public void update() {

    }

    public void setGridLocation(Point gridLocation) {
        this.gridLocation = gridLocation;
    }

    public Point2D getGridLocation() {
        return gridLocation;
    }
}
