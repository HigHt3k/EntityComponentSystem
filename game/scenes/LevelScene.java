package game.scenes;

import com.Game;
import com.IdGenerator;
import com.ecs.component.IntentComponent;
import com.ecs.component.graphics.GraphicsComponent;
import com.ecs.entity.Entity;
import com.ecs.entity.GenericButton;
import com.ecs.intent.ExitIntent;
import com.graphics.scene.Scene;
import com.resource.colorpalettes.Bit8;
import com.resource.fonts.FontCollection;
import game.entities.LevelButton;
import game.intent.StartIntent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LevelScene extends Scene {
    private static final int ITEM_MARGIN = 20;
    private static final int ITEM_WIDTH = 350;
    private static final int ITEM_HEIGHT = 60;
    private static final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private static final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private static final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private static final Color HOVER_COLOR = new Color(40, 40, 40, 150);

    public LevelScene(String name, int id) {
        super(name, id);

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 15f);

        // Create the Menu GUI
        Entity background = new Entity("Background", IdGenerator.generateId());
        GraphicsComponent backgroundGraphicsComponent = new GraphicsComponent();
        backgroundGraphicsComponent.setBounds(new Rectangle(
                0,
                0,
                1920,
                1080
                )
        );

        try {
            backgroundGraphicsComponent.setImage(ImageIO.read(new File("game/res/bottom-view-plane-sky.jpg")));
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        background.addComponent(backgroundGraphicsComponent);
        backgroundGraphicsComponent.setEntity(background);
        addEntityToScene(background);

        addLevel(1, 300, 300, Bit8.CORNFLOWER_BLUE);
        unlockLevel(1);
        addLevel(2, 300, 350, Bit8.CORNFLOWER_BLUE);
        addLevel(3, 300, 400, Bit8.CORNFLOWER_BLUE);
        addLevel(4, 300, 450, Bit8.CORNFLOWER_BLUE);
        addLevel(5, 300, 500, Bit8.CORNFLOWER_BLUE);
        addLevel(6, 500, 350, Bit8.DARK_PASTEL_GREEN);
        unlockLevel(6);

        /*int item = 0;
        for(Scene s : Game.scene().getScenes()) {
            if(s instanceof GameScene) {
                GenericButton menuItem = new GenericButton(
                        s.getName() + "_button", IdGenerator.generateId(),
                        1920 / 2 - ITEM_WIDTH / 2, 200 + (ITEM_HEIGHT + ITEM_MARGIN) * item,
                        ITEM_WIDTH, ITEM_HEIGHT,
                        s.getName(), font
                );
                menuItem.addIntent(new StartIntent());
                addEntityToScene(menuItem);

                item += 1;
            }
        }

         */

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 800,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                font
        );

        mainMenuButton.addIntent(new StartIntent());
        this.addEntityToScene(mainMenuButton);

        GenericButton exitButton = new GenericButton(
                "Exit", IdGenerator.generateId(),
                1600, 900,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3", font
                );
        exitButton.addIntent(new ExitIntent());
        addEntityToScene(exitButton);
    }

    public void unlockLevel(int id) {
        for(Entity e : getEntities()) {
            if(e.getComponent(IntentComponent.class) != null) {
                if(e.getComponent(IntentComponent.class).getIntent(StartIntent.class) != null) {
                    if(e.getComponent(IntentComponent.class).getIntent(StartIntent.class).getScene() == Game.scene().getScene(id)) {
                        ((GameScene) Game.scene().getScene(id)).setUnlocked(true);
                        if(e instanceof LevelButton lb) {
                            lb.unlock();
                        }
                    }
                }
            }
        }
    }

    private void addLevel(int id, int x, int y, Color c) {
        LevelButton lvl = new LevelButton("lvl" + id, IdGenerator.generateId(),
                x, y, 25, 25, "", FontCollection.bit8Font, c);
        lvl.addIntent(new StartIntent(Game.scene().getScene(id)));
        addEntityToScene(lvl);
    }

    @Override
    public void init() {
        checkUnlocks();
    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }

    private void checkUnlocks() {
        for(Scene s : Game.scene().getScenes()) {
            if(s instanceof GameScene gs) {
                ArrayList<Integer> unlocksNeeded = gs.getUnlocksNeeded();
                boolean allLevelsPassed = true;
                for(Scene sCompare : Game.scene().getScenes()) {
                    if(sCompare instanceof GameScene gsCompare) {
                        if(!unlocksNeeded.contains(gsCompare.getId())) {
                            continue;
                        }
                        if(!gsCompare.isLevelPassed()) {
                            allLevelsPassed = false;
                        }
                    }
                }
                if(allLevelsPassed) {
                    unlockLevel(gs.getId());
                }
            }
        }
    }
}
