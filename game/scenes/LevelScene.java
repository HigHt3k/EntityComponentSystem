package game.scenes;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ActionComponent;
import engine.ecs.component.action.ExitAction;
import engine.ecs.component.action.StartAction;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.graphics.scene.Scene;
import engine.resource.colorpalettes.Bit8;
import engine.resource.colorpalettes.ColorPalette;
import engine.resource.fonts.FontCollection;
import engine.resource.score.HighScore;
import game.action.ShowLevelInfoAction;
import game.entities.ui.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class LevelScene extends Scene {
    private static final int ITEM_WIDTH = 350;
    private static final int ITEM_HEIGHT = 60;
    private final Entity highscoreView;
    private final Entity playersView;
    private final Entity levelInfoDesc;
    private final Entity levelInfoHead;

    public void addHighscores(int levelId) {
        StringBuilder players = new StringBuilder();
        StringBuilder highscores = new StringBuilder();

        for (HighScore hs : Game.res().score().getLevelScores(levelId)) {
            players.append(hs.getName()).append("\n");
            highscores.append(hs.getScore()).append("\n");
        }

        ((TextObject) playersView.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(players.toString());
        ((TextObject) highscoreView.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(highscores.toString());
    }

    public void removeHighscores() {
        ((TextObject) playersView.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) highscoreView.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
    }

    public void showLevelInfo(int levelId) {
        GameScene s = (GameScene) Game.scene().getScene(levelId);

        ((TextObject) levelInfoHead.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(s.getName());
        ((TextObject) levelInfoDesc.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(String.valueOf(s.getDifficulty()));
    }

    public void removeLevelInfo() {
        ((TextObject) levelInfoHead.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) levelInfoDesc.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
    }

    public LevelScene(String name, int id) {
        super(name, id);

        Font font = FontCollection.scaleFont(FontCollection.bit8Font, 25f);
        Game.res().score().loadScores("game/res/scores/highscores.xml");

        // Create the Menu GUI

        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/cablesBackground.png")), 0, 0, 1920, 1080, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        Font fontBig = FontCollection.scaleFont(FontCollection.bit8Font, 18f);
        Font fontMed = FontCollection.scaleFont(FontCollection.bit8Font, 14f);

        Entity levelInfoPanel = new SimplePanel("level_info", IdGenerator.generateId(),
                1500, 0, 402, 300, ColorPalette.setAlpha(Bit8.GREY, 100), Bit8.TRANSPARENT, Bit8.CHROME);
        addEntityToScene(levelInfoPanel);

        levelInfoHead = new TextBody("levelInfoHead", IdGenerator.generateId(),
                1500, 0, 402, 50, fontBig, Bit8.CHROME, "");
        addEntityToScene(levelInfoHead);

        levelInfoDesc = new TextBody("levelinfoDesc", IdGenerator.generateId(),
                1500, 50,402, 250, fontMed, Bit8.CHROME, "");
        addEntityToScene(levelInfoDesc);

        Entity highscorePanel = new SimplePanel("highscore_panel", IdGenerator.generateId(),
                1500, 300, 402, 500, ColorPalette.setAlpha(Bit8.GREY, 100), Bit8.TRANSPARENT, Bit8.CHROME);
        addEntityToScene(highscorePanel);

        Entity highscoreHead= new TextBody("highscoreHead", IdGenerator.generateId(),
                1500, 300, 402, 50, fontBig, Bit8.CHROME, "Highscores");
        addEntityToScene(highscoreHead);

        playersView = new TextBody("highscorePlayers", IdGenerator.generateId(),
                1500, 350, 250, 450, fontMed, Bit8.CHROME, "");
        addEntityToScene(playersView);

        highscoreView = new TextBody("highscoreView", IdGenerator.generateId(),
                1750, 350, 152, 450, fontMed, Bit8.CHROME, "");
        addEntityToScene(highscoreView);

        BufferedImage blue = null;
        BufferedImage red = null;
        BufferedImage green = null;
        BufferedImage yellow = null;

        try {
            blue = ImageIO.read(new File("game/res/menus/gui/processor_blue.png"));
            red = ImageIO.read(new File("game/res/menus/gui/processor_red.png"));
            yellow = ImageIO.read(new File("game/res/menus/gui/processor_yellow.png"));
            green = ImageIO.read(new File("game/res/menus/gui/processor_green.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addLevel(1, 300, 300, blue);
        unlockLevel(1);
        addLevel(2, 300, 400, blue);
        addLevel(3, 300, 500, blue);
        addLevel(4, 300, 600, blue);
        addLevel(5, 300, 700, blue);
        addLevel(6, 500, 200, green);
        addLevel(8, 500, 300, green);
        addLevel(7, 500, 400, green);
        addLevel(9, 500, 500, green);
        addLevel(10, 500, 600, green);
        addLevel(11, 500, 700, green);
        addLevel(12, 500, 800, green);
        addLevel(13, 500, 900, green);
        addLevel(14, 700, 400, yellow);
        addLevel(15, 700, 500, yellow);
        addLevel(16, 700, 600, yellow);
        addLevel(17, 700, 700, yellow);
        addLevel(18, 700, 800, yellow);
        addLevel(19,700,900, yellow);
        addLevel(20, 1000, 300, red);
        addLevel(21, 1000, 400, red);
        addLevel(22, 1000, 500, red);
        addLevel(23, 1000, 600, red);
        addLevel(24, 1000, 700, red);
        addLevel(25, 1000, 800, red);
        makeConnections();
        unlockAll();

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 800,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                font, new StartAction(-255),
                Bit8.CHROME,null, null
        );
        this.addEntityToScene(mainMenuButton);

        GenericButton exitButton = new GenericButton(
                "Exit", IdGenerator.generateId(),
                1600, 900,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3", font, new ExitAction(),
                Bit8.CHROME,null, null
        );
        addEntityToScene(exitButton);
    }

    public void unlockLevel(int id) {
        for(Entity e : getEntities()) {
            if (e instanceof LevelImageButton lb) {
                if (Objects.equals(lb.getName(), "lvl" + id)) {
                    lb.unlock();
                }
            }
        }
    }

    private void unlockAll() {
        for (Entity e : getEntities()) {
            if (e instanceof LevelImageButton lb) {
                lb.unlock();
            }
        }
        for (Scene s : Game.scene().getScenes()) {
            if (s instanceof GameScene gs) {
                gs.setUnlocked(true);
            }
        }
    }

    private void addLevel(int id, int x, int y, BufferedImage image) {
        LevelImageButton lvl = new LevelImageButton("lvl" + id, IdGenerator.generateId(),
                x, y, 50, 50, "", FontCollection.bit8Font,
                new StartAction(Game.scene().getScene(id)),
                image);
        lvl.getComponent(ActionComponent.class).addAction(MouseEvent.NOBUTTON, new ShowLevelInfoAction(id));
        addEntityToScene(lvl);
    }

    @Override
    public void init() {
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

    private void makeConnections() {
        ArrayList<Entity> entities = (ArrayList<Entity>) getEntities().clone();
        for(Entity e : entities) {
            if(e instanceof LevelImageButton lb) {
                Scene s = Game.scene().getScene(Integer.parseInt(lb.getName().replace("lvl", "")));
                if (s != null) {
                    if (s instanceof GameScene gs) {
                        for (int i : gs.getUnlocksNeeded()) {
                            for (Entity e2 : entities) {
                                if (e2 instanceof LevelImageButton lb2) {
                                    Scene s2 = Game.scene().getScene(Integer.parseInt(lb2.getName().replace("lvl", "")));
                                    if (s2 instanceof GameScene gs2) {
                                        if (gs2.getId() == i) {
                                            Point p1 = new Point(lb.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation());
                                            Point p2 = new Point(lb2.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation());
                                            makeConnection(p1, p2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void makeConnection(Point p1, Point p2) {
        Entity line = new LineEntity("line_connector", IdGenerator.generateId(),
                new Point(p1.x + 25, p1.y + 25), new Point(p2.x + 25, p2.y + 25), 4, Bit8.GREY);
        addEntityToScene(line);
    }
}
