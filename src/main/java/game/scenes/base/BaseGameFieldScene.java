package game.scenes.base;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.RenderObject;
import engine.ecs.component.graphics.objects.ShapeObject;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.resource.ResourceManager;
import engine.resource.colorpalettes.Bit8;
import game.action.BuildPanelChange;
import game.action.BuildPanelChangeAction;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.entities.simulation.BuildPanelEntity;
import game.entities.simulation.GridEntity;
import game.entities.simulation.SimulationEntity;
import game.entities.ui.SimplePanel;
import game.handler.simulation.SimulationType;
import game.scenes.game.GameScene;
import game.scenes.util.BuildPanelPage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * this class can be used to inherit from to create the Build & GameScene.
 */
public class BaseGameFieldScene extends BaseScene {
    protected final int ITEM_MARGIN = 80;
    protected final int DESIGN_CELL_SIZE = 128;
    protected final ArrayList<BuildPanelPage> pages = new ArrayList<>();
    protected int CELL_SIZE = DESIGN_CELL_SIZE;
    protected ArrayList<String> descriptions;
    protected int accGoal;
    protected int sensGoal;
    protected int cGoal;
    protected int X_MARGIN = 200;
    protected int Y_MARGIN = 300;
    private boolean unlocked = false;
    protected int nextNewPage = -1;
    protected int currentPageShown = 0;
    private String description;
    private double goal = 10e-4;
    protected int numberOfBuildPanelElements = 0;

    private int xMax;
    private int yMax;

    protected Entity box;

    public BaseGameFieldScene(String name, int id) {
        super(name, id);

        try {
            ImageEntity background = new ImageEntity("Background", IdGenerator.generateId(),
                    ImageIO.read(new File("res/backgrounds/sky-not-animated.png")), 0, 0, 1920, 1080, Layer.BACKGROUND);
            addEntityToScene(background);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }

        try {
            ImageEntity aircraftGameScene = new ImageEntity("aircraftGameScene", IdGenerator.generateId(),
                    ImageIO.read(new File("res/backgrounds/aircraft-game-scene.png")), X_MARGIN - 120,
                    Y_MARGIN - 510, 1200, (int) (1200 * 1.0608f), Layer.GAMELAYER1);
            addEntityToScene(aircraftGameScene);
        } catch (IOException e) {
            Game.logger().severe("Couldn't load image.\n" + e.getMessage());
        }
        setupBuildPanel();

        box = new SimplePanel("box", IdGenerator.generateId(),
                50, 50, 200, 200, null, Bit8.DARK_GREY, null);
        addEntityToScene(box);
    }

    /**
     * adds a SimulationEntity to the scene
     *
     * @param x:            grid position at x
     * @param y:            grid position at y
     * @param imgId:        entity / image id
     * @param failureRatio: failure ratio of the entity
     * @param removable:    can be removed or not
     */
    public void addSimulationElement(int x, int y, int imgId, float failureRatio,
                                     int correctSignalsNeeded, int outOfControlSignalsAccepted, boolean removable, float failureDetectionRatio) {
        if (Game.res().getTileSet().getType(imgId) == SimulationType.CPU) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId), imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    correctSignalsNeeded, outOfControlSignalsAccepted,
                    new int[]{0, 1, 2, 3}, new int[]{0, 1, 2, 3}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if (Game.res().getTileSet().getType(imgId) == SimulationType.SENSOR) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId), imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    correctSignalsNeeded, outOfControlSignalsAccepted,
                    new int[]{}, new int[]{0, 1, 2, 3}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if (Game.res().getTileSet().getType(imgId) == SimulationType.ACTUATOR) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId), imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    correctSignalsNeeded, outOfControlSignalsAccepted,
                    new int[]{0, 1, 2, 3}, new int[]{}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if (Game.res().getTileSet().getType(imgId) == SimulationType.CABLE) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId), imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    0, 0,
                    new int[]{imgId - 500}, new int[]{imgId - 500}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if (Game.res().getTileSet().getType(imgId) == SimulationType.VOTE) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId), imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    0, 0,
                    new int[]{0, 1, 2, 3}, new int[]{0, 1, 2, 3}, removable, failureDetectionRatio
            );
            addEntityToScene(simulationEntity);
        } else if (Game.res().getTileSet().getType(imgId) == SimulationType.PLACEHOLDER) {
            SimulationEntity simulationEntity = new SimulationEntity(
                    "simulation_element_" + imgId + ":" + x + ":" + y, IdGenerator.generateId(),
                    X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                    x, y,
                    Game.res().loadTile(imgId), imgId,
                    failureRatio, Game.res().getTileSet().getType(imgId),
                    0, 0,
                    new int[]{}, new int[]{}, removable, failureDetectionRatio
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
     * add an Entity that snaps to the grid. This method can e.g. be called from the {@link ResourceManager} to parse
     * the level.XML file to the actual {@link GameScene}.
     * A {@link GridComponent} is added to the Entity to store the grid position data, which can be called to validate entities against
     * each other without having to calculate their grid position based on the actual screen position.
     *
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

    protected void updateBox() {
        box.getComponent(RenderComponent.class).getRenderObjectsOfType(ShapeObject.class).get(0).setLocation(new Point(X_MARGIN, Y_MARGIN));
        box.getComponent(RenderComponent.class).getRenderObjectsOfType(ShapeObject.class).get(0).setBounds(new Rectangle(X_MARGIN, Y_MARGIN, xMax * CELL_SIZE, yMax * CELL_SIZE));
    }

    /**
     * Update the grid size / cell size
     */
    public void updateGridSize() {
        if (xMax > yMax && (xMax > 7 || yMax > 5)) {
            CELL_SIZE = DESIGN_CELL_SIZE * 7 / xMax;
        } else if (xMax > 7 || yMax > 5) {
            CELL_SIZE = DESIGN_CELL_SIZE * 5 / yMax;
        } else {
            CELL_SIZE = DESIGN_CELL_SIZE;
        }

        X_MARGIN = (1500 - xMax * CELL_SIZE) / 2;
        Y_MARGIN = (850 - yMax * CELL_SIZE) / 2;

        updateBox();
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

    /**
     * set the goal of the level
     *
     * @param goal: target failure rate
     */
    public void setGoal(double goal) {
        this.goal = goal;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @return the description as defined in the level.xml data
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description of the game scene
     *
     * @param description: the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    protected void updateEntitySize() {
        for (Entity e : getEntities()) {
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

    public void setGridSize(int x, int y) {
        //CELL_SIZE = 750/y;
        xMax = x;
        yMax = y;
        updateGridSize();
        updateEntitySize();
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMax() {
        return yMax;
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
}
