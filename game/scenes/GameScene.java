package game.scenes;

import com.Game;
import com.ecs.CollisionComponent;
import com.ecs.Entity;
import com.ecs.GraphicsComponent;
import com.ecs.IntentComponent;
import com.ecs.intent.ExitIntent;
import com.ecs.intent.HoverIntent;
import com.graphics.scene.Scene;
import game.intent.StartIntent;

import java.awt.*;

public class GameScene extends Scene {
    private static final int ITEM_MARGIN = 20;
    private static final int ITEM_WIDTH = 300;
    private static final int ITEM_HEIGHT = 60;
    private static final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private static final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private static final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private static final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    public GameScene(String name, int id) {
        super(name, id);

        // Create the GUI including buttons going back to menu, exit etc.
        // Exit button
        Entity exitButton = new Entity("Exit", 4999);
        GraphicsComponent exitButtonGraphicsComponent = new GraphicsComponent();
        Rectangle exitButtonBounds = new Rectangle(1600, 900, ITEM_WIDTH, ITEM_HEIGHT);
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

        System.out.println("Exit button pos:" + exitButton.getComponent(GraphicsComponent.class).getShape().getBounds());

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

        // back to menu button
        Entity mainMenuButton = new Entity("Menu_button", 4998);
        GraphicsComponent mainMenuButtonGraphicsComponent = new GraphicsComponent();
        Rectangle mainMenuButtonBounds = new Rectangle(1600, 800, ITEM_WIDTH, ITEM_HEIGHT);
        mainMenuButtonGraphicsComponent.setBounds(mainMenuButtonBounds);
        mainMenuButtonGraphicsComponent.setShape(mainMenuButtonBounds);
        mainMenuButtonGraphicsComponent.setText("MAIN MENU");
        mainMenuButtonGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        mainMenuButtonGraphicsComponent.setTextColor(TEXT_COLOR);
        mainMenuButtonGraphicsComponent.setBorderColor(BOX_BORDER_COLOR);
        mainMenuButtonGraphicsComponent.setFillColor(BOX_COLOR);
        mainMenuButtonGraphicsComponent.setHoverColor(HOVER_COLOR);

        mainMenuButton.addComponent(mainMenuButtonGraphicsComponent);
        mainMenuButtonGraphicsComponent.setEntity(mainMenuButton);

        CollisionComponent mainMenuButtonCollisionComponent = new CollisionComponent();
        mainMenuButtonCollisionComponent.setCollisionBox(mainMenuButtonBounds);

        mainMenuButton.addComponent(mainMenuButtonCollisionComponent);
        mainMenuButtonCollisionComponent.setEntity(mainMenuButton);

        IntentComponent mainMenuButtonIntentComponent = new IntentComponent();
        HoverIntent mainMenuButtonHoverIntent = new HoverIntent();
        mainMenuButtonHoverIntent.setIntentComponent(mainMenuButtonIntentComponent);
        mainMenuButtonIntentComponent.addIntent(mainMenuButtonHoverIntent);
        StartIntent mainMenuButtonStartIntent = new StartIntent();
        mainMenuButtonStartIntent.setIntentComponent(mainMenuButtonIntentComponent);
        mainMenuButtonIntentComponent.addIntent(mainMenuButtonStartIntent);

        mainMenuButtonIntentComponent.setEntity(mainMenuButton);
        mainMenuButton.addComponent(mainMenuButtonIntentComponent);

        addEntityToScene(mainMenuButton);
    }
    @Override
    public void update() {

    }
}
