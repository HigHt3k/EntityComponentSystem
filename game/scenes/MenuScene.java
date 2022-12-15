package game.scenes;

import com.Game;
import com.ecs.CollisionComponent;
import com.ecs.Entity;
import com.ecs.GraphicsComponent;
import com.graphics.scene.Scene;
import com.input.InputManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MenuScene extends Scene {
    private final int ITEM_MARGIN = 20;
    private final int ITEM_WIDTH = 300;
    private final int ITEM_HEIGHT = 60;
    private final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    public MenuScene(String name, int id) {
        super(name, id);
        Game.setInputManager(new InputManager() {
            @Override
            public void handle() {
                while(!getMouseEvents().isEmpty()) {
                    MouseEvent e = getMouseEvents().get(0);
                    for(Entity entity : Game.scene().current().getEntities()) {
                        GraphicsComponent gc = entity.getComponent(GraphicsComponent.class);
                        if(gc != null) {
                            if(gc.contains(e.getPoint())) {
                                gc.hovered();
                            } else {
                                gc.unhovered();
                            }
                        }
                    }

                    getMouseEvents().remove(e);
                }
            }
        });
        // Create the Menu GUI
        Entity background = new Entity("Background", 0);
        GraphicsComponent backgroundGraphicsComponent = new GraphicsComponent();
        backgroundGraphicsComponent.setBounds(new Rectangle(
                0,
                0,
                Game.config().renderConfiguration().getResolution().width,
                Game.config().renderConfiguration().getResolution().height)
        );

        try {
            backgroundGraphicsComponent.setImage(ImageIO.read(new File("game/res/system.jpg")));
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        background.addComponent(backgroundGraphicsComponent);
        backgroundGraphicsComponent.setEntity(background);
        addEntityToScene(background);

        int _id = 5000;
        int item = 0;
        for(Scene s : Game.scene().getScenes()) {
            if(s instanceof GameScene) {
                Entity menuItem = new Entity(s.getName() + "_button", _id);

                GraphicsComponent menuItemGraphicsComponent = new GraphicsComponent();
                Rectangle rectangle = new Rectangle(
                        700,
                        300 + (ITEM_HEIGHT+ITEM_MARGIN) * item,
                        ITEM_WIDTH,
                        ITEM_HEIGHT
                );
                menuItemGraphicsComponent.setBounds(rectangle);
                menuItemGraphicsComponent.setShape(rectangle);
                menuItemGraphicsComponent.setText(s.getName());
                menuItemGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 24f));
                menuItemGraphicsComponent.setTextColor(TEXT_COLOR);
                menuItemGraphicsComponent.setBorderColor(BOX_BORDER_COLOR);
                menuItemGraphicsComponent.setFillColor(BOX_COLOR);
                menuItemGraphicsComponent.setHoverColor(HOVER_COLOR);

                menuItem.addComponent(menuItemGraphicsComponent);
                menuItemGraphicsComponent.setEntity(menuItem);

                CollisionComponent menuItemCollisionComponent = new CollisionComponent();
                menuItemCollisionComponent.setCollisionBox(menuItemGraphicsComponent.getBounds());

                menuItemCollisionComponent.setEntity(menuItem);
                menuItem.addComponent(menuItemCollisionComponent);

                addEntityToScene(menuItem);
                _id += 1;
                item += 1;
            }
        }
    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }
}
