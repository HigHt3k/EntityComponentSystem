package game.scenes;

import com.Game;
import com.IdGenerator;
import com.ecs.entity.Entity;
import com.ecs.component.GraphicsComponent;
import com.ecs.entity.GenericButton;
import com.ecs.intent.ExitIntent;
import com.graphics.scene.Scene;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.entities.BuildPanelEntity;
import game.entities.CableEntity;
import game.entities.GridEntity;
import game.entities.SimulationEntity;
import game.handler.BuildHandler;
import game.handler.SimulationSystem;
import game.intent.StartIntent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameScene extends Scene {
    private final int ITEM_MARGIN = 20;
    private final int ITEM_WIDTH = 300;
    private final int ITEM_HEIGHT = 60;
    private final int X_MARGIN = 200;
    private final int Y_MARGIN = 300;
    private final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private final Color HOVER_COLOR = new Color(40, 40, 40, 150);
    private final int CELL_SIZE = 128;
    private String description;
    private double goal = 10e-4;
    private int numberOfBuildPanelElements = 0;
    private Entity currentlyBuilding = null;

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
        Game.input().addHandler(new BuildHandler());
        Game.system().addSystem(new SimulationSystem());
    }

    /**
     * set the description of the game scene
     * @param description: the description
     */
    public void setDescription(String description) {
        this.description = description;
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

    public Entity getEntityById(int id) {
        for(Entity e : getEntities()) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
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
    public void addSimulationElement(int x, int y, int imgId, float failureRatio, boolean removable) {
        SimulationEntity simulationEntity = new SimulationEntity(
                "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                x, y,
                Game.res().loadTile(imgId),
                failureRatio, Game.res().getTileSet().getType(imgId),
                4, removable
        );

        addEntityToScene(simulationEntity);
        simulationEntity.initCablePorts(this);
    }

    /**
     * add an Entity to the buildpanel
     * @param imgId: the entity/img id
     * @param amount: how many times can this be built?
     * @param failureRatio: failure ratio of the entity
     */
    public void addToBuildPanel(int imgId, int amount, float failureRatio) {
        BuildPanelEntity buildPanelEntity = new BuildPanelEntity("build_element_" + imgId, IdGenerator.generateId(),
                150 + numberOfBuildPanelElements * (CELL_SIZE + ITEM_MARGIN), 875, CELL_SIZE, CELL_SIZE,
                Game.res().loadTile(imgId),
                amount, failureRatio,
                Game.res().getTileSet().getType(imgId),
                Game.res().loadDescription(imgId)
        );
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
                1600, 900,
                ITEM_WIDTH, ITEM_HEIGHT,
                "EXIT",
                font
        );
        exitButton.addIntent(new ExitIntent());
        this.addEntityToScene(exitButton);

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 800,
                ITEM_WIDTH, ITEM_HEIGHT,
                "MAIN MENU",
                font
        );

        mainMenuButton.addIntent(new StartIntent());
        this.addEntityToScene(mainMenuButton);
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
     * setup method for the description panel (right side)
     */
    private void setupDescriptionPanel() {
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
        descriptionPanelGC.addText("Target failure ratio: <=" + goal);
        descriptionPanelGC.addLocation(new Point(descriptionPanelBounds.getLocation().x, descriptionPanelBounds.getLocation().y + 600));

        descriptionPanel.addComponent(descriptionPanelGC);
        descriptionPanelGC.setEntity(descriptionPanel);

        addEntityToScene(descriptionPanel);
    }

    public int getCellSize() {
        return CELL_SIZE;
    }
}
