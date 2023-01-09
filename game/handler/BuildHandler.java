package game.handler;

import com.Game;
import com.IdGenerator;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.entity.Entity;
import com.input.handler.Handler;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.customexceptions.TooManyEntitiesAtGridPositionException;
import game.entities.CableEntity;
import game.entities.SimulationEntity;
import game.scenes.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildHandler extends Handler {
    public BuilderState currentBuildState = BuilderState.NOT_BUILDING;
    public Entity currentBuilding = null;

    @Override
    public void handle(KeyEvent e) {
        // do nothing
    }

    @Override
    public void handle(MouseEvent e) {
        ArrayList<Entity> entities = (ArrayList<Entity>) Game.scene().current().getEntities().clone();

        for(Entity entity : entities) {
            // check the button clicked is left click
            if (e.getButton() == MouseEvent.BUTTON1) {
                // Handle events when builder is not building
                if (currentBuildState == BuilderState.NOT_BUILDING) {
                    // check location of the click
                    if (entity.getComponent(CollisionComponent.class) != null
                            && entity.getComponent(CollisionComponent.class).contains(e.getPoint())) {
                        // check if the click was on a BuildComponent first -> create new Simulation Entity
                        if (entity.getComponent(BuildComponent.class) != null) {
                            // check amount left, >0? -> new Entity
                            if (entity.getComponent(BuildComponent.class).getAmount() > 0) {
                                SimulationEntity newEntity = new SimulationEntity(
                                        entity.getName() + "_simulation", IdGenerator.generateId(),
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                        -1, -1,
                                        entity.getComponent(GraphicsComponent.class).getImage(),
                                        entity.getComponent(BuildComponent.class).getFailureRatio(),
                                        entity.getComponent(BuildComponent.class).getSimulationType(),
                                        entity.getComponent(BuildComponent.class).getAmount(),
                                        true
                                );

                                GameScene gs = (GameScene) Game.scene().current();
                                currentBuilding = newEntity;
                                gs.addEntityToScene(newEntity);

                                entity.getComponent(BuildComponent.class)
                                        .subtractFromAmount();
                                entity.getComponent(GraphicsComponent.class)
                                        .getTexts()
                                        .set(0,
                                                String.valueOf(entity.getComponent(BuildComponent.class)
                                                        .getAmount()
                                                )
                                        );
                                currentBuildState = BuilderState.BUILDING_SIMULATION;
                                break;
                            }
                        }
                        // check if the click was on a SimulationComponent second, this will automatically exclude being a
                        // Build Panel Entity because of the previous if query -> create a new Cable Entity
                        else if (entity.getComponent(SimulationComponent.class) != null) {

                        }
                    }
                }
                // check if state is building
                else if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    // try to place the component
                    if(placeComponent(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                    }
                }
            }
            // check the button clicked is right click
            else if (e.getButton() == MouseEvent.BUTTON3) {
                // handle if is building simulation component
                if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    // remove the component
                    if(putBackToStack(currentBuilding)) {
                        currentBuildState = BuilderState.NOT_BUILDING;
                        currentBuilding = null;
                    }
                }
                // handle if not building state -> remove component
                else if(currentBuildState == BuilderState.NOT_BUILDING) {
                    // remove component at positon
                    Point gridLocation = findEntityGridPosition(e.getPoint());
                    if(gridLocation == null) {
                        return;
                    } else {
                        ArrayList<Entity> entitiesAtSameCell = new ArrayList<>();
                        try {
                            entitiesAtSameCell = getEntitiesAtGridPosition(gridLocation);
                        } catch(TooManyEntitiesAtGridPositionException ex) {
                            ex.printStackTrace();
                        }

                        for(Entity remove : entitiesAtSameCell) {
                            if(remove.isRemovable() && remove.getComponent(SimulationComponent.class) != null) {
                                if(putBackToStack(remove)) {
                                    return;
                                }
                            }
                        }
                    }
                }
            // check if not button is clicked
            } else if(e.getButton() == MouseEvent.NOBUTTON) {
                if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    currentBuilding.getComponent(GraphicsComponent.class).reposition(e.getPoint());
                }
            }
        }
    }

    private boolean putBackToStack(Entity e) {
        if(e instanceof SimulationEntity) {
            if (e.getComponent(SimulationComponent.class) != null) {
                //replace the component
                Pattern p = Pattern.compile("_\\d{3}");
                Matcher m1 = p.matcher(e.getName());
                if (m1.find()) {
                    for (Entity build : Game.scene().current().getEntities()) {
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

                Game.scene().current().getEntities().remove(e);
            }
        } else if(e instanceof CableEntity) {
            Game.scene().current().getEntities().remove(e);
        }
        //TODO: implement not found in build panel
        return true;
    }

    private boolean placeComponent(Entity e) {
        Point gridPos = findEntityGridPosition(e.getComponent(GraphicsComponent.class).getBounds().getLocation());

        if(gridPos == null) {
            return false;
        }

        ArrayList<Entity> entitiesAtSameCell = new ArrayList<>();
        try {
            entitiesAtSameCell = getEntitiesAtGridPosition(gridPos);
        } catch(TooManyEntitiesAtGridPositionException ex) {
            ex.printStackTrace();
        }

        if(entitiesAtSameCell.size() == 2) {
            for (Entity replace : entitiesAtSameCell) {
                if (replace.getComponent(SimulationComponent.class) != null) {
                    // replace an Entity

                    // check if replacing is possible, otherwise skip Entity
                    if(!replace.isRemovable()) {
                        continue;
                    }
                    if (putBackToStack(replace)) {
                        // Set location of grid
                        e.getComponent(GridComponent.class).setGridLocation(new Point(
                                (int) replace.getComponent(GridComponent.class).getGridLocation().getX(),
                                (int) replace.getComponent(GridComponent.class).getGridLocation().getY()
                        ));
                        // Set Grapics and Collision box
                        e.getComponent(GraphicsComponent.class).reposition(replace.getComponent(GraphicsComponent.class)
                                .getBounds().getLocation());
                        e.getComponent(CollisionComponent.class)
                                .setCollisionBox(e.getComponent(GraphicsComponent.class).get_BOUNDS());
                        Game.logger().info("Successfully added a new entity to the grid.\n" +
                                "Name: " + e.getName() +
                                "\nPosition: " + e.getComponent(GridComponent.class).getGridLocation() +
                                "\nRemovable: " + e.isRemovable());

                        return true;
                    }
                }
            }
        } else if(entitiesAtSameCell.size() == 1) {
            for (Entity replace : entitiesAtSameCell) {
                if (replace.getComponent(GridComponent.class) != null) {
                    // place a component

                    e.getComponent(GridComponent.class).setGridLocation(new Point(
                            (int) replace.getComponent(GridComponent.class).getGridLocation().getX(),
                            (int) replace.getComponent(GridComponent.class).getGridLocation().getY()
                    ));
                    // Set Grapics and Collision box
                    e.getComponent(GraphicsComponent.class).reposition(replace.getComponent(GraphicsComponent.class)
                            .getBounds().getLocation());
                    e.getComponent(CollisionComponent.class)
                            .setCollisionBox(e.getComponent(GraphicsComponent.class).get_BOUNDS());
                    Game.logger().info("Successfully added a new entity to the grid.\n" +
                            "Name: " + e.getName() +
                            "\nPosition: " + e.getComponent(GridComponent.class).getGridLocation() +
                            "\nRemovable: " + e.isRemovable());

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * find the grid position of a component
     * @param p
     * @return
     */
    private Point findEntityGridPosition(Point p) {
        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(CollisionComponent.class) != null
                    && e.getComponent(CollisionComponent.class).contains(p)
                    && e.getComponent(GridComponent.class) != null) {
                return (Point) e.getComponent(GridComponent.class).getGridLocation();
            }
        }
        return null;
    }

    /**
     * Gets all entities at a grid position
     * @param p
     * @return
     */
    private ArrayList<Entity> getEntitiesAtGridPosition(Point p) throws TooManyEntitiesAtGridPositionException {
        ArrayList<Entity> entitiesAtGridPosition = new ArrayList<>();

        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(GridComponent.class) != null) {
                if(e.getComponent(GridComponent.class).getGridLocation().getX() == p.getX()
                        && e.getComponent(GridComponent.class).getGridLocation().getY() == p.getY()) {
                    entitiesAtGridPosition.add(e);
                }
            }
        }

        if(entitiesAtGridPosition.size() > 2) {
            throw new TooManyEntitiesAtGridPositionException();
        }

        return entitiesAtGridPosition;
    }
}
