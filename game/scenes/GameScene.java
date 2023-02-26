package game.scenes;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ActionComponent;
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
import engine.resource.colorpalettes.ColorPalette;
import engine.resource.fonts.FontCollection;
import game.action.BuildPanelChange;
import game.action.BuildPanelChangeAction;
import game.action.SaveScoreAction;
import game.action.ValidateAction;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.components.TooltipComponent;
import game.entities.simulation.BuildPanelEntity;
import game.entities.simulation.GridEntity;
import game.entities.simulation.SimulationEntity;
import game.entities.ui.*;
import game.handler.simulation.SimulationType;
import game.handler.simulation.markov.MarkovProcessor;
import game.scenes.helpers.BuildPanelPage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends Scene {
    private final int ITEM_MARGIN = 80;
    private final int BUTTON_ITEM_MARGIN = 20;
    private final int ITEM_WIDTH = 230;
    private final int ITEM_HEIGHT = 60;
    private int X_MARGIN = 200;
    private int Y_MARGIN = 300;
    private final Color TEXT_COLOR = Bit8.DARK_GREY;
    private boolean unlocked = false;
    private int nextNewPage = -1;
    private int currentPageShown = 0;

    // Category colors
    private final Color NSE = Bit8.CORNFLOWER_BLUE;
    private final Color MINOR = Bit8.DARK_PASTEL_GREEN;
    private final Color MAJOR = Bit8.YELLOW;
    private final Color HAZARDOUS = Bit8.ORANGE;
    private final Color CATASTROPHIC = Bit8.RED;

    private final Difficulty difficulty;

    private final ArrayList<Integer> unlocksNeeded = new ArrayList<>();

    private final int DESIGN_CELL_SIZE = 128;
    private int CELL_SIZE = DESIGN_CELL_SIZE;
    private String description;
    private ArrayList<String> descriptions;
    private double goal = 10e-4;
    private int numberOfBuildPanelElements = 0;
    private Entity currentlyBuilding = null;
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
    private Entity aircraft;
    private int currentlyDisplayedDescriptionPart = 0;

    private boolean levelPassed = false;

    private int xMax;
    private int yMax;

    private int accGoal;
    private int sensGoal;
    private int cGoal;

    private ArrayList<BuildPanelPage> pages = new ArrayList<>();

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

        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/cablesBackground.png")), 0, 0, 1920, 1080, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }
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

    private void updateEntitySize() {
        for(Entity e : getEntities()) {
            if (e.getComponent(RenderComponent.class) == null) {
                continue;
            }

            if (e.getComponent(GridComponent.class) == null) {
                continue;
            }
            int x = (int) e.getComponent(GridComponent.class).getGridLocation().getX();
            int y = (int) e.getComponent(GridComponent.class).getGridLocation().getY();
            Rectangle bounds = new Rectangle(X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
            for (RenderObject r : e.getComponent(RenderComponent.class).getRenderObjects()) {
                r.setLocation(bounds.getLocation());
                r.setBounds(bounds);
            }
            for (CollisionObject c : e.getComponent(ColliderComponent.class).getCollisionObjects()) {
                c.setCollisionBoundaries(bounds);
            }
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
        // create new page
        if (numberOfBuildPanelElements % 6 == 0) {
            numberOfBuildPanelElements = 0;
            nextNewPage++;
            pages.add(new BuildPanelPage(nextNewPage));
        }

        BuildPanelEntity buildPanelEntity = new BuildPanelEntity("build_element_" + imgId, IdGenerator.generateId(),
                160 + numberOfBuildPanelElements * (DESIGN_CELL_SIZE + ITEM_MARGIN), 839, DESIGN_CELL_SIZE, DESIGN_CELL_SIZE,
                Game.res().loadTile(imgId), imgId,
                amount, failureRatio,
                Game.res().getTileSet().getType(imgId),
                correctSignalsNeeded, outOfControlSignalsAccepted,
                Game.res().loadDescription(imgId), failureDetectionRatio
        );
        buildPanelEntity.getComponent(BuildComponent.class).setPortId(imgId - 500);
        addEntityToScene(buildPanelEntity);
        pages.get(nextNewPage).addToPage(buildPanelEntity);
        updateBuildPanel();
        numberOfBuildPanelElements++;
    }

    /**
     * setup method for buttons in the GameScene
     */
    private void setupButtons() {
        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f);
        GenericButton exitButton = null;
        try {
            exitButton = new GenericButton(
                    "Exit",
                    IdGenerator.generateId(),
                    20, 40,
                    64, 64, new ExitAction(),
                    ImageIO.read(new File("game/res/menus/gui/buttons/exit_button.png")));
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
                    ImageIO.read(new File("game/res/menus/gui/buttons/menu_icon.png"))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.addEntityToScene(mainMenuButton);
    }

    /**
     * Update the grid size / cell size
     */
    public void updateGridSize() {
        if(xMax > yMax && (xMax > 7 || yMax > 5)) {
            CELL_SIZE = DESIGN_CELL_SIZE * 7 / xMax;
        } else if (xMax > 7 || yMax > 5) {
            CELL_SIZE = DESIGN_CELL_SIZE * 5 / yMax;
        } else {
            CELL_SIZE = DESIGN_CELL_SIZE;
        }

        X_MARGIN = (1500 - xMax * CELL_SIZE) / 2;
        Y_MARGIN = (850 - yMax * CELL_SIZE) / 2;
    }

    /**
     * Set the next build panel page
     */
    public void nextBuildPanelPage() {
        int maxPages = pages.size() - 1;
        currentPageShown += 1;
        if (currentPageShown > maxPages) {
            currentPageShown = 0;
        }
        updateBuildPanel();
    }

    /**
     * Set the previous build panel page
     */
    public void prevBuildPanelPage() {
        currentPageShown -= 1;
        if (currentPageShown < 0) {
            currentPageShown = pages.size() - 1;
        }
        updateBuildPanel();
    }

    /**
     * Update the build panel entities (hide graphics and deactivate colliders for pages that are not shown)
     */
    public void updateBuildPanel() {
        for (BuildPanelPage page : pages) {
            for (Entity e : page.getEntitiesInPage()) {
                for (RenderObject ro : e.getComponent(RenderComponent.class).getRenderObjects()) {
                    if (page.getPageId() == currentPageShown) {
                        ro.setHidden(false);
                    } else {
                        ro.setHidden(true);
                    }
                }

                for (CollisionObject co : e.getComponent(ColliderComponent.class).getCollisionObjects()) {
                    if (page.getPageId() == currentPageShown) {
                        co.setDeactivated(false);
                    } else {
                        co.setDeactivated(true);
                    }
                }
            }
        }
    }

    /**
     * Setup method for the build panel
     */
    private void setupBuildPanel() {
        try {
            ImageEntity buildPanel = new ImageEntity("Build Panel", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/menus/blueprint8bit.png")), 0, 800, 1500, 1080 - 850, Layer.UI);
            addEntityToScene(buildPanel);

            GenericButton leftButton = new GenericButton("left_build_panel", IdGenerator.generateId(),
                    85, 890, 40, 40,
                    new BuildPanelChangeAction(BuildPanelChange.LEFT),
                    ImageIO.read(new File("game/res/menus/gui/button_left.png")));

            addEntityToScene(leftButton);

            GenericButton rightButton = new GenericButton("right_build_panel", IdGenerator.generateId(),
                    1355, 890, 40, 40,
                    new BuildPanelChangeAction(BuildPanelChange.RIGHT),
                    ImageIO.read(new File("game/res/menus/gui/button_right.png")));
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
                    ImageIO.read(new File("game/res/menus/gui/hud_element_1.png")),
                    1500, 0, 402, 350, Layer.UI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(desc);

        Entity goal = null;
        try {
            goal = new ImageEntity("goal", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/menus/gui/hud_element_2.png")),
                    1500, 375, 402, 200, Layer.UI);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(goal);

        Entity tips = null;
        try {
            tips = new ImageEntity("tips", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/menus/gui/hud_element_3.png")),
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
                    ImageIO.read(new File("game/res/menus/gui/button_left_white.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(previous);

        try {
            next = new GenericButton("next", IdGenerator.generateId(),
                    1840, 280, 40, 40, new StartAction(null),
                    ImageIO.read(new File("game/res/menus/gui/button_right_white.png")));
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
                1500 + (1920 - 1500) / 2 - 300 / 2, 535, 300, 50, "@30", FontCollection.bit8FontHuge, new ValidateAction(), Bit8.CHROME,null, null);
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

    public int getSensGoal() {
        return sensGoal;
    }

    public int getcGoal() {
        return cGoal;
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

    public void displayLevelFinished(int score, double[] probabilities) {
        this.score = (int) (score * Math.pow(0.98f, tries));
        tries++;
        addEntityToScene(new ScoreBox("Scorebox", IdGenerator.generateId(),
                Game.res().loadFont("game/res/font/joystix monospace.ttf", 25f), score,
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

        try {
            aircraft = new ImageEntity("aircraft", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/aircraft.png")), 1920 / 2, 1080 / 2, 200, 50, Layer.GAMELAYER3);
            addEntityToScene(aircraft);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                Game.res().loadFont("game/res/font/joystix monospace.ttf", 25f), 0,
                1920 / 2 - 300, 1080 / 2 - 250, 600, 500, "Requirements not met!"));
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

        createScale(probabilities);
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

    /**
     * play an animation while the system is calculating
     */
    public void playAircraftAnimation() {
        if (aircraft == null) {
            return;
        }
        Rectangle prevBounds = (Rectangle) aircraft.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds();
        int newX;
        if (prevBounds.x + 1 > 1920) {
            newX = 0;
        } else {
            newX = prevBounds.x + 1;
        }
        aircraft.getComponent(RenderComponent.class).reposition(new Point(newX, prevBounds.y));
    }

    /**
     * set the objects for the aircraft flying animation
     */
    public void setAircraftAnimation() {
        try {
            aircraft = new ImageEntity("Aircraft", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/aircraft.png")), 0, 1080 / 2, 500, 200, Layer.GAMELAYER3);
            addEntityToScene(aircraft);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the aircraft animation
     */
    public void removeAircraftAnimation() {
        removeEntityFromScene(aircraft);
        aircraft = null;
    }
}
