package game.handler;

import com.Game;
import com.IdGenerator;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.entity.Entity;
import com.input.handler.Handler;
import com.input.handler.HandlerType;
import game.components.*;
import game.customexceptions.TooManyEntitiesAtGridPositionException;
import game.entities.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildHandler extends Handler {
    public BuilderState currentBuildState = BuilderState.NOT_BUILDING;
    public Entity currentBuilding = null;

    public BuildHandler() {
        super(HandlerType.EVENT);
    }

    @Override
    public void handle(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_D) {
            printAllEntitiesGridPosition();
        }
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
                                ArrayList<CablePort> ports = entity.getComponent(CablePortsComponent.class).getAvailablePortsOut();
                                if(!ports.isEmpty()) {
                                    /*CableEntity cable = new CableEntity(
                                            "cable", IdGenerator.generateId(),
                                            e.getPoint().x,
                                            e.getPoint().y,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                            -1, -1,
                                            entity, null
                                    );*/

                                    // testing refactored cable
                                    CableEntity cable = new CableEntity(
                                            "cable_ref", IdGenerator.generateId(),
                                            e.getPoint().x, e.getPoint().y,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
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
                    if(placeCableRefactored(currentBuilding, e.getPoint())) {
                        colorizeAndPositionCable((CableEntity) currentBuilding);
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
                    CablePort[] ports = currentBuilding.getComponent(CablePortsComponent.class).getCablePortsIn();
                    for(CablePort port : ports) {
                        if(port != null) {
                            if(port.getConnectedEntity() != null) {
                                CablePort[] otherPorts = port.getConnectedEntity().getComponent(CablePortsComponent.class).getCablePortsOut();
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

        if(entitiesAtGridPosition.size() > 3) {
            throw new TooManyEntitiesAtGridPositionException();
        }

        return entitiesAtGridPosition;
    }

    /**
     * Calculate the distance between 2 points (x,y)1, (x,y)2
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    /**
     * Calculate the distance between 2 points (p1, p2)
     * @param p1
     * @param p2
     * @return
     */
    private double calculateDistanceBetweenPoints(
            Point p1, Point p2) {
        double distance = Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
        return distance;
    }

    /**
     * Debugging method that prints all entities and their grid positions
     */
    private void printAllEntitiesGridPosition() {
        System.out.println("------Debugging-------");
        ArrayList<Entity> entities = Game.scene().current().getEntities();

        for(Entity e : entities) {
            if(e.getComponent(GridComponent.class) != null) {
                System.out.println(e.getName() + " at position: " + e.getComponent(GridComponent.class).getGridLocation());
            }
        }
    }

    /**
     * Refactored cable placing method. Creates a cable combiner if not available on the field clicked; connects cables if
     * possible.
     * @param e
     * @param mousePos
     * @return
     */
    private boolean placeCableRefactored(Entity e, Point mousePos) {
        Point gridPos = findEntityGridPosition(e.getComponent(GraphicsComponent.class).getBounds().getLocation());
        ArrayList<Entity> clickedEntities = new ArrayList<>();

        Point mouseGridPos = findEntityGridPosition(mousePos);
        // if mouse not in grid, dont place
        if(mouseGridPos == null) {
            return false;
        }
        try {
            clickedEntities.addAll(getEntitiesAtGridPosition(mouseGridPos));
        } catch(TooManyEntitiesAtGridPositionException ex) {
            ex.printStackTrace();
        }

        // If not entity was clicked, dont place
        if(clickedEntities.isEmpty()) {
            Game.logger().info("any of the two lists is empty");
            return false;
        }

        for(Entity entity : clickedEntities) {
            // If clicked entity is not a neighbor, dont place.
            if(!isNeighbor(e.getComponent(CablePortsComponent.class).getCablePortIn(0).getConnectedEntity(), entity)) {
                return false;
            }
            // Cant connect components besides each other without a space in between for cables.
            if(entity.getComponent(SimulationComponent.class) != null
                    && e.getComponent(CablePortsComponent.class).getCablePortIn(0).getConnectedEntity().getComponent(SimulationComponent.class) != null) {
                return false;
            }
        }

        // check if clicked instance is a cable combiner
        for(Entity entity : clickedEntities) {
            if(entity instanceof CableCombinerEntity ccer) {
                if(ccer.getComponent(CablePortsComponent.class).getAvailablePortsIn().isEmpty()) {
                    return false;
                }
                ccer.getComponent(CablePortsComponent.class).getAvailablePortsIn().get(0).setConnectedEntity(e);
                e.getComponent(CablePortsComponent.class).getCablePortOut(0).setConnectedEntity(ccer);
                return true;
            }
        }

        // check if clicked instance is a sim entity
        for(Entity entity : clickedEntities) {
            if(entity instanceof SimulationEntity se) {
                if(se.getComponent(CablePortsComponent.class).getAvailablePortsIn().isEmpty()) {
                    return false;
                }
                se.getComponent(CablePortsComponent.class).getAvailablePortsIn().get(0).setConnectedEntity(e);
                e.getComponent(CablePortsComponent.class).getCablePortOut(0).setConnectedEntity(se);
                return true;
            }
        }

        // check if clicked entity is a grid entity -> create new cablecombiner here and connect the cable automatically
        for(Entity entity : clickedEntities) {
            if(entity instanceof GridEntity) {
                CableCombinerEntity ccer = new CableCombinerEntity(
                        "cable_combiner", IdGenerator.generateId(),
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                        (int) entity.getComponent(GridComponent.class).getGridLocation().getX(),
                        (int) entity.getComponent(GridComponent.class).getGridLocation().getY()
                );
                Game.scene().current().addEntityToScene(ccer);
                ccer.getComponent(CablePortsComponent.class).getAvailablePortsIn().get(0).setConnectedEntity(e);
                e.getComponent(CablePortsComponent.class).getCablePortOut(0).setConnectedEntity(ccer);
                return true;
            }
        }

        return false;
    }

    private boolean isNeighbor(Entity e1, Entity e2) {
        return calculateDistanceBetweenPoints(
                (Point) e1.getComponent(GridComponent.class).getGridLocation(),
                (Point) e2.getComponent(GridComponent.class).getGridLocation()
        ) <= 1.0;
    }

    private void colorizeAndPositionCable(CableEntity e) {
        CablePortsComponent cpc = e.getComponent(CablePortsComponent.class);
        int leftId = cpc.getCablePortIn(0)
                .getConnectedEntity()
                .getComponent(CablePortsComponent.class)
                .getPortIdOut(e);
        int rightId = cpc.getCablePortOut(0)
                .getConnectedEntity()
                .getComponent(CablePortsComponent.class)
                .getPortIdIn(e);

        System.out.println(leftId + " " + rightId);

        switch (leftId) {
            case 0, 4 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_1_COLOR);
            case 1, 5 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_2_COLOR);
            case 2, 6 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_3_COLOR);
            case 3, 7 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_4_COLOR);
        }

        e.getComponent(GraphicsComponent.class).setLine(new Point(
                        cpc.getCablePortIn(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().x +
                        cpc.getCablePortIn(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().width/2,
                        (int) (cpc.getCablePortIn(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().y + (leftId%4 + 1.0) *
                                                cpc.getCablePortIn(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().height/5)
                ),
                new Point(
                        cpc.getCablePortOut(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().x +
                                cpc.getCablePortOut(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().width/2,
                        (int) (cpc.getCablePortOut(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().y + (rightId%4 + 1.0) *
                                cpc.getCablePortOut(0).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().height/5)
                )
        );
    }
}
