package game.components;

import com.Game;
import com.ecs.Component;

import java.awt.*;

public class GUIComponent extends Component {

    Color color = Color.ORANGE;
    Color textColor = Color.DARK_GRAY;
    String text = "";
    Font font;
    public GUIComponent(int x, int y, int width, int height) {
        font = Game.res().loadFont("test/res/font/joystix monospace.ttf", 14);
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void render(Graphics2D g) {

    }

    public String getText() {
        return text;
    }

    @Override
    public void update(float dTime) {

    }
}
