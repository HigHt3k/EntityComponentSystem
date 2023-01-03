package game.scenes;

import com.Game;
import com.IdGenerator;
import com.ecs.component.CollisionComponent;
import com.ecs.Entity;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.entity.GenericButton;
import com.ecs.intent.ExitIntent;
import com.ecs.intent.HoverIntent;
import com.graphics.elements.ToolTip;
import com.graphics.scene.Scene;
import game.components.*;
import game.entities.BuildPanelEntity;
import game.entities.GridEntity;
import game.entities.SimulationEntity;
import game.intent.BuildIntent;
import game.intent.SimulationIntent;
import game.intent.StartIntent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameScene extends Scene {
    private static final int ITEM_MARGIN = 20;
    private static final int ITEM_WIDTH = 300;
    private static final int ITEM_HEIGHT = 60;
    private static final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private static final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private static final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private static final Color HOVER_COLOR = new Color(40, 40, 40, 150);
    private static final int CELL_SIZE = 128;
    private String description;
    private double goal = 10e-4;
    private int numberOfBuildPanelElements = 0;
    private Entity currentlyBuilding = null;

    public Entity getCurrentlyBuilding() {
        return currentlyBuilding;
    }

    public void setCurrentlyBuilding(Entity entity) {
        currentlyBuilding = entity;
    }

    public GameScene(String name, int id) {
        super(name, id);
        setupBuildPanel();
    }

    public void init() {
        // Create the GUI including buttons going back to menu, exit etc.
        setupDescriptionPanel();
        setupButtons();
    }

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

    /**
     * place a new entity to the grid if possible. Don't build if the entity is placed outside the grid.
     * Replace if an entity is already on the grid. The "replaced" entity will go back to the building panel stack.
     * TODO: make this method more transparent and possibly use methods for identifying if a grid tile is already used or not.
     * @param mousePos
     */
    public boolean finalizeBuilding(Point mousePos) {
        for(Entity e : getEntities()) {
            if(e.getComponent(GridComponent.class) != null) {
                if(e.getComponent(GraphicsComponent.class)
                        .getBounds()
                        .contains(mousePos)
                ) {
                    checkEntity(e);

                    GridComponent gc = new GridComponent();
                    gc.setGridLocation(new Point(
                                (int) e.getComponent(GridComponent.class).getGridLocation().getX(),
                                (int) e.getComponent(GridComponent.class).getGridLocation().getY()
                            )
                    );
                    currentlyBuilding.addComponent(gc);
                    gc.setEntity(currentlyBuilding);

                    currentlyBuilding.getComponent(GraphicsComponent.class).reposition(e.getComponent(GraphicsComponent.class)
                            .get_BOUNDS().getLocation());
                    Game.logger().info("Successfully added a new entity to the grid: " + currentlyBuilding.getName());
                    currentlyBuilding = null;
                    return true;
                }
            }
        }
        return false;
    }

    public void checkEntity(Entity e) {
        for(Entity check : getEntities()) {
            if(e.getComponent(GridComponent.class) != null && check.getComponent(GridComponent.class) != null
                    && e != check) {
                if(e.getComponent(GridComponent.class).getGridLocation().equals(
                        check.getComponent(GridComponent.class).getGridLocation())) {
                    if(check.getComponent(SimulationComponent.class) != null) {
                        //replace the component
                        Pattern p = Pattern.compile("_\\d{3}");
                        Matcher m1 = p.matcher(check.getName());
                        if(m1.find()) {
                            for (Entity build : getEntities()) {
                                Matcher m2 = p.matcher(build.getName());
                                if (m2.find()) {
                                    if (build.getComponent(BuildComponent.class) != null) {
                                        if (m1.group(0).equals(m2.group(0))) {
                                            build.getComponent(BuildComponent.class).addToAmount();
                                            build.getComponent(GraphicsComponent.class).getTexts().set(0,
                                                    String.valueOf(build.getComponent(BuildComponent.class).getAmount()));
                                            Game.logger().info("Found component and putting back to build stack.");
                                            break;
                                        }
                                    }
                                }
                            }
                            Game.logger().info("If no log found for putting component back to stack: add component to levels" +
                                    "xml file: " + check.getName());
                        }

                        getEntities().remove(check);
                        break;
                    }
                }
            }
        }
    }

    public void removeComponent(Entity e) {
        if(e.getComponent(SimulationComponent.class) != null) {
            //replace the component
            Pattern p = Pattern.compile("_\\d{3}");
            Matcher m1 = p.matcher(e.getName());
            if(m1.find()) {
                for (Entity build : getEntities()) {
                    Matcher m2 = p.matcher(build.getName());
                    if (m2.find()) {
                        if (build.getComponent(BuildComponent.class) != null) {
                            if (m1.group(0).equals(m2.group(0))) {
                                build.getComponent(BuildComponent.class).addToAmount();
                                build.getComponent(GraphicsComponent.class).getTexts().set(0,
                                        String.valueOf(build.getComponent(BuildComponent.class).getAmount()));
                                Game.logger().info("Found component and putting back to build stack.");
                                break;
                            }
                        }
                    }
                }
                Game.logger().info("If no log found for putting component back to stack: add component to levels" +
                        "xml file: " + e.getName());
            }

            getEntities().remove(e);
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
                CELL_SIZE * x, CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                x, y,
                Game.res().loadTile(1)
        );
        addEntityToScene(gridEntity);
    }

    public void addSimulationElement(int x, int y, int imgId, float failureRatio, boolean removable) {
        SimulationEntity simulationEntity = new SimulationEntity(
                "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                CELL_SIZE * x, CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                x, y,
                Game.res().loadTile(imgId),
                failureRatio, Game.res().getTileSet().getType(imgId),
                4, removable
        );

        addEntityToScene(simulationEntity);
    }

    public void addToBuildPanel(int imgId, int amount, float failureRatio) {
        BuildPanelEntity buildPanelEntity = new BuildPanelEntity("build_element_" + imgId, IdGenerator.generateId(),
                150 + numberOfBuildPanelElements * (CELL_SIZE + ITEM_MARGIN), 875, CELL_SIZE, CELL_SIZE,
                Game.res().loadTile(imgId),
                amount, failureRatio,
                Game.res().loadDescription(imgId)
        );
        addEntityToScene(buildPanelEntity);
        numberOfBuildPanelElements++;
    }

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

    private void setupDescriptionPanel() {
        Entity descriptionPanel = new Entity("Description Panel", IdGenerator.generateId());

        GraphicsComponent descriptionPanelGC = new GraphicsComponent();
        Rectangle descriptionPanelBounds = new Rectangle(1500, 0, 420, 1080);
        descriptionPanelGC.setBounds(descriptionPanelBounds);

        try {
            descriptionPanelGC.setImage(ImageIO.read(new File("game/res/menus/box1.png")));
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
