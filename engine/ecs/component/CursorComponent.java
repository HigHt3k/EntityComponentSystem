package engine.ecs.component;

import engine.Game;
import engine.ecs.entity.Entity;

import java.awt.*;

public class CursorComponent extends Component {
    private Point cursorPosition;
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
        this.cursorPosition = Game.scale().scalePoint(cursorPosition);
    }
}
