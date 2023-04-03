package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.*;

/**
 * Used to describe a virtual cursor position (e.g. for game pads)
 */
public class CursorComponent extends Component {
    private Point cursorPosition;
    private Point _CURSORPOSITION;
    private Entity selected;

    /**
     * Get the currently selected entity
     * @return currently selected entity
     */
    public Entity getSelected() {
        return selected;
    }

    /**
     * Set the currently selected entity
     * @param selected: entity to be selected
     */
    public void setSelected(Entity selected) {
        this.selected = selected;
    }

    /**
     * get the current cursor position on the screen (in px)
     * @return cursor position
     */
    public Point getCursorPosition() {
        return cursorPosition;
    }

    /**
     * set the cursor position (x,y)-coordinates. Scale to the input screen dimensions
     * @param cursorPosition: x,y position of cursor
     */
    public void setCursorPosition(Point cursorPosition) {
        this._CURSORPOSITION = cursorPosition;
        this.cursorPosition = Game.scale().scalePoint(cursorPosition);
    }

    /**
     * Reposition the cursor to a new (x,y) coordinate
     * @param p: new position
     */
    public void reposition(Point p) {
        this.cursorPosition = p;
        this._CURSORPOSITION = Game.scale().upscalePoint(p);
    }
}
