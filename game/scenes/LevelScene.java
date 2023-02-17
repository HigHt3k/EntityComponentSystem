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
import engine.resource.fonts.FontCollection;
import engine.resource.score.HighScore;
import game.action.ShowLevelInfoAction;
import game.entities.ui.LevelButton;
import game.entities.ui.LineEntity;
import game.entities.ui.SimplePanel;
import game.entities.ui.TextBody;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

        System.out.println(levelId + ":" + players);

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

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 15f);
        Game.res().score().loadScores("game/res/scores/highscores.xml");

        // Create the Menu GUI

        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/bottom-view-plane-sky.jpg")), 0, 0, 1920, 1080, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        Font fontBig = FontCollection.scaleFont(FontCollection.bit8Font, 18f);
        Font fontMed = FontCollection.scaleFont(FontCollection.bit8Font, 14f);

        Entity levelInfoPanel = new SimplePanel("level_info", IdGenerator.generateId(),
                1500, 0, 402, 300, Bit8.CHROME, Bit8.JAM, Bit8.DARK_GREY);
        addEntityToScene(levelInfoPanel);

        levelInfoHead = new TextBody("levelInfoHead", IdGenerator.generateId(),
                1500, 0, 402, 50, fontBig, Bit8.DARK_GREY, "");
        addEntityToScene(levelInfoHead);

        levelInfoDesc = new TextBody("levelinfoDesc", IdGenerator.generateId(),
                1500, 50,402, 250, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(levelInfoDesc);

        Entity highscorePanel = new SimplePanel("highscore_panel", IdGenerator.generateId(),
                1500, 300, 402, 500, Bit8.CHROME, Bit8.JAM, Bit8.DARK_GREY);
        addEntityToScene(highscorePanel);

        Entity highscoreHead= new TextBody("highscoreHead", IdGenerator.generateId(),
                1500, 300, 402, 50, fontBig, Bit8.DARK_GREY, "Highscores");
        addEntityToScene(highscoreHead);

        playersView = new TextBody("highscorePlayers", IdGenerator.generateId(),
                1500, 350, 250, 450, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(playersView);

        highscoreView = new TextBody("highscoreView", IdGenerator.generateId(),
                1750, 350, 152, 450, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(highscoreView);

        addLevel(1, 300, 300, Bit8.CORNFLOWER_BLUE);
        unlockLevel(1);
        addLevel(2, 300, 400, Bit8.CORNFLOWER_BLUE);
        addLevel(3, 300, 500, Bit8.CORNFLOWER_BLUE);
        addLevel(4, 300, 600, Bit8.CORNFLOWER_BLUE);
        addLevel(5, 300, 700, Bit8.CORNFLOWER_BLUE);
        addLevel(6, 500, 200, Bit8.DARK_PASTEL_GREEN);
        addLevel(7,500, 300, Bit8.DARK_PASTEL_GREEN);
        addLevel(8,500, 400, Bit8.DARK_PASTEL_GREEN);
        addLevel(9,500, 500, Bit8.DARK_PASTEL_GREEN);
        addLevel(10,500, 600, Bit8.DARK_PASTEL_GREEN);
        addLevel(11,500, 700, Bit8.DARK_PASTEL_GREEN);
        addLevel(12,500, 800, Bit8.DARK_PASTEL_GREEN);
        addLevel(13,500, 900, Bit8.DARK_PASTEL_GREEN);
        addLevel(14,700,400, Bit8.ORANGE);
        addLevel(15,700,500, Bit8.ORANGE);
        addLevel(16,700,600, Bit8.ORANGE);
        addLevel(17,700,700, Bit8.ORANGE);
        addLevel(18,700,800, Bit8.ORANGE);
        addLevel(19,700,900, Bit8.ORANGE);
        addLevel(20, 1000, 300, Bit8.RED);
        addLevel(21, 1000, 400, Bit8.RED);
        addLevel(22, 1000, 500, Bit8.RED);
        addLevel(23, 1000, 600, Bit8.RED);
        addLevel(24, 1000, 700, Bit8.RED);
        addLevel(25, 1000, 800, Bit8.RED);
        makeConnections();
        unlockAll();

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 800,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                font, new StartAction(-255)
        );
        this.addEntityToScene(mainMenuButton);

        GenericButton exitButton = new GenericButton(
                "Exit", IdGenerator.generateId(),
                1600, 900,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3", font, new ExitAction()
        );
        addEntityToScene(exitButton);
    }

    public void unlockLevel(int id) {
        for(Entity e : getEntities()) {
            if (e instanceof LevelButton lb) {
                if (lb.getName() == "lvl" + id) {
                    lb.unlock();
                }
            }
        }
    }

    private void unlockAll() {
        for (Entity e : getEntities()) {
            if (e instanceof LevelButton lb) {
                lb.unlock();
            }
        }
        for (Scene s : Game.scene().getScenes()) {
            if (s instanceof GameScene gs) {
                gs.setUnlocked(true);
            }
        }
    }

    private void addLevel(int id, int x, int y, Color c) {
        LevelButton lvl = new LevelButton("lvl" + id, IdGenerator.generateId(),
                x, y, 50, 50, "", FontCollection.bit8Font, c,
                new StartAction(Game.scene().getScene(id)));
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
            if(e instanceof LevelButton lb) {
                Scene s = Game.scene().getScene(Integer.parseInt(lb.getName().replace("lvl", "")));
                if (s != null) {
                    if (s instanceof GameScene gs) {
                        for (int i : gs.getUnlocksNeeded()) {
                            for (Entity e2 : entities) {
                                if (e2 instanceof LevelButton lb2) {
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
                new Point(p1.x + 25, p1.y + 25), new Point(p2.x + 25, p2.y + 25), 4, Bit8.NAVY);
        addEntityToScene(line);
    }
}
