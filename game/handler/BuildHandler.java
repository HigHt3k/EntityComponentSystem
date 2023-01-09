package game.handler;

import com.Game;
import com.IdGenerator;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.entity.Entity;
import com.input.handler.Handler;
import game.components.*;
import game.customexceptions.TooManyEntitiesAtGridPositionException;
import game.entities.CableEntity;
import game.entities.SimulationEntity;

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
                                        4,
                                        true
                                );

                                currentBuilding = newEntity;
                                Game.scene().current().addEntityToScene(newEntity);

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
                        else if (entity.getComponent(GridComponent.class) != null) {
                            // handle the cable placement
                            if(entity.getComponent(CablePortsComponent.class) != null) {
                                // check for available cable ports on the clicked entity
                                ArrayList<CablePort> ports = entity.getComponent(CablePortsComponent.class).getAvailablePorts();
                                if(!ports.isEmpty()) {
                                    CableEntity cable = new CableEntity(
                                            "cable", IdGenerator.generateId(),
                                            e.getPoint().x,
                                            e.getPoint().y,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                            -1, -1,
                                            entity, null
                                    );
                                    ports.get(0).setConnectedEntity(cable);
                                    Game.scene().current().addEntityToScene(cable);
                                    currentBuilding = cable;
                                    currentBuildState = BuilderState.BUILDING_CABLE;
                                    break;
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                }
                // check if state is building
                else if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    // try to place the component
                    if(placeComponent(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                        break;
                    }
                }
                // check if state is building cable
                else if (currentBuildState == BuilderState.BUILDING_CABLE) {
                    // try to connect the cable to a neighboring grid tile.
                    if(placeCable(currentBuilding, e.getPoint())) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                        break;
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
                // handle if building cable -> remove cable
                else if(currentBuildState == BuilderState.BUILDING_CABLE) {
                    CablePort[] ports = currentBuilding.getComponent(CablePortsComponent.class).getCablePorts();
                    for(CablePort port : ports) {
                        if(port != null) {
                            if(port.getConnectedEntity() != null) {
                                CablePort[] otherPorts = port.getConnectedEntity().getComponent(CablePortsComponent.class).getCablePorts();
                                for (CablePort otherPort : otherPorts) {
                                    if (otherPort.getConnectedEntity() == currentBuilding) {
                                        otherPort.setConnectedEntity(null);
                                    }
                                }
                            }
                        }
                    }
                    Game.scene().current().removeEntityFromScene(currentBuilding);

                    currentBuildState = BuilderState.NOT_BUILDING;
                    currentBuilding = null;
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
            // check if no button is clicked
            } else if(e.getButton() == MouseEvent.NOBUTTON) {
                if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    currentBuilding.getComponent(GraphicsComponent.class).reposition(e.getPoint());
                }
                else if(currentBuildState == BuilderState.BUILDING_CABLE) {
                    // Reposition the cable end
                    currentBuilding
                            .getComponent(GraphicsComponent.class)
                            .setLine(currentBuilding
                                    .getComponent(GraphicsComponent.class)
                                    .getLineStart(),
                                    e.getPoint()
                            );
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

    private boolean placeCable(Entity e, Point mousePos) {
        Point gridPos = findEntityGridPosition(e.getComponent(GraphicsComponent.class).getBounds().getLocation());
        ArrayList<Entity> clickedEntities = new ArrayList<>();

        Point mouseGridPos = findEntityGridPosition(mousePos);
        if(mouseGridPos == null) {
            return false;
        }
        try {
            clickedEntities.addAll(getEntitiesAtGridPosition(mouseGridPos));
        } catch(TooManyEntitiesAtGridPositionException ex) {
            ex.printStackTrace();
        }
        ArrayList<Entity> neighbors = getEntitiesNeighboringGridPosition(gridPos);

        // check if any of the two lists is empty, if so, it isn't possible to establish a connection here -> return false
        if(clickedEntities.isEmpty() || neighbors.isEmpty()) {
            Game.logger().info("any of the two lists is empty");
            return false;
        }

        // if both lists have content, check if the clicked entities are a subset of the neighbors list, if not, return false
        if(!neighbors.containsAll(clickedEntities)) {
            Game.logger().info("not all entities in clicked list are in neighbors list");
            System.out.println("---- debug : Neighbors");
            for(Entity debug : neighbors) {
                System.out.println(debug.getName() + " at: " + debug.getComponent(GridComponent.class).getGridLocation());
            }

            System.out.println("---- debug : clicked");
            for(Entity debug : clickedEntities) {
                System.out.println(debug.getName() + " at: " + debug.getComponent(GridComponent.class).getGridLocation());
            }
            return false;
        }

        // check if there is already a cable at mouseGridPos, cables can have two entities at the same location

        for(Entity clicked : clickedEntities) {
            // check if entity has cable ports available; there should be only one with cable ports?
            // TODO: (what about cables)
            if(clicked.getComponent(CablePortsComponent.class) != null) {
                if(!clicked.getComponent(CablePortsComponent.class).getAvailablePorts().isEmpty()) {
                    // set the interconnection
                    currentBuilding.getComponent(CablePortsComponent.class).getCablePort(1).setConnectedEntity(clicked);
                    clicked.getComponent(CablePortsComponent.class).getAvailablePorts().get(0).setConnectedEntity(currentBuilding);

                    currentBuilding.getComponent(GridComponent.class).setGridLocation(mouseGridPos);

                    currentBuilding.getComponent(GraphicsComponent.class).setBounds(
                            clicked.getComponent(GraphicsComponent.class).get_BOUNDS()
                    );
                    currentBuilding.getComponent(CollisionComponent.class).setCollisionBox(
                            currentBuilding.getComponent(GraphicsComponent.class).get_BOUNDS()
                    );
                    currentBuilding.getComponent(GraphicsComponent.class).setLine(
                            new Point((int) (currentBuilding.getComponent(CablePortsComponent.class).getCablePort(0).getConnectedEntity()
                                    .getComponent(GraphicsComponent.class).getBounds().getX() + 50),
                                    (int) (currentBuilding.getComponent(CablePortsComponent.class).getCablePort(0).getConnectedEntity()
                                            .getComponent(GraphicsComponent.class).getBounds().getY() + 40)
                            ),
                            new Point(currentBuilding.getComponent(GraphicsComponent.class).getBounds().x + 50,
                                    currentBuilding.getComponent(GraphicsComponent.class).getBounds().y + 40)
                    );

                    return true;
                }
            }
        }

        for(Entity clicked : clickedEntities) {
            if(clicked.getComponent(CablePortsComponent.class) == null
                    && clicked.getComponent(SimulationComponent.class) == null) {
                // leave the second port of a cable at null, because there is no connection here. however, move the cable
                // entity here
                currentBuilding.getComponent(GridComponent.class).setGridLocation(mouseGridPos);
                System.out.println(mouseGridPos);

                currentBuilding.getComponent(GraphicsComponent.class).setBounds(
                        clicked.getComponent(GraphicsComponent.class).get_BOUNDS()
                );
                currentBuilding.getComponent(CollisionComponent.class).setCollisionBox(
                        currentBuilding.getComponent(GraphicsComponent.class).get_BOUNDS()
                );
                currentBuilding.getComponent(GraphicsComponent.class).setLine(
                        new Point((int) (currentBuilding.getComponent(CablePortsComponent.class).getCablePort(0).getConnectedEntity()
                                                        .getComponent(GraphicsComponent.class).getBounds().getX() + 50),
                                (int) (currentBuilding.getComponent(CablePortsComponent.class).getCablePort(0).getConnectedEntity()
                                                                        .getComponent(GraphicsComponent.class).getBounds().getY() + 20)
                        ),
                        new Point(currentBuilding.getComponent(GraphicsComponent.class).getBounds().x + 50,
                                currentBuilding.getComponent(GraphicsComponent.class).getBounds().y + 20)
                );
                return true;
            }
        }

        return false;
    }

    private ArrayList<Entity> getEntitiesNeighboringGridPosition(Point p) {
        ArrayList<Entity> neighbors = new ArrayList<>();

        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(GridComponent.class) != null) {
                if(e == currentBuilding) {
                    continue;
                }
                Point possibleNeighbor = (Point) e.getComponent(GridComponent.class).getGridLocation();
                if(calculateDistanceBetweenPoints(p.x, p.y, possibleNeighbor.x, possibleNeighbor.y) <= 1) {
                    neighbors.add(e);
                }
            }
        }

        return neighbors;
    }

    private double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}
