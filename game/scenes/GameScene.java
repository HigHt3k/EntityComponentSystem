package game.scenes;

import com.Game;
import com.IdGenerator;
import com.ecs.component.collision.CollisionComponent;
import com.ecs.entity.Entity;
import com.ecs.component.graphics.GraphicsComponent;
import com.ecs.entity.GenericButton;
import com.ecs.intent.ExitIntent;
import com.graphics.scene.Scene;
import com.resource.colorpalettes.Bit8;
import com.resource.fonts.FontCollection;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.entities.*;
import game.handler.simulation.SimulationType;
import game.handler.BuildHandler;
import game.handler.SimulationSystem;
import game.intent.CableLayerSwitchIntent;
import game.intent.StartIntent;

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

    // Category colors
    private final Color NSE = Bit8.CORNFLOWER_BLUE;
    private final Color MINOR = Bit8.DARK_PASTEL_GREEN;
    private final Color MAJOR = Bit8.YELLOW;
    private final Color HAZARDOUS = Bit8.ORANGE;
    private final Color CATASTROPHIC = Bit8.RED;


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
    private int currentlyDisplayedDescriptionPart = 0;

    private boolean levelPassed = false;

    private int xMax;
    private int yMax;

    private int accGoal;
    private int sensGoal;
    private int cGoal;

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
    public GameScene(String name, int id) {
        super(name, id);
        setupBuildPanel();
    }

    /**
     * initialize the game scene; sets up the description panel and buttons
     */
    public void init() {
        // Create the GUI including buttons going back to menu, exit etc.
        Game.input().removeAllHandlers();
        Game.system().resetSystems();
        setupDescriptionPanel();
        setupButtons();
        addToBuildPanel(500, 1000, 1e-25f, 1, 0);
        addToBuildPanel(501, 1000, 1e-25f, 1, 0);
        addToBuildPanel(502, 1000, 1e-25f, 1, 0);
        addToBuildPanel(503, 1000, 1e-25f, 1, 0);
        Game.input().addHandler(new BuildHandler());
        Game.system().addSystem(new SimulationSystem());
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
     * add an Entity that snaps to the grid. This method can e.g. be called from the {@link com.resource.ResourceManager} to parse
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
    public void addSimulationElement(int x, int y, int imgId, float failureRatio, int correctSignalsNeeded, int outOfControlSignalsAccepted, boolean removable) {
        if(Game.res().getTileSet().getType(imgId) == SimulationType.CPU) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId),imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    correctSignalsNeeded, outOfControlSignalsAccepted,
                    new int[] {0,1,2,3}, new int[] {0,1,2,3}, removable
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
                    new int[] {}, new int[] {0,1,2,3}, removable
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
                    new int[] {0,1,2,3}, new int[] {}, removable
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
                    new int[] {imgId - 500}, new int[] {imgId - 500}, removable
            );
            addEntityToScene(simulationEntity);
        }
    }

    /**
     * add an Entity to the buildpanel
     * @param imgId: the entity/img id
     * @param amount: how many times can this be built?
     * @param failureRatio: failure ratio of the entity
     */
    public void addToBuildPanel(int imgId, int amount, float failureRatio, int correctSignalsNeeded, int outOfControlSignalsAccepted) {
        BuildPanelEntity buildPanelEntity = new BuildPanelEntity("build_element_" + imgId, IdGenerator.generateId(),
                150 + numberOfBuildPanelElements * (DESIGN_CELL_SIZE + ITEM_MARGIN), 875, DESIGN_CELL_SIZE, DESIGN_CELL_SIZE,
                Game.res().loadTile(imgId), imgId,
                amount, failureRatio,
                Game.res().getTileSet().getType(imgId),
                correctSignalsNeeded, outOfControlSignalsAccepted,
                Game.res().loadDescription(imgId)
        );
        buildPanelEntity.getComponent(BuildComponent.class).setPortId(imgId-500);
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
                font
        );
        exitButton.addIntent(new ExitIntent());
        this.addEntityToScene(exitButton);

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 850 + ITEM_MARGIN,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                font
        );

        mainMenuButton.addIntent(new StartIntent());
        this.addEntityToScene(mainMenuButton);

        // cable switch buttons
        GenericButton layer0 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                50, 50, 50, 50,
                "0",
                font
        );
        layer0.addIntent(new CableLayerSwitchIntent(0));
        this.addEntityToScene(layer0);

        GenericButton layer1 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                120, 50, 50, 50,
                "1",
                font
        );
        layer1.addIntent(new CableLayerSwitchIntent(1));
        this.addEntityToScene(layer1);

        GenericButton layer2 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                190, 50, 50, 50,
                "2",
                font
        );
        layer2.addIntent(new CableLayerSwitchIntent(2));
        this.addEntityToScene(layer2);

        GenericButton layer3 = new GenericButton(
                "Layer1_button", IdGenerator.generateId(),
                260, 50, 50, 50,
                "3",
                font
        );
        layer3.addIntent(new CableLayerSwitchIntent(3));
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
        Entity buildPanel = new Entity("Build Panel", IdGenerator.generateId());

        GraphicsComponent buildPanelGC = new GraphicsComponent();
        Rectangle buildPanelBounds = new Rectangle(0, 850, 1500, (1080-850));
        buildPanelGC.setBounds(buildPanelBounds);
        try {
            buildPanelGC.setImage(ImageIO.read(new File("game/res/menus/blueprint_scaled.png")));
        } catch (IOException e) {
            Game.logger().severe("Could not load image from file\n" + e.getMessage());
        }
        buildPanel.addComponent(buildPanelGC);
        buildPanelGC.setEntity(buildPanel);

        addEntityToScene(buildPanel);
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

        descText = new TextBody("descText", IdGenerator.generateId(),
                1500, 50, 402, 300, fontMed, Bit8.DARK_GREY, description);
        addEntityToScene(descText);

        Entity descHead = new TextBody("descHead", IdGenerator.generateId(),
                1500, 0, 402, 50, fontBig, Bit8.DARK_GREY, "@49");
        addEntityToScene(descHead);

        previous = new GenericButton("previous", IdGenerator.generateId(),
                1520, 300, 40, 40, "<-", fontMed);
        addEntityToScene(previous);

        next = new GenericButton("next", IdGenerator.generateId(),
                1860, 300, 40, 40, "<-", fontMed);
        addEntityToScene(next);

        Entity goalHead = new TextBody("goalHead", IdGenerator.generateId(),
                1500, 350, 402, 50, fontBig, Bit8.DARK_GREY, "@50");
        addEntityToScene(goalHead);

        Entity safetyReqDesc = new TextBody("safetyReqDesc", IdGenerator.generateId(),
                1500, 400, 250, 30, fontMed, Bit8.DARK_GREY, "@51");
        addEntityToScene(safetyReqDesc);

        Color safetyReqColor = getSafetyReqColor(getGoal());

        Entity goalContent = new TextBody("goalContent", IdGenerator.generateId(),
                1750, 400, 152, 30, fontMed, safetyReqColor, String.valueOf(getGoal()));
        addEntityToScene(goalContent);
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

    /**
     * setup method for the description panel (right side)
     */
    private void setupDescriptionPanelOld() {
        Entity descriptionPanel = new Entity("Description Panel", IdGenerator.generateId());

        GraphicsComponent descriptionPanelGC = new GraphicsComponent();
        //TODO: investigate why the numbers are not 420 and 1080 to correctly show this panel on Surface Pro 7
        Rectangle descriptionPanelBounds = new Rectangle(1500, 0, 402, 1037);
        descriptionPanelGC.setBounds(descriptionPanelBounds);

        try {
            descriptionPanelGC.setImage(ImageIO.read(new File("game/res/menus/box_new.png")));
        } catch (IOException e) {
            Game.logger().severe("Could not load image from file\n" + e.getMessage());
        }

        descriptionPanelGC.setTextColor(TEXT_COLOR);
        descriptionPanelGC.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));

        descriptionPanelGC.addText(description);
        descriptionPanelGC.addLocation(new Point(descriptionPanelBounds.getLocation().x,
                descriptionPanelBounds.getLocation().y + 50));
        descriptionPanelGC.addText("Target Failure Probability: \n<=" + goal);
        descriptionPanelGC.addLocation(new Point(descriptionPanelBounds.getLocation().x, descriptionPanelBounds.getLocation().y + 450));

        descriptionPanel.addComponent(descriptionPanelGC);
        descriptionPanelGC.setEntity(descriptionPanel);

        addEntityToScene(descriptionPanel);

        Entity failureProbabilityPanel = new Entity("failureProbabilityDisplay", IdGenerator.generateId());

        Rectangle failurePanelBounds = new Rectangle(1400 + (402/8), 750, 402/2, 402/2);
        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setTextColor(TEXT_COLOR);
        graphics.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        graphics.setBounds(failurePanelBounds);
        graphics.addText("Current calculated\nmaximum failure\nprobability:\n 0.0000");
        graphics.addLocation(new Point(1500, 650));
        graphics.setEntity(failureProbabilityPanel);
        failureProbabilityPanel.addComponent(graphics);

        addEntityToScene(failureProbabilityPanel);

        Rectangle goalImageBounds = new Rectangle(1400 + 402/8, 800, 160, 40);
        Entity goal = new Entity("Goal", IdGenerator.generateId());
        GraphicsComponent goalGraphics = new GraphicsComponent();
        goalGraphics.setBounds(goalImageBounds);
        goalGraphics.setShape(goalImageBounds);
        goalGraphics.setThickness(10);
        if(getGoal() <= 1e-9) {
            goalGraphics.setTextColor(CATASTROPHIC);
            goalGraphics.setBorderColor(CATASTROPHIC);
            goalGraphics.addText("VERY STRONG");
            goalGraphics.addLocation(goalImageBounds.getLocation());
        }
        else if(getGoal() <= 1e-7){
            goalGraphics.setTextColor(HAZARDOUS);
            goalGraphics.setBorderColor(HAZARDOUS);
            goalGraphics.addText("STRONG");
            goalGraphics.addLocation(goalImageBounds.getLocation());
        }
        else if(getGoal() <= 1e-5){
            goalGraphics.setTextColor(MAJOR);
            goalGraphics.setBorderColor(MAJOR);
            goalGraphics.addText("MEDIUM");
            goalGraphics.addLocation(goalImageBounds.getLocation());
        }
        else if(getGoal() <= 1e-3) {
            goalGraphics.setTextColor(MINOR);
            goalGraphics.setBorderColor(MINOR);
            goalGraphics.addText("LOW");
            goalGraphics.addLocation(goalImageBounds.getLocation());
        }
        else {
            goalGraphics.setTextColor(NSE);
            goalGraphics.setBorderColor(NSE);
            goalGraphics.addText("VERY LOW");
            goalGraphics.addLocation(goalImageBounds.getLocation());
        }
        goalGraphics.setEntity(goal);
        goal.addComponent(goalGraphics);

        addEntityToScene(goal);

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
}
