package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.StartAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.GenericButton;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.GenericButtonFront;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.ImageEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts.FontCollection;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.action.NextLevelAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.action.SaveScoreAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.action.ValidateAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.TooltipComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.TextBody;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base.BaseGameFieldScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util.BuildPanelPage;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util.Difficulty;

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
    private final double goal = 10e-4;
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
        //TODO: move cable to xml? so only the necessary choices are shown
    }

    @Override
    public void update() {
        for(Entity e : (ArrayList<Entity>) getEntities().clone()) {
            e.update();
        }
    }

    /**
     * set up method for the right side description and info panel
     */
    private void setupDescriptionPanel() {
        Font fontBig = FontCollection.scaleFont(FontCollection.bit8Font, 25f);
        Font fontMed = FontCollection.scaleFont(FontCollection.bit8Font, 18f);

        Entity desc = null;
        try {
            desc = new ImageEntity("desc", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/hud_element_4.png")),
                    1415, 90, 474, 925, Layer.UI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(desc);

        Entity descHead = new TextBody("descHead", IdGenerator.generateId(),
                1515, 110, 320, 50, fontBig, Bit8.DARK_GREY, "@49");
        addEntityToScene(descHead);

        descText = new TextBody("descText", IdGenerator.generateId(),
                1450, 183, 414, 180, fontMed, Bit8.DARK_GREY, descriptions.get(0));
        addEntityToScene(descText);

        try {
            previous = new GenericButton("previous", IdGenerator.generateId(),
                    1420, 158 + 50, 40, 150, new StartAction(null),
                    ImageIO.read(new File("res/menus/gui/button_left.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(previous);

        try {
            next = new GenericButton("next", IdGenerator.generateId(),
                    1415 - 5 + 474 - 40, 158 + 50, 40, 150, new StartAction(null),
                    ImageIO.read(new File("res/menus/gui/button_right.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(next);

        Entity goalHead = new TextBody("goalHead", IdGenerator.generateId(),
                1515, 420, 320, 50, fontBig, Bit8.DARK_GREY, "@50");
        addEntityToScene(goalHead);

        Entity safetyReqDesc = new TextBody("safetyReqDesc", IdGenerator.generateId(),
                1425, 480, 320, 50, fontMed, Bit8.DARK_GREY, "@51");
        addEntityToScene(safetyReqDesc);

        Color safetyReqColor = getSafetyReqColor(getGoal());

        Entity safetyReqContent = new TextBody("safetyReqContent", IdGenerator.generateId(),
                1750, 480, 152, 50, fontMed, safetyReqColor, String.valueOf(getGoal()));
        addEntityToScene(safetyReqContent);

        Entity minActuatorsDesc = new TextBody("minActuatorsDesc", IdGenerator.generateId(),
                1425, 527, 320, 50, fontMed, Bit8.DARK_GREY, "@52"
        );
        addEntityToScene(minActuatorsDesc);

        Entity minActuatorsContent = new TextBody("minActuatorsContent", IdGenerator.generateId(),
                1750, 527, 152, 50, fontMed, Bit8.DARK_GREY, String.valueOf(accGoal));
        addEntityToScene(minActuatorsContent);

        try {
            validate = new GenericButton("validate", IdGenerator.generateId(),
                    1800 - 156, 930, 220, 64, new ValidateAction(),
                    ImageIO.read(new File("res/menus/gui/buttons/play_button.png")));
            addEntityToScene(validate);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tipsEntity = new TextBody("tipsEntityType", IdGenerator.generateId(),
                1518, 610, 250, 50, fontBig, Bit8.DARK_GREY, "");
        addEntityToScene(tipsEntity);

        tipsText = new TextBody("tipsText", IdGenerator.generateId(),
                1425, 682, 464, 120, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(tipsText);

        failTipDesc = new TextBody("failTipDesc", IdGenerator.generateId(),
                1425, 802, 250, 50, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(failTipDesc);

        failTipText = new TextBody("failTipText", IdGenerator.generateId(),
                1750, 802, 152, 50, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(failTipText);

        correctSignalsTipDesc = new TextBody("correctSignalsTipDesc", IdGenerator.generateId(),
                1425, 832, 250, 50, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(correctSignalsTipDesc);

        correctSignalsTipText = new TextBody("correctSignalsTipText", IdGenerator.generateId(),
                1750, 832, 152, 50, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(correctSignalsTipText);

        acceptedOOCTipDesc = new TextBody("acceptedOOCTipDesc", IdGenerator.generateId(),
                1425, 862, 250, 50, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(acceptedOOCTipDesc);

        acceptedOOCTipText = new TextBody("acceptedOOCTipText", IdGenerator.generateId(),
                1750, 862, 152, 50, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(acceptedOOCTipText);
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
            descText.getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(description);
        } catch(IndexOutOfBoundsException ex) {
            // This is okay here.
        }
    }

    public Color getSafetyReqColor(double d) {
        if (d <= 1e-9) {
            return CATASTROPHIC;
        } else if (d <= 1e-7) {
            return HAZARDOUS;
        } else if (d <= 1e-5) {
            return MAJOR;
        } else if (d <= 1e-3) {
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
        getTipsText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(
                entity.getComponent(TooltipComponent.class).getTooltipText());
        getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(entity.getComponent(TooltipComponent.class).getFailureRatio());
        getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setColor(getSafetyReqColor(Double.parseDouble(entity.getComponent(TooltipComponent.class).getFailureRatio())));
        getCorrectSignalsTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(entity.getComponent(TooltipComponent.class).getCorrectInputSignals());
        getAcceptedOOCTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(entity.getComponent(TooltipComponent.class).getAcceptedOOCSignals());
        getFailTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@54");
        getAcceptedOOCTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@55");
        getCorrectSignalsTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@53");
        getTipsEntity().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText(String.valueOf(entity.getComponent(TooltipComponent.class).getType()));
    }

    /**
     * Remove the tool tip
     */
    public void displayEmptyToolTip() {
        getTipsText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
        getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
        getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setColor(Bit8.DARK_GREY);
        getCorrectSignalsTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
        getAcceptedOOCTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
        getFailTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
        getAcceptedOOCTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
        getCorrectSignalsTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
        getTipsEntity().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("");
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

        try {
            Entity scoreBox = new ImageEntity("scorebox", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/hud_element_7.png")),
                    1920/2 - 131*3 - 40, 1080/2 - 62*3 - 20, 131 * 6 + 80, 62 * 6 + 40, Layer.UI);
            addEntityToScene(scoreBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Image img1 = new ImageIcon(new File("res/menus/gui/fireworks1.gif").toURL()).getImage();
            ImageEntity ex1 = new ImageEntity("ex1", IdGenerator.generateId(),
                    img1, -100, -500, 2000, 2000, Layer.GAMELAYER3, false, 0);
            addEntityToScene(ex1);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        createScale(probabilities);

        Entity scoreText = new TextBody("scoreText", IdGenerator.generateId(),
                1920/2 - 400, 1080/2 - 150, 200, 50,
                FontCollection.bit8FontLarge, Bit8.DARK_GREY, "@61"
                );
        addEntityToScene(scoreText);
        Entity scoreContent = new TextBody("scoreContent", IdGenerator.generateId(),
                1920/2 - 200, 1080/2 - 150, 200, 50,
                FontCollection.bit8FontLarge, Bit8.DARK_GREY, String.valueOf(score)
        );
        addEntityToScene(scoreContent);

        GenericButtonFront saveScore = new GenericButtonFront(
                "ScoreSaveButton", IdGenerator.generateId(),
                1920 / 2 - 300, 1080 / 2 + 150, 200, 40,
                "@59", FontCollection.bit8FontLarge, new SaveScoreAction(),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(saveScore);

        GenericButtonFront nextLevel = new GenericButtonFront(
                "nextLevelButton", IdGenerator.generateId(),
                1920 / 2 - 100, 1080 / 2 + 150, 200, 40,
                "@62", FontCollection.bit8FontLarge, new NextLevelAction(),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(nextLevel);

        addEntityToScene(new GenericButtonFront(
                "Back to Menu", IdGenerator.generateId(),
                1920 / 2 + 100, 1080 / 2 + 150, 200, 40,
                "@60", FontCollection.bit8FontLarge, new StartAction(-254),
                Bit8.DARK_GREY, null, null
        ));
    }

    /**
     * Display a level not finished screen including some information about the reason
     *
     * @param probabilities: the probabilities needed to calculate the scale
     */
    public void displayLevelNotFinished(double[] probabilities) {
        tries++;

        try {
            Entity scoreBox = new ImageEntity("scorebox", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/hud_element_7.png")),
                    1920/2 - 131*3 - 40, 1080/2 - 62*3 - 20, 131 * 6 + 80, 62 * 6 + 40, Layer.UI);
            addEntityToScene(scoreBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        createScale(probabilities);
        GenericButtonFront back = new GenericButtonFront(
                "back", IdGenerator.generateId(),
                1920 / 2 - 150, 1080 / 2 + 150, 300, 40,
                "@58", FontCollection.bit8FontLarge, new StartAction(null),
                Bit8.DARK_GREY, null, null
        );
        addEntityToScene(back);
    }

    /**
     * Create a scale representing proximity of the user solution to the target failure probability
     *
     * @param probabilities: Passive and OOC failure probabilities for the users' solution
     */
    private void createScale(double[] probabilities) {
        int scaleWidth = 128*4;
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

        Entity scale = null;
        try {
            scale = new ImageEntity("scale", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/scale.png")),
                    1920/2 - scaleWidth/2, 1080/2 - 16*2+ 50, scaleWidth, 16*4, Layer.UI);
            addEntityToScene(scale);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //  create marker for goal, current ooc and passive fails
        try {
            Entity goalMarker = new ImageEntity("goalMarker", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/marker.png")),
                    1920/2 - scaleWidth/2 + positionGoal - 32*2, 1080/2 - 16*2 - 8*4 + 50, 32*4, 32*4, Layer.UI);
            addEntityToScene(goalMarker);

            Entity passiveMarker = new ImageEntity("passiveMarker", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/passiveMarker.png")),
                    1920/2 - scaleWidth/2 + positionScorePassive - 32*2, 1080/2 - 16*2 - 8*4+ 50, 32*4, 32*4, Layer.UI);
            addEntityToScene(passiveMarker);

            Entity oocMarker = new ImageEntity("oocMarker", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/oocMarker.png")),
                    1920/2 - scaleWidth/2 + positionScoreOOC - 32*2, 1080/2 - 16*2 - 8*4+ 50, 32*4, 32*4, Layer.UI);
            addEntityToScene(oocMarker);

            Entity goalMarkerDesc = new ImageEntity("goalMarkerDesc",IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/targetMarker2.png")),
                    1920/2, 1080/2 - 180, 32, 32, Layer.UI
            );
            addEntityToScene(goalMarkerDesc);

            Entity passiveMarkerDesc = new ImageEntity("passiveMarkerDesc",IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/passiveMarker2.png")),
                    1920/2 , 1080/2 - 130, 32, 32, Layer.UI
            );
            addEntityToScene(passiveMarkerDesc);

            Entity oocMarkerDesc = new ImageEntity("oocMarkerDesc",IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/oocMarker2.png")),
                    1920/2, 1080/2 - 80, 32, 32, Layer.UI
            );
            addEntityToScene(oocMarkerDesc);

            TextBody goalMarkerText = new TextBody("goalMarkerText", IdGenerator.generateId(),
                    1920/2 + 35, 1080/2 - 180, 500, 50,
                    FontCollection.bit8FontLarge, Bit8.DARK_GREY, "@63");
            addEntityToScene(goalMarkerText);

            TextBody passiveMarkerText = new TextBody("passiveMarkerText", IdGenerator.generateId(),
                    1920/2 + 35, 1080/2 - 130, 500, 50,
                    FontCollection.bit8FontLarge, Bit8.DARK_GREY, "@64");
            addEntityToScene(passiveMarkerText);

            TextBody oocMarkerText = new TextBody("oocMarkerText", IdGenerator.generateId(),
                    1920/2 + 35, 1080/2 - 80, 500, 50,
                    FontCollection.bit8FontLarge, Bit8.DARK_GREY, "@65");
            addEntityToScene(oocMarkerText);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
