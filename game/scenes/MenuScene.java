package game.scenes;

import com.Game;
import com.ecs.*;
import com.ecs.intent.ExitIntent;
import com.graphics.scene.Scene;
import com.ecs.intent.HoverIntent;

import javax.imageio.ImageIO;
import java.awt.*;
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
            backgroundGraphicsComponent.setImage(ImageIO.read(new File("game/res/bottom-view-plane-sky.jpg")));
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
                menuItemGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 14f));
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

                IntentComponent menuItemIntentComponent = new IntentComponent();
                HoverIntent hi = new HoverIntent();
                hi.setIntentComponent(menuItemIntentComponent);
                menuItemIntentComponent.addIntent(hi);

                menuItemIntentComponent.setEntity(menuItem);
                menuItem.addComponent(menuItemIntentComponent);

                addEntityToScene(menuItem);
                _id += 1;
                item += 1;
            }
        }

        Entity exitButton = new Entity("Exit", 4999);
        GraphicsComponent exitButtonGraphicsComponent = new GraphicsComponent();
        Rectangle exitButtonBounds = new Rectangle(1400, 900, ITEM_WIDTH, ITEM_HEIGHT);
        exitButtonGraphicsComponent.setBounds(exitButtonBounds);
        exitButtonGraphicsComponent.setShape(exitButtonBounds);
        exitButtonGraphicsComponent.setText("EXIT");
        exitButtonGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        exitButtonGraphicsComponent.setTextColor(TEXT_COLOR);
        exitButtonGraphicsComponent.setBorderColor(BOX_BORDER_COLOR);
        exitButtonGraphicsComponent.setFillColor(BOX_COLOR);
        exitButtonGraphicsComponent.setHoverColor(HOVER_COLOR);

        exitButton.addComponent(exitButtonGraphicsComponent);
        exitButtonGraphicsComponent.setEntity(exitButton);

        CollisionComponent exitButtonCollisionComponent = new CollisionComponent();
        exitButtonCollisionComponent.setCollisionBox(exitButtonBounds);

        exitButton.addComponent(exitButtonCollisionComponent);
        exitButtonCollisionComponent.setEntity(exitButton);

        IntentComponent exitButtonIntentComponent = new IntentComponent();
        HoverIntent hi = new HoverIntent();
        hi.setIntentComponent(exitButtonIntentComponent);
        exitButtonIntentComponent.addIntent(hi);
        ExitIntent ei = new ExitIntent();
        ei.setIntentComponent(exitButtonIntentComponent);
        exitButtonIntentComponent.addIntent(ei);

        exitButtonIntentComponent.setEntity(exitButton);
        exitButton.addComponent(exitButtonIntentComponent);


        addEntityToScene(exitButton);
    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }
}
