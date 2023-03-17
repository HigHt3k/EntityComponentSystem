package de.unistuttgart.ils.skyengine.ecs.component;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.ecs.entity.Entity;

import java.awt.*;

public class CursorComponent extends Component {
    private Point cursorPosition;
    private Point _CURSORPOSITION;
    private Entity selected;

    @Override
    public void update() {

    }

    public Entity getSelected() {
        return selected;
    }

    public void setSelected(Entity selected) {
        this.selected = selected;
    }

    public Point getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(Point cursorPosition) {
        this._CURSORPOSITION = cursorPosition;
        this.cursorPosition = Game.scale().scalePoint(cursorPosition);
    }

    public void reposition(Point p) {
        this.cursorPosition = p;
        this._CURSORPOSITION = Game.scale().upscalePoint(p);
    }
}
