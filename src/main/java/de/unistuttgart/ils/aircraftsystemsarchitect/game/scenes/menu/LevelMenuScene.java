package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.menu;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.ActionComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.StartAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.ImageEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene.Scene;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts.FontCollection;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.score.HighScore;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.action.ShowLevelInfoAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.LevelImageButton;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.LineEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.SimplePanel;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.TextBody;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.tutorial.TutorialHandler;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base.BaseMenuScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.tutorial.Tutorial;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util.CharacterTalking;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.GameScene;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class LevelMenuScene extends BaseMenuScene implements Tutorial {
    private static final int ITEM_WIDTH = 350;
    private static final int ITEM_HEIGHT = 60;
    protected int X_MARGIN = 200;
    protected int Y_MARGIN = 300;
    private final Entity highscoreView;
    private final Entity playersView;
    private final Entity levelInfoDesc;
    private final Entity levelInfoHead;
    private boolean firstTimeOpened = true;
    private boolean tutorialRunning = false;
    private int tinaId = 805;
    private int ingoId = 800;
    private CharacterTalking currentlyTalking;

    public LevelMenuScene(String name, int id) {
        super(name, id);

        try {
            ImageEntity aircraftGameScene = new ImageEntity("aircraftGameScene", IdGenerator.generateId(),
                    ImageIO.read(new File("res/backgrounds/aircraft-game-scene.png")), X_MARGIN - 120,
                    Y_MARGIN - 510, 1200, (int) (1200 * 1.0608f), Layer.GAMELAYER1);
            addEntityToScene(aircraftGameScene);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        Game.res().score().loadScores("res/scores/highscores.xml");

        // Create the Menu GUI
        Font fontBig = FontCollection.scaleFont(FontCollection.bit8Font, 25f);
        Font fontMed = FontCollection.scaleFont(FontCollection.bit8Font, 18f);

        BufferedImage blue = null;
        BufferedImage red = null;
        BufferedImage green = null;
        BufferedImage yellow = null;

        try {
            blue = ImageIO.read(new File("res/menus/gui/processor_blue.png"));
            red = ImageIO.read(new File("res/menus/gui/processor_red.png"));
            yellow = ImageIO.read(new File("res/menus/gui/processor_yellow.png"));
            green = ImageIO.read(new File("res/menus/gui/processor_green.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addLevel(1, 375, 375, blue);
        unlockLevel(1);
        addLevel(2, 435, 425, blue);
        addLevel(3, 495, 375, blue);
        addLevel(4, 565, 425, blue);
        addLevel(5, 635, 375, blue);
        addLevel(6, 675, 300, green);
        addLevel(8, 675, 230, green);
        addLevel(7, 650, 160, green);
        addLevel(17, 625, 90, green);
        addLevel(9, 750, 104, green);
        addLevel(10, 750, 275, yellow);
        addLevel(11, 725, 400, yellow);
        addLevel(12, 700, 500, yellow);

        addLevel(13, 675, 600, yellow);
        addLevel(14, 645, 670, yellow);
        addLevel(15, 605, 740, yellow);
        addLevel(16, 570, 810, red);
        addLevel(18, 530, 880, red);
        addLevel(19,760,550, red);

        addLevel(20, 800, 425, red);
        addLevel(21, 860, 375, red);
        addLevel(22, 940, 425, red);
        addLevel(23, 1000, 375, red);
        addLevel(24, 1060, 425, red);
        addLevel(25, 1120, 400, red);

        makeConnections();
        unlockAll();

        Entity levelInfoPanel = null;
        try {
            levelInfoPanel = new ImageEntity("level_info", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/hud_element_5.png")),
                    1415, 90, 474, 925, Layer.UI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(levelInfoPanel);

        levelInfoHead = new TextBody("levelInfoHead", IdGenerator.generateId(),
                1515, 110, 320, 50, fontBig, Bit8.DARK_GREY, "");
        addEntityToScene(levelInfoHead);

        levelInfoDesc = new TextBody("levelinfoDesc", IdGenerator.generateId(),
                1450, 183, 414, 180, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(levelInfoDesc);

        Entity highscoreHead= new TextBody("highscoreHead", IdGenerator.generateId(),
                1515, 420, 320, 50, fontBig, Bit8.DARK_GREY, "@71");
        addEntityToScene(highscoreHead);

        playersView = new TextBody("highscorePlayers", IdGenerator.generateId(),
                1425, 480, 320, 350, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(playersView);

        highscoreView = new TextBody("highscoreView", IdGenerator.generateId(),
                1750, 480, 150, 350, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(highscoreView);
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
        checkUnlocks();
        if(firstTimeOpened) {
            tutorialDialogue();
            firstTimeOpened = false;
        }
    }

    public void checkUnlocks() {
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

    public void addHighscores(int levelId) {
        StringBuilder players = new StringBuilder();
        StringBuilder highscores = new StringBuilder();

        for (HighScore hs : Game.res().score().getLevelScores(levelId)) {
            players.append(hs.getName()).append("\n");
            highscores.append(hs.getScore()).append("\n");
        }

        playersView.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(players.toString());
        highscoreView.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(highscores.toString());
    }

    public void showLevelInfo(int levelId) {
        GameScene s = (GameScene) Game.scene().getScene(levelId);

        levelInfoHead.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(s.getName());
        levelInfoDesc.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(String.valueOf(s.getDifficulty()));
    }

    /**
     * Show a tutorial / introduction dialogue before starting to choose a level
     */
    @Override
    public void tutorialDialogue() {
        tutorialRunning = true;
        deactivateMap();
        Game.input().addHandler(new TutorialHandler());
        // make background blurry
        Entity blur = new SimplePanel("blur", IdGenerator.generateId(),
                0, 0, 1920, 1080, Bit8.setAlpha(Bit8.WHITE, 170), null, null, Layer.UI);
        addEntityToScene(blur);

        // create character models
        Entity tina = null;
        try {
            tina = new ImageEntity("tina", IdGenerator.generateId(),
                    ImageIO.read(new File("res/character/Tina-Technik.png")), 1100, 500, 19 * 12, 27 * 12, Layer.UI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(tina);

        Entity ingo = null;
        try {
            ingo = new ImageEntity("ingo", IdGenerator.generateId(),
                    ImageIO.read(new File("res/character/Ingo-Ingenieur.png")), 450, 600, 19 * 12, 27 * 12, Layer.UI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(ingo);


        // Speech bubble for character Ingo
        Entity speechBubble1 = null;
        try {
            speechBubble1 = new ImageEntity("bubble1", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/speechbubble.png")), 200, 400, 64*8, 32*8, Layer.UI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(speechBubble1);
        Entity textIngo = new TextBody("text1", IdGenerator.generateId(),
                220, 410, 64*8 - 40, 32*8 - 20, FontCollection.bit8FontLarge, Bit8.DARK_GREY, "@" + ingoId);
        addEntityToScene(textIngo);
        currentlyTalking = CharacterTalking.INGO;
        ingoId++;


        // Speech bubble for character Tina
        Entity speechBubble2 = null;
        try {
            speechBubble2 = new ImageEntity("bubble2", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/speechbubble.png")), 800, 300, 64*8, 32*8, Layer.UI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addEntityToScene(speechBubble2);
        Entity textTina = new TextBody("text2", IdGenerator.generateId(),
                820, 310, 64*8-40, 32*8-20, FontCollection.bit8FontLarge, Bit8.DARK_GREY, "");
        addEntityToScene(textTina);
    }

    /**
     * Check if the tutorial is currently running
     * @return true if running, false if not running
     */
    @Override
    public boolean isTutorialRunning() {
        return tutorialRunning;
    }

    @Override
    public void setTutorialRunning() {

    }

    /**
     * Show the next tutorial part / speechbubble text. Remove if the text is finished
     */
    @Override
    public void showNextTutorialText() {
        if(tinaId == 809 && ingoId == 804) {
            activateMap();
            removeTutorial();
            return;
        }
        if(currentlyTalking == CharacterTalking.INGO) {
            getEntityByName("text1").getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
            getEntityByName("text2").getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@" + tinaId);
            tinaId++;
            currentlyTalking = CharacterTalking.TINA;
            if(ingoId == 803) {
                removeEntityFromScene(getEntityByName("menu-marker"));
            }
        } else if(currentlyTalking == CharacterTalking.TINA) {
            if(ingoId == 802) {
                Entity menuMarker = new SimplePanel("menu-marker", IdGenerator.generateId(),
                        1498, 13,
                        68, 68,
                        null, Bit8.RED, null);
                addEntityToScene(menuMarker);
            }
            getEntityByName("text1").getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@" + ingoId);
            getEntityByName("text2").getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");

            ingoId++;
            currentlyTalking = CharacterTalking.INGO;
        }
    }

    @Override
    public void removeTutorial() {
        tutorialRunning = false;
        Game.input().removeHandler(TutorialHandler.class);
        removeEntityFromScene(getEntityByName("text1"));
        removeEntityFromScene(getEntityByName("text2"));
        removeEntityFromScene(getEntityByName("blur"));
        removeEntityFromScene(getEntityByName("bubble1"));
        removeEntityFromScene(getEntityByName("bubble2"));
        removeEntityFromScene(getEntityByName("ingo"));
        removeEntityFromScene(getEntityByName("tina"));
    }

    /**
     * activate the map content
     */
    private void activateMap() {
        for(Entity e : Query.getEntitiesWithComponent(RenderComponent.class)) {
            e.getComponent(RenderComponent.class).showAllObjects();
        }

        for(Entity e : Query.getEntitiesWithComponent(ColliderComponent.class)) {
            e.getComponent(ColliderComponent.class).activateAllObjects();
        }
    }

    /**
     * deactivate the map content
     */
    private void deactivateMap() {
        for(Entity e : Query.getEntitiesWithComponent(RenderComponent.class)) {
            if(e.getName().contains("lvl") || e.getName().equals("line_connector")) {
                e.getComponent(RenderComponent.class).hideAllObjects();
            }
        }

        for(Entity e : Query.getEntitiesWithComponent(ColliderComponent.class)) {
            if(e.getName().contains("lvl") || e.getName().equals("line_connector")) {
                e.getComponent(ColliderComponent.class).deactivateAllObjects();
            }
        }
    }

    /**
     * only show the map, without activating buttons
     */
    private void showMap() {
        for(Entity e : Query.getEntitiesWithComponent(RenderComponent.class)) {
            e.getComponent(RenderComponent.class).showAllObjects();
        }
    }
}
