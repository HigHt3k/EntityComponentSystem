package game.scenes.game;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ExitAction;
import engine.ecs.component.action.StartAction;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.RenderObject;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.graphics.scene.Scene;
import engine.resource.ResourceManager;
import engine.resource.colorpalettes.Bit8;
import engine.resource.fonts.FontCollection;
import game.action.BuildPanelChange;
import game.action.BuildPanelChangeAction;
import game.action.SaveScoreAction;
import game.action.ValidateAction;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.components.TooltipComponent;
import game.entities.simulation.BuildPanelEntity;
import game.entities.simulation.GridEntity;
import game.entities.simulation.SimulationEntity;
import game.entities.ui.*;
import game.handler.simulation.SimulationType;
import game.scenes.base.BaseGameFieldScene;
import game.scenes.util.Difficulty;
import game.scenes.util.BuildPanelPage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class GameScene extends BaseGameFieldScene {
    private boolean unlocked = false;

    // Category colors
    private final Color NSE = Bit8.CORNFLOWER_BLUE;
    private final Color MINOR = Bit8.DARK_PASTEL_GREEN;
    private final Color MAJOR = Bit8.YELLOW;
    private final Color HAZARDOUS = Bit8.ORANGE;
    private final Color CATASTROPHIC = Bit8.RED;

    private final Difficulty difficulty;

    private final ArrayList<Integer> unlocksNeeded = new ArrayList<>();

    private final int DESIGN_CELL_SIZE = 128;
    private String description;
    private double goal = 10e-4;
    private int score = 0;
    private int tries = 0;

    private Entity previous;
    private Entity next;
    private Entity descText;
    private Entity validate;
    private Entity tipsText;
    private Entity tipsEntity;
    private Entity failTipText;
    private Entity correctSignalsTipText;
    private Entity acceptedOOCTipText;
    private Entity failTipDesc;
    private Entity acceptedOOCTipDesc;
    private Entity correctSignalsTipDesc;
    private int currentlyDisplayedDescriptionPart = 0;

    private boolean levelPassed = false;

    private final ArrayList<BuildPanelPage> pages = new ArrayList<>();

    /**
     * create a new GameScene object;
     * @param name
     * @param id
     */
    public GameScene(String name, int id, Difficulty difficulty) {
        super(name, id);
        this.difficulty = difficulty;
        setupBuildPanel();
        //TODO: move init()?

    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public Entity getTipsText() {
        return tipsText;
    }

    public Entity getTipsEntity() {
        return tipsEntity;
    }

    public Entity getAcceptedOOCTipDesc() {
        return acceptedOOCTipDesc;
    }

    public Entity getCorrectSignalsTipDesc() {
        return correctSignalsTipDesc;
    }

    public Entity getFailTipDesc() {
        return failTipDesc;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public ArrayList<Integer> getUnlocksNeeded() {
        return unlocksNeeded;
    }

    public void addUnlockNeeded(int id) {
        unlocksNeeded.add(id);
    }

    public boolean isLevelPassed() {
        return levelPassed;
    }

    public void setLevelPassed(boolean levelPassed) {
        this.levelPassed = levelPassed;
    }

    /**
     * initialize the game scene; sets up the description panel and buttons
     */
    public void init() {
        // Create the GUI including buttons going back to menu, exit etc.
        setupDescriptionPanel();
        setupButtons();
        //TODO: move cable to xml? so only the necessary choices are shown
        addToBuildPanel(500, 1000, 0, 1, 0, 0f);
        addToBuildPanel(501, 1000, 0, 1, 0, 0f);
        addToBuildPanel(502, 1000, 0, 1, 0, 0f);
        addToBuildPanel(503, 1000, 0, 1, 0, 0f);
    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }

    /**
     * setup method for buttons in the GameScene
     */
    private void setupButtons() {
        GenericButton exitButton = null;
        try {
            exitButton = new GenericButton(
                    "Exit",
                    IdGenerator.generateId(),
                    20, 40,
                    64, 64, new ExitAction(),
                    ImageIO.read(new File("res/menus/gui/buttons/exit_button.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.addEntityToScene(exitButton);

        GenericButton mainMenuButton = null;
        try {
            mainMenuButton = new GenericButton(
                    "Menu_button",
                    IdGenerator.generateId(),
                    100, 40,
                    64, 64,new StartAction(Game.scene().getScene(-255)),
                    ImageIO.read(new File("res/menus/gui/buttons/menu_icon.png"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.addEntityToScene(mainMenuButton);
    }

    /**
     * Setup method for the build panel
     */
    private void setupBuildPanel() {
        try {
            ImageEntity buildPanel = new ImageEntity("Build Panel", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/blueprint8bit.png")), 0, 800, 1500, 1080 - 850, Layer.UI);
            addEntityToScene(buildPanel);

            GenericButton leftButton = new GenericButton("left_build_panel", IdGenerator.generateId(),
                    85, 890, 40, 40,
                    new BuildPanelChangeAction(BuildPanelChange.LEFT),
                    ImageIO.read(new File("res/menus/gui/button_left.png")));

            addEntityToScene(leftButton);

            GenericButton rightButton = new GenericButton("right_build_panel", IdGenerator.generateId(),
                    1355, 890, 40, 40,
                    new BuildPanelChangeAction(BuildPanelChange.RIGHT),
                    ImageIO.read(new File("res/menus/gui/button_right.png")));
            addEntityToScene(rightButton);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set up method for the right side description and info panel
     */
    private void setupDescriptionPanel() {
        Font fontBig = FontCollection.scaleFont(FontCollection.bit8Font, 18f);
        Font fontMed = FontCollection.scaleFont(FontCollection.bit8Font, 14f);

        Entity desc = null;
        try {
            desc = new ImageEntity("desc", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/hud_element_1.png")),
                    1500, 0, 402, 350, Layer.UI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(desc);

        Entity goal = null;
        try {
            goal = new ImageEntity("goal", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/hud_element_2.png")),
                    1500, 375, 402, 200, Layer.UI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(goal);

        Entity tips = null;
        try {
            tips = new ImageEntity("tips", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/hud_element_3.png")),
                    1500, 600, 402, 400, Layer.UI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(tips);

        tipsEntity = new TextBody("tipsEntityType", IdGenerator.generateId(),
                1518, 620, 250, 50, fontBig, Bit8.CHROME, "");
        addEntityToScene(tipsEntity);

        tipsText = new TextBody("tipsText", IdGenerator.generateId(),
                1518, 752, 402 - 18, 300, fontMed, Bit8.CHROME, "");
        addEntityToScene(tipsText);

        failTipDesc = new TextBody("failTipDesc", IdGenerator.generateId(),
                1540, 858, 250, 15, fontMed, Bit8.CHROME, "");
        addEntityToScene(failTipDesc);

        failTipText = new TextBody("failTipText", IdGenerator.generateId(),
                1750, 858, 152, 15,fontMed, Bit8.CHROME, "");
        addEntityToScene(failTipText);

        correctSignalsTipDesc = new TextBody("correctSignalsTipDesc", IdGenerator.generateId(),
                1540, 905, 250, 15, fontMed,Bit8.CHROME, "");
        addEntityToScene(correctSignalsTipDesc);

        correctSignalsTipText = new TextBody("correctSignalsTipText", IdGenerator.generateId(),
                1750, 905, 152, 15, fontMed, Bit8.CHROME, "");
        addEntityToScene(correctSignalsTipText);

        acceptedOOCTipDesc = new TextBody("acceptedOOCTipDesc", IdGenerator.generateId(),
                1540, 952, 250, 15, fontMed,Bit8.CHROME, "");
        addEntityToScene(acceptedOOCTipDesc);

        acceptedOOCTipText = new TextBody("acceptedOOCTipText", IdGenerator.generateId(),
                1750, 952, 152, 15,fontMed, Bit8.CHROME, "");
        addEntityToScene(acceptedOOCTipText);

        descText = new TextBody("descText", IdGenerator.generateId(),
                1518, 155, 402 - 18, 300, fontMed, Bit8.CHROME, description);
        addEntityToScene(descText);

        Entity descHead = new TextBody("descHead", IdGenerator.generateId(),
                1518, 20, 250, 50, fontBig, Bit8.CHROME, "@49");
        addEntityToScene(descHead);

        try {
            previous = new GenericButton("previous", IdGenerator.generateId(),
                    1520, 280, 40, 40, new StartAction(null),
                    ImageIO.read(new File("res/menus/gui/button_left_white.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(previous);

        try {
            next = new GenericButton("next", IdGenerator.generateId(),
                    1840, 280, 40, 40, new StartAction(null),
                    ImageIO.read(new File("res/menus/gui/button_right_white.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(next);

        Entity goalHead = new TextBody("goalHead", IdGenerator.generateId(),
                1518, 396, 402, 50, fontBig, Bit8.CHROME, "@50");
        addEntityToScene(goalHead);

        Entity safetyReqDesc = new TextBody("safetyReqDesc", IdGenerator.generateId(),
                1540, 480, 250, 15, fontMed, Bit8.CHROME, "@51");
        addEntityToScene(safetyReqDesc);

        Color safetyReqColor = getSafetyReqColor(getGoal());

        Entity safetyReqContent = new TextBody("safetyReqContent", IdGenerator.generateId(),
                1780, 480, 152, 15, fontMed, safetyReqColor, String.valueOf(getGoal()));
        addEntityToScene(safetyReqContent);

        Entity minActuatorsDesc = new TextBody("minActuatorsDesc", IdGenerator.generateId(),
                1540, 527, 250, 15, fontMed, Bit8.CHROME, "@52"
        );
        addEntityToScene(minActuatorsDesc);

        Entity minActuatorsContent = new TextBody("minActuatorsContent", IdGenerator.generateId(),
                1780, 527, 152, 15, fontMed, Bit8.CHROME, String.valueOf(accGoal));
        addEntityToScene(minActuatorsContent);

        validate = new GenericButton("validate", IdGenerator.generateId(),
                1200, 10, 300, 50, "@30", FontCollection.bit8FontHuge, new ValidateAction(), Bit8.DARK_GREY, null, null);
        addEntityToScene(validate);
    }

    public Entity getValidate() {
        return validate;
    }

    public Entity getNext() {
        return next;
    }

    public Entity getPrevious() {
        return previous;
    }

    /**
     * i is either -1 or +1
     * @param i
     */
    public void setDescriptionDisplayUsingOffset(int i) {
        if(i != -1 && i != 1) {
            return;
        }

        try {
            description = descriptions.get(currentlyDisplayedDescriptionPart + i);
            currentlyDisplayedDescriptionPart += i;
            ((TextObject) descText.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(description);
        } catch(IndexOutOfBoundsException ex) {
            // This is okay here.
        }
    }

    private Color getSafetyReqColor(double d) {
        if(getGoal() <= 1e-9) {
            return CATASTROPHIC;
        }
        else if(getGoal() <= 1e-7){
            return HAZARDOUS;
        }
        else if(getGoal() <= 1e-5){
            return MAJOR;
        }
        else if(getGoal() <= 1e-3) {
            return MINOR;
        }
        else {
            return NSE;
        }
    }

    public Entity getAcceptedOOCTipText() {
        return acceptedOOCTipText;
    }

    public Entity getFailTipText() {
        return failTipText;
    }

    //
    public Entity getCorrectSignalsTipText() {
        return correctSignalsTipText;
    }

    /**
     * Display the currently hovered tool tip component
     *
     * @param entity: the entity which contains the tool tip
     */
    public void displayToolTip(Entity entity) {
        ((TextObject) getTipsText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(
                entity.getComponent(TooltipComponent.class).getTooltipText());
        ((TextObject) getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(entity.getComponent(TooltipComponent.class).getFailureRatio());
        ((TextObject) getCorrectSignalsTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(entity.getComponent(TooltipComponent.class).getCorrectInputSignals());
        ((TextObject) getAcceptedOOCTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(entity.getComponent(TooltipComponent.class).getAcceptedOOCSignals());
        ((TextObject) getFailTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("@54");
        ((TextObject) getAcceptedOOCTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("@55");
        ((TextObject) getCorrectSignalsTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("@53");
        ((TextObject) getTipsEntity().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(String.valueOf(entity.getComponent(TooltipComponent.class).getType()));
    }

    /**
     * Remove the tool tip
     */
    public void displayEmptyToolTip() {
        ((TextObject) getTipsText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getCorrectSignalsTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getAcceptedOOCTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getFailTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getAcceptedOOCTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getCorrectSignalsTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getTipsEntity().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
    }

    /**
     * Get the score from the current scene
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    public void playSky() {
        try {
            Image img1 = new ImageIcon(new File("res/animations/sky-animated.gif").toURL()).getImage();
            ImageEntity ex1 = new ImageEntity("sky-animated", IdGenerator.generateId(),
                    img1, 0, 0, 1920, 1080, Layer.BACKGROUND, false, 0);
            addEntityToScene(ex1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayLevelFinished(int score, double[] probabilities) {
        this.score = (int) (score * Math.pow(0.98f, tries));
        tries++;
        addEntityToScene(new ScoreBox("Scorebox", IdGenerator.generateId(),
                FontCollection.bit8FontLarge, score,
                1920 / 2 - 300, 1080 / 2 - 250, 600, 500, "level passed!"));
        GenericButton saveScore = new GenericButton(
                "ScoreSaveButton", IdGenerator.generateId(),
                1920 / 2 - 150, 1080 / 2 + 50, 300, 40,
                "SAVE SCORE", FontCollection.bit8FontMedium, new SaveScoreAction()
        );
        addEntityToScene(saveScore);

        addEntityToScene(new GenericButton(
                "Back to Menu", IdGenerator.generateId(),
                1920 / 2 - 150, 1080 / 2 - 50, 300, 40,
                "BACK TO MENU", FontCollection.bit8FontMedium, new StartAction(-255)
        ));

        createScale(probabilities);
    }

    /**
     * Display a level not finished screen including some information about the reason
     *
     * @param probabilities: the probabilities needed to calculate the scale
     */
    public void displayLevelNotFinished(double[] probabilities) {
        tries++;
        addEntityToScene(new ScoreBox("scorebox", IdGenerator.generateId(),
                FontCollection.bit8FontLarge, 0,
                1920 / 2 - 300, 1080 / 2 - 250, 600, 500, "Requirements not met!"));
        GenericButton back = new GenericButton(
                "back", IdGenerator.generateId(),
                1920 / 2 - 150, 1080 / 2 + 50, 300, 40,
                "TRY AGAIN", Game.res().loadFont("res/font/joystix monospace.ttf", 18f), new StartAction(null)
        );
        addEntityToScene(back);

        createScale(probabilities);

        try {
            Image img1 = new ImageIcon(new File("res/animations/explosion-medium-rotated.gif").toURL()).getImage();
            ImageEntity ex1 = new ImageEntity("ex1", IdGenerator.generateId(),
                    img1, 500, 300, 155, 155, Layer.GAMELAYER3, false, 0);
            addEntityToScene(ex1);

            Image img2 = new ImageIcon(new File("res/animations/explosion-slow.gif").toURL()).getImage();
            ImageEntity ex2 = new ImageEntity("ex2", IdGenerator.generateId(),
                    img2, 700, 200, 200, 200, Layer.GAMELAYER3, false, 0);
            addEntityToScene(ex2);

            Image img3 = new ImageIcon(new File("res/animations/explosion-medium.gif").toURL()).getImage();
            ImageEntity ex3 = new ImageEntity("ex3", IdGenerator.generateId(),
                    img3, 100, 250, 340, 340, Layer.GAMELAYER3, false, 0);
            addEntityToScene(ex3);

            Image img4 = new ImageIcon(new File("res/animations/explosion-medium-rotated.gif").toURL()).getImage();
            ImageEntity ex4 = new ImageEntity("ex4", IdGenerator.generateId(),
                    img4, 520, 700, 250, 200, Layer.GAMELAYER3, false, 0);
            addEntityToScene(ex4);

            Image img5 = new ImageIcon(new File("res/animations/explosion-slow.gif").toURL()).getImage();
            ImageEntity ex5 = new ImageEntity("ex5", IdGenerator.generateId(),
                    img5, 1050, 320, 370, 230, Layer.GAMELAYER3, false, 0);
            addEntityToScene(ex5);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a scale representing proximity of the user solution to the target failure probability
     *
     * @param probabilities: Passive and OOC failure probabilities for the users' solution
     */
    private void createScale(double[] probabilities) {
        int scaleWidth = 500;
        int positionGoal;
        int positionScoreOOC;
        int positionScorePassive;
        if (goal <= 0.0) {
            positionGoal = scaleWidth;
        } else {
            positionGoal = (int) (Math.abs(Math.log10(goal)) * scaleWidth / 10);
        }
        if (probabilities[0] <= 0.0) {
            positionScorePassive = scaleWidth;
        } else {
            positionScorePassive = (int) (Math.abs(Math.log10(probabilities[0])) * scaleWidth / 10);
        }
        if (positionScorePassive > scaleWidth) {
            positionScorePassive = scaleWidth;
        }
        if (probabilities[1] <= 0.0) {
            positionScoreOOC = scaleWidth;
        } else {
            positionScoreOOC = (int) (Math.abs(Math.log10(probabilities[1])) * scaleWidth / 10);
        }
        if (positionScoreOOC > scaleWidth) {
            positionScoreOOC = scaleWidth;
        }


        ScaleEntity scoreScale = new ScaleEntity(
                "scale", IdGenerator.generateId(), 1920 / 2 - scaleWidth / 2, 1080 / 2 - 100, scaleWidth, 10,
                new Marker(new Point(1920 / 2 - scaleWidth / 2 + positionGoal, 1080 / 2 - 100), getSafetyReqColor(goal)),
                new Marker(new Point(1920 / 2 - scaleWidth / 2 + positionScoreOOC, 1080 / 2 - 100), Bit8.JAM),
                new Marker(new Point(1920 / 2 - scaleWidth / 2 + positionScorePassive, 1080 / 2 - 100), Bit8.CORNFLOWER_BLUE)
        );
        addEntityToScene(scoreScale);
    }
}
