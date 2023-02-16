package game.scenes;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ExitAction;
import engine.ecs.component.action.StartAction;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.graphics.scene.Scene;
import engine.resource.ResourceManager;
import engine.resource.colorpalettes.Bit8;
import engine.resource.fonts.FontCollection;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.components.TooltipComponent;
import game.entities.*;
import game.handler.simulation.SimulationType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends Scene {
    private final int ITEM_MARGIN = 20;
    private final int ITEM_WIDTH = 230;
    private final int ITEM_HEIGHT = 60;
    private int X_MARGIN = 200;
    private int Y_MARGIN = 300;
    private final Color TEXT_COLOR = Bit8.DARK_GREY;
    private boolean unlocked = false;

    // Category colors
    private final Color NSE = Bit8.CORNFLOWER_BLUE;
    private final Color MINOR = Bit8.DARK_PASTEL_GREEN;
    private final Color MAJOR = Bit8.YELLOW;
    private final Color HAZARDOUS = Bit8.ORANGE;
    private final Color CATASTROPHIC = Bit8.RED;

    private Difficulty difficulty;

    private ArrayList<Integer> unlocksNeeded = new ArrayList<>();

    private final int DESIGN_CELL_SIZE = 128;
    private int CELL_SIZE = DESIGN_CELL_SIZE;
    private String description;
    private ArrayList<String> descriptions;
    private double goal = 10e-4;
    private int numberOfBuildPanelElements = 0;
    private Entity currentlyBuilding = null;

    private Entity previous;
    private Entity next;
    private Entity descText;
    private Entity validate;
    private Entity tipsText;
    private Entity failTipText;
    private Entity correctSignalsTipText;
    private Entity acceptedOOCTipText;
    private Entity failTipDesc;
    private Entity acceptedOOCTipDesc;
    private Entity correctSignalsTipDesc;
    private Entity aircraft;
    private int currentlyDisplayedDescriptionPart = 0;

    private boolean levelPassed = false;

    private int xMax;
    private int yMax;

    private int accGoal;
    private int sensGoal;
    private int cGoal;

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public Entity getTipsText() {
        return tipsText;
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

    public void setGridSize(int x, int y) {
        //CELL_SIZE = 750/y;
        xMax = x;
        yMax = y;
        updateGridSize();
        updateEntitySize();
    }

    /**
     * get the currently built entity
     * @return: the entity
     */
    public Entity getCurrentlyBuilding() {
        return currentlyBuilding;
    }

    /**
     * set the currently built entity
     * @param entity: the entity
     */
    public void setCurrentlyBuilding(Entity entity) {
        currentlyBuilding = entity;
    }

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
        init();
    }

    /**
     * initialize the game scene; sets up the description panel and buttons
     */
    public void init() {
        // Create the GUI including buttons going back to menu, exit etc.
        setupDescriptionPanel();
        setupButtons();
        addToBuildPanel(500, 1000, 1e-25f, 1, 0, 0f);
        addToBuildPanel(501, 1000, 1e-25f, 1, 0, 0f);
        addToBuildPanel(502, 1000, 1e-25f, 1, 0, 0f);
        addToBuildPanel(503, 1000, 1e-25f, 1, 0, 0f);
    }

    private void updateEntitySize() {
        for(Entity e : getEntities()) {
            if(e.getComponent(GraphicsComponent.class) == null) {
                continue;
            }
            if(e.getComponent(GridComponent.class) == null) {
                continue;
            }
            int x = (int) e.getComponent(GridComponent.class).getGridLocation().getX();
            int y = (int) e.getComponent(GridComponent.class).getGridLocation().getY();
            Rectangle bounds = new Rectangle(X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
            e.getComponent(GraphicsComponent.class).setBounds(bounds);
            e.getComponent(CollisionComponent.class).setCollisionBox(bounds);
        }
    }

    /**
     * set the description of the game scene
     * @param description: the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * set the goal of the level
     * @param goal: target failure rate
     */
    public void setGoal(double goal) {
        this.goal = goal;
    }

    /**
     * @return the description as defined in the level.xml data
     */
    public String getDescription() {
        return description;
    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }



    /**
     * add an Entity that snaps to the grid. This method can e.g. be called from the {@link ResourceManager} to parse
     * the level.XML file to the actual {@link GameScene}.
     * A {@link GridComponent} is added to the Entity to store the grid position data, which can be called to validate entities against
     * each other without having to calculate their grid position based on the actual screen position.
     * @param x: x position in the grid
     * @param y: y position in the grid
     */
    public void addGridElement(int x, int y) {
        GridEntity gridEntity = new GridEntity("grid_element_" + x + ":" + y, IdGenerator.generateId(),
                X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                x, y,
                Game.res().loadTile(1)
        );
        addEntityToScene(gridEntity);
    }

    /**
     * adds a SimulationEntity to the scene
     * @param x: grid position at x
     * @param y: grid position at y
     * @param imgId: entity / image id
     * @param failureRatio: failure ratio of the entity
     * @param removable: can be removed or not
     */
    public void addSimulationElement(int x, int y, int imgId, float failureRatio,
                                     int correctSignalsNeeded, int outOfControlSignalsAccepted, boolean removable, float failureDetectionRatio) {
        if(Game.res().getTileSet().getType(imgId) == SimulationType.CPU) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId),imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    correctSignalsNeeded, outOfControlSignalsAccepted,
                    new int[] {0,1,2,3}, new int[] {0,1,2,3}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if(Game.res().getTileSet().getType(imgId) == SimulationType.SENSOR) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId),imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    correctSignalsNeeded, outOfControlSignalsAccepted,
                    new int[] {}, new int[] {0,1,2,3}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if(Game.res().getTileSet().getType(imgId) == SimulationType.ACTUATOR) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId),imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    correctSignalsNeeded, outOfControlSignalsAccepted,
                    new int[] {0,1,2,3}, new int[] {}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if(Game.res().getTileSet().getType(imgId) == SimulationType.CABLE) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId),imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    0, 0,
                    new int[] {imgId - 500}, new int[] {imgId - 500}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        }
    }

    /**
     * add an Entity to the buildpanel
     *
     * @param imgId:        the entity/img id
     * @param amount:       how many times can this be built?
     * @param failureRatio: failure ratio of the entity
     */
    public void addToBuildPanel(int imgId, int amount, float failureRatio, int correctSignalsNeeded,
                                int outOfControlSignalsAccepted, float failureDetectionRatio) {
        BuildPanelEntity buildPanelEntity = new BuildPanelEntity("build_element_" + imgId, IdGenerator.generateId(),
                150 + numberOfBuildPanelElements * (DESIGN_CELL_SIZE + ITEM_MARGIN), 875, DESIGN_CELL_SIZE, DESIGN_CELL_SIZE,
                Game.res().loadTile(imgId), imgId,
                amount, failureRatio,
                Game.res().getTileSet().getType(imgId),
                correctSignalsNeeded, outOfControlSignalsAccepted,
                Game.res().loadDescription(imgId), failureDetectionRatio
        );
        buildPanelEntity.getComponent(BuildComponent.class).setPortId(imgId - 500);
        addEntityToScene(buildPanelEntity);
        numberOfBuildPanelElements++;
    }

    /**
     * setup method for buttons in the GameScene
     */
    private void setupButtons() {
        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f);
        GenericButton exitButton = new GenericButton(
                "Exit",
                IdGenerator.generateId(),
                1600, 850 + ITEM_HEIGHT + ITEM_MARGIN * 2,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3",
                font, new ExitAction()
        );
        this.addEntityToScene(exitButton);

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 850 + ITEM_MARGIN,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                font, new StartAction(Game.scene().getScene(-255))
        );
        this.addEntityToScene(mainMenuButton);

        // cable switch buttons
        GenericButton layer0 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                50, 50, 50, 50,
                "0",
                font, new StartAction(null)
        );
        this.addEntityToScene(layer0);

        GenericButton layer1 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                120, 50, 50, 50,
                "1",
                font, new StartAction(null)
        );
        this.addEntityToScene(layer1);

        GenericButton layer2 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                190, 50, 50, 50,
                "2",
                font, new StartAction(null)
        );
        this.addEntityToScene(layer2);

        GenericButton layer3 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                260, 50, 50, 50,
                "3",
                font, new StartAction(null)
        );
        this.addEntityToScene(layer3);
    }

    public void updateGridSize() {
        if(xMax > yMax && (xMax > 7 || yMax > 5)) {
            CELL_SIZE = DESIGN_CELL_SIZE * 7 / xMax;
        } else if(xMax > 7 || yMax > 5) {
            CELL_SIZE = DESIGN_CELL_SIZE * 5 / yMax;
        } else {
            CELL_SIZE = DESIGN_CELL_SIZE;
        }

        X_MARGIN = (1500 - xMax*CELL_SIZE)/2;
        Y_MARGIN = (850 - yMax*CELL_SIZE)/2;
    }

    /**
     * Setup method for the build panel
     */
    private void setupBuildPanel() {
        try {
            ImageEntity buildPanel = new ImageEntity("Build Panel", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/menus/blueprint_scaled.png")), 0, 850, 1500, 1080 - 850, Layer.UI);
            addEntityToScene(buildPanel);
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

        Entity desc = new SimplePanel("desc", IdGenerator.generateId(),
                1500, 0, 402, 350, Bit8.CHROME, Bit8.JAM, Bit8.DARK_GREY);
        addEntityToScene(desc);

        Entity goal = new SimplePanel("goal", IdGenerator.generateId(),
                1500, 350, 402, 200, Bit8.CHROME, Bit8.JAM, Bit8.DARK_GREY);
        addEntityToScene(goal);

        Entity tips = new SimplePanel("tips", IdGenerator.generateId(),
                1500, 550, 402, 300, Bit8.CHROME, Bit8.JAM, Bit8.DARK_GREY);
        addEntityToScene(tips);

        tipsText = new TextBody("tipsText", IdGenerator.generateId(),
                1500, 550, 402, 180, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(tipsText);

        failTipDesc = new TextBody("failTipDesc", IdGenerator.generateId(),
                1500, 730, 250, 15, fontMed,Bit8.DARK_GREY, "");
        addEntityToScene(failTipDesc);

        failTipText = new TextBody("failTipText", IdGenerator.generateId(),
                1750, 730, 152, 15,fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(failTipText);

        correctSignalsTipDesc = new TextBody("correctSignalsTipDesc", IdGenerator.generateId(),
                1500, 760, 250, 15, fontMed,Bit8.DARK_GREY, "");
        addEntityToScene(correctSignalsTipDesc);

        correctSignalsTipText = new TextBody("correctSignalsTipText", IdGenerator.generateId(),
                1750, 760, 152, 15, fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(correctSignalsTipText);

        acceptedOOCTipDesc = new TextBody("acceptedOOCTipDesc", IdGenerator.generateId(),
                1500, 790, 250, 15, fontMed,Bit8.DARK_GREY, "");
        addEntityToScene(acceptedOOCTipDesc);

        acceptedOOCTipText = new TextBody("acceptedOOCTipText", IdGenerator.generateId(),
                1750, 790, 152, 15,fontMed, Bit8.DARK_GREY, "");
        addEntityToScene(acceptedOOCTipText);

        descText = new TextBody("descText", IdGenerator.generateId(),
                1500, 50, 402, 300, fontMed, Bit8.DARK_GREY, description);
        addEntityToScene(descText);

        Entity descHead = new TextBody("descHead", IdGenerator.generateId(),
                1500, 0, 402, 50, fontBig, Bit8.DARK_GREY, "@49");
        addEntityToScene(descHead);

        previous = new GenericButton("previous", IdGenerator.generateId(),
                1520, 300, 40, 40, "<-", fontMed, new StartAction(null));
        addEntityToScene(previous);

        next = new GenericButton("next", IdGenerator.generateId(),
                1840, 300, 40, 40, "->", fontMed, new StartAction(null));
        addEntityToScene(next);

        Entity goalHead = new TextBody("goalHead", IdGenerator.generateId(),
                1500, 350, 402, 50, fontBig, Bit8.DARK_GREY, "@50");
        addEntityToScene(goalHead);

        Entity safetyReqDesc = new TextBody("safetyReqDesc", IdGenerator.generateId(),
                1500, 430, 250, 15, fontMed, Bit8.DARK_GREY, "@51");
        addEntityToScene(safetyReqDesc);

        Color safetyReqColor = getSafetyReqColor(getGoal());

        Entity safetyReqContent = new TextBody("safetyReqContent", IdGenerator.generateId(),
                1750, 430, 152, 15, fontMed, safetyReqColor, String.valueOf(getGoal()));
        addEntityToScene(safetyReqContent);

        Entity minActuatorsDesc = new TextBody("minActuatorsDesc", IdGenerator.generateId(),
                1500, 400, 250, 15, fontMed, Bit8.DARK_GREY, "@52"
                );
        addEntityToScene(minActuatorsDesc);

        Entity minActuatorsContent = new TextBody("minActuatorsContent", IdGenerator.generateId(),
                1750, 400, 152, 15, fontMed, Bit8.DARK_GREY, String.valueOf(accGoal));
        addEntityToScene(minActuatorsContent);

        validate = new GenericButton("validate", IdGenerator.generateId(),
                1500 + (1920 - 1500) / 2 - 300 / 2, 480, 300, 50, "@30", fontBig, new StartAction(null));
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
            descText.getComponent(GraphicsComponent.class).getTexts().set(0, description);
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

    public int getCellSize() {
        return CELL_SIZE;
    }

    public void setGoal(int act, int sens, int c) {
        this.accGoal = act;
        this.sensGoal = sens;
        this.cGoal = c;
    }

    public int getAccGoal() {
        return accGoal;
    }

    public double getGoal() {
        return goal;
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

    public void displayToolTip(Entity entity) {
        ((TextObject) getTipsText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(
                entity.getComponent(TooltipComponent.class).getTooltipText());
        ((TextObject) getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(entity.getComponent(TooltipComponent.class).getFailureRatio());
        ((TextObject) getCorrectSignalsTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(entity.getComponent(TooltipComponent.class).getCorrectInputSignals());
        ((TextObject) getAcceptedOOCTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText(entity.getComponent(TooltipComponent.class).getAcceptedOOCSignals());
        ((TextObject) getFailTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("@54");
        ((TextObject) getAcceptedOOCTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("@55");
        ((TextObject) getCorrectSignalsTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("@53");
    }

    public void displayEmptyToolTip() {
        ((TextObject) getTipsText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getFailTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getCorrectSignalsTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getAcceptedOOCTipText().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getFailTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getAcceptedOOCTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
        ((TextObject) getCorrectSignalsTipDesc().getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0)).setText("");
    }

    public void displayLevelFinished(int score) {
        addEntityToScene(new ScoreBox("Scorebox", IdGenerator.generateId(),
                Game.res().loadFont("game/res/font/joystix monospace.ttf", 25f), score,
                1920 / 2 - 200, 1080 / 2 - 100, 400, 200, "level passed!"));
        GenericButton saveScore = new GenericButton(
                "ScoreSaveButton", IdGenerator.generateId(),
                1920 / 2 - 150, 1080 / 2 + 50, 300, 40,
                "BACK TO MENU", Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f), new StartAction(null)
        );
        addEntityToScene(saveScore);

        try {
            aircraft = new ImageEntity("aircraft", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/aircraft.png")), 1920 / 2, 1080 / 2, 200, 50, Layer.GAMELAYER3);
            addEntityToScene(aircraft);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayLevelNotFinished() {
        addEntityToScene(new ScoreBox("scorebox", IdGenerator.generateId(),
                Game.res().loadFont("game/res/font/joystix monospace.ttf", 25f), 0,
                1920 / 2 - 200, 1080 / 2 - 100, 400, 200, "Requirements not met!"));
        GenericButton back = new GenericButton(
                "back", IdGenerator.generateId(),
                1920 / 2 - 150, 1080 / 2 + 50, 300, 40,
                "TRY AGAIN", Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f), new StartAction(null)
        );
        addEntityToScene(back);

        try {
            aircraft = new ImageEntity("aircraft", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/aircraft-crash.png")), 1920 / 2, 1080 / 2, 500, 200, Layer.GAMELAYER3);
            addEntityToScene(aircraft);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAircraftAnimation() {
        Rectangle prevBounds = aircraft.getComponent(GraphicsComponent.class).get_BOUNDS();
        int newX;
        if (prevBounds.x + 1 > 1920) {
            newX = 0;
        } else {
            newX = prevBounds.x + 1;
        }
        Rectangle newBounds = new Rectangle(newX, prevBounds.y, prevBounds.width, prevBounds.height);
        aircraft.getComponent(GraphicsComponent.class).setBounds(newBounds);
    }

    public void setAircraftAnimation() {
        try {
            aircraft = new ImageEntity("Aircraft", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/aircraft.png")), 0, 1080 / 2, 500, 200, Layer.GAMELAYER3);
            addEntityToScene(aircraft);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeAircraftAnimation() {
        removeEntityFromScene(aircraft);
        aircraft = null;
    }
}
