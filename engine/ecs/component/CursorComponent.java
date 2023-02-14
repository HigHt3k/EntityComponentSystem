package engine.ecs.component;

import java.awt.*;

public class CursorComponent extends Component {
    private Point cursorPosition;

    @Override
    public void update() {

    }

    public Point getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(Point cursorPosition) {
        this.cursorPosition = cursorPosition;
    }
}
