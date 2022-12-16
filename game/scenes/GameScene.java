package game.scenes;

import com.Game;
import com.IdGenerator;
import com.ecs.CollisionComponent;
import com.ecs.Entity;
import com.ecs.GraphicsComponent;
import com.ecs.IntentComponent;
import com.ecs.intent.ExitIntent;
import com.ecs.intent.HoverIntent;
import com.graphics.scene.Scene;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.intent.StartIntent;

import java.awt.*;

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

    public GameScene(String name, int id) {
        super(name, id);

        // Create the GUI including buttons going back to menu, exit etc.
        setupButtons();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void update() {

    }

    public void addGridElement(int x, int y) {
        Entity gridElement = new Entity("grid_element_" + x + ":" + y, IdGenerator.generateId());
        GraphicsComponent gridElementGraphicsComponent = new GraphicsComponent();
        Rectangle gridElementBounds = new Rectangle(CELL_SIZE * x, CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
        gridElementGraphicsComponent.setBounds(gridElementBounds);
        gridElementGraphicsComponent.setHoverColor(HOVER_COLOR);
        gridElementGraphicsComponent.setImage(Game.res().loadTile(1));
        gridElement.addComponent(gridElementGraphicsComponent);
        gridElementGraphicsComponent.setEntity(gridElement);

        GridComponent gridElementGridComponent = new GridComponent();
        gridElementGridComponent.setGridLocation(new Point(x, y));
        gridElement.addComponent(gridElementGridComponent);
        gridElementGridComponent.setEntity(gridElement);

        CollisionComponent gridElementCollisionComponent = new CollisionComponent();
        gridElementCollisionComponent.setCollisionBox(gridElementBounds);
        gridElement.addComponent(gridElementCollisionComponent);
        gridElementCollisionComponent.setEntity(gridElement);

        IntentComponent gridElementIntentComponent = new IntentComponent();
        HoverIntent gridElementIntentComponentHoverIntent = new HoverIntent();
        gridElementIntentComponentHoverIntent.setIntentComponent(gridElementIntentComponent);
        gridElementIntentComponent.addIntent(gridElementIntentComponentHoverIntent);
        gridElement.addComponent(gridElementIntentComponent);
        gridElementIntentComponent.setEntity(gridElement);

        addEntityToScene(gridElement);
    }

    public void addSimulationElement(int x, int y, int imgId, float failureRatio, boolean interactable) {
        Entity simElement = new Entity("simulation_element_" + x + ":" + y, IdGenerator.generateId());

        GraphicsComponent simElementGraphicsComponent = new GraphicsComponent();
        Rectangle simElementBounds = new Rectangle(CELL_SIZE * x, CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
        simElementGraphicsComponent.setBounds(simElementBounds);
        simElementGraphicsComponent.setImage(Game.res().loadTile(imgId));
        simElement.addComponent(simElementGraphicsComponent);
        simElementGraphicsComponent.setEntity(simElement);

        GridComponent simElementGridComponent = new GridComponent();
        simElementGridComponent.setGridLocation(new Point(x, y));
        simElement.addComponent(simElementGridComponent);
        simElementGridComponent.setEntity(simElement);

        SimulationComponent simElementSimulationComponent = new SimulationComponent();
        simElementSimulationComponent.setFailureRatio(failureRatio);
        simElement.addComponent(simElementSimulationComponent);
        simElementSimulationComponent.setEntity(simElement);

        CollisionComponent simElementCollisionComponent = new CollisionComponent();
        simElementCollisionComponent.setCollisionBox(simElementBounds);
        simElement.addComponent(simElementCollisionComponent);
        simElementCollisionComponent.setEntity(simElement);

        IntentComponent simElementIntentComponent = new IntentComponent();
        HoverIntent simElementIntentComponentHoverIntent = new HoverIntent();
        simElementIntentComponentHoverIntent.setIntentComponent(simElementIntentComponent);
        simElementIntentComponent.addIntent(simElementIntentComponentHoverIntent);
        simElement.addComponent(simElementIntentComponent);
        simElementIntentComponent.setEntity(simElement);

        addEntityToScene(simElement);
    }

    private void setupButtons() {
        // Exit button
        Entity exitButton = new Entity("Exit", IdGenerator.generateId());
        GraphicsComponent exitButtonGraphicsComponent = new GraphicsComponent();
        Rectangle exitButtonBounds = new Rectangle(1600, 900, ITEM_WIDTH, ITEM_HEIGHT);
        exitButtonGraphicsComponent.setBounds(exitButtonBounds);
        exitButtonGraphicsComponent.setShape(exitButtonBounds);
        exitButtonGraphicsComponent.setText("EXIT");
        exitButtonGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        exitButtonGraphicsComponent.setTextColor(TEXT_COLOR);
        exitButtonGraphicsComponent.setBorderColor(BOX_BORDER_COLOR);
        exitButtonGraphicsComponent.setFillColor(BOX_COLOR);
        exitButtonGraphicsComponent.setHoverColor(HOVER_COLOR);

        exitButton.addComponent(exitButtonGraphicsComponent);
        exitButtonGraphicsComponent.setEntity(exitButton);

        CollisionComponent exitButtonCollisionComponent = new CollisionComponent();
        exitButtonCollisionComponent.setCollisionBox(exitButtonBounds);

        exitButton.addComponent(exitButtonCollisionComponent);
        exitButtonCollisionComponent.setEntity(exitButton);

        IntentComponent exitButtonIntentComponent = new IntentComponent();
        HoverIntent hi = new HoverIntent();
        hi.setIntentComponent(exitButtonIntentComponent);
        exitButtonIntentComponent.addIntent(hi);
        ExitIntent ei = new ExitIntent();
        ei.setIntentComponent(exitButtonIntentComponent);
        exitButtonIntentComponent.addIntent(ei);

        exitButtonIntentComponent.setEntity(exitButton);
        exitButton.addComponent(exitButtonIntentComponent);

        addEntityToScene(exitButton);

        // back to menu button
        Entity mainMenuButton = new Entity("Menu_button", IdGenerator.generateId());
        GraphicsComponent mainMenuButtonGraphicsComponent = new GraphicsComponent();
        Rectangle mainMenuButtonBounds = new Rectangle(1600, 800, ITEM_WIDTH, ITEM_HEIGHT);
        mainMenuButtonGraphicsComponent.setBounds(mainMenuButtonBounds);
        mainMenuButtonGraphicsComponent.setShape(mainMenuButtonBounds);
        mainMenuButtonGraphicsComponent.setText("MAIN MENU");
        mainMenuButtonGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        mainMenuButtonGraphicsComponent.setTextColor(TEXT_COLOR);
        mainMenuButtonGraphicsComponent.setBorderColor(BOX_BORDER_COLOR);
        mainMenuButtonGraphicsComponent.setFillColor(BOX_COLOR);
        mainMenuButtonGraphicsComponent.setHoverColor(HOVER_COLOR);

        mainMenuButton.addComponent(mainMenuButtonGraphicsComponent);
        mainMenuButtonGraphicsComponent.setEntity(mainMenuButton);

        CollisionComponent mainMenuButtonCollisionComponent = new CollisionComponent();
        mainMenuButtonCollisionComponent.setCollisionBox(mainMenuButtonBounds);

        mainMenuButton.addComponent(mainMenuButtonCollisionComponent);
        mainMenuButtonCollisionComponent.setEntity(mainMenuButton);

        IntentComponent mainMenuButtonIntentComponent = new IntentComponent();
        HoverIntent mainMenuButtonHoverIntent = new HoverIntent();
        mainMenuButtonHoverIntent.setIntentComponent(mainMenuButtonIntentComponent);
        mainMenuButtonIntentComponent.addIntent(mainMenuButtonHoverIntent);
        StartIntent mainMenuButtonStartIntent = new StartIntent();
        mainMenuButtonStartIntent.setIntentComponent(mainMenuButtonIntentComponent);
        mainMenuButtonIntentComponent.addIntent(mainMenuButtonStartIntent);

        mainMenuButtonIntentComponent.setEntity(mainMenuButton);
        mainMenuButton.addComponent(mainMenuButtonIntentComponent);

        addEntityToScene(mainMenuButton);
    }
}
