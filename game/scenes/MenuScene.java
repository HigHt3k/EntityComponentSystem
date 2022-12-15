package game.scenes;

import com.Game;
import com.ecs.Entity;
import com.ecs.GraphicsComponent;
import com.graphics.scene.Scene;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MenuScene extends Scene {
    public MenuScene(String name, int id) {
        super(name, id);
        // Create the Menu GUI
        Entity gui = new Entity("GUI", 0);
        GraphicsComponent background = new GraphicsComponent();
        background.setBounds(new Rectangle(
                0,
                0,
                Game.config().renderConfiguration().getResolution().width,
                Game.config().renderConfiguration().getResolution().height)
        );

        try {
            background.setImage(ImageIO.read(new File("game/res/system.jpg")));
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        gui.addComponent(background);
        background.setEntity(gui);
        addEntityToScene(gui);

    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }
}
