package game.handler;

import com.Game;
import com.ecs.entity.Entity;
import com.ecs.system.SystemHandle;
import game.components.*;
import game.entities.CableEntity;
import game.scenes.GameScene;

import java.util.ArrayList;

public class SimulationSystem extends SystemHandle {

    @Override
    public void handle() {
        updateGroupIds();
        int[] groupIds = getDistinctGroupIds();

        for(int i : groupIds) {
            ArrayList<Entity> group = getEntitiesByGroupdId(i);
            boolean valid = validateGroup(group);
            System.out.println("group with id: " + i + " is " + valid);
        }
    }

    private boolean validateGroup(ArrayList<Entity> group) {
        boolean hasCPU = false;
        boolean hasSensor = false;
        boolean hasActuator = false;
        for(Entity e : group) {
            if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CPU) {
                hasCPU = true;
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.SENSOR) {
                hasSensor = true;
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.ACTUATOR) {
                hasActuator = true;
            }
        }
        return hasCPU && hasSensor && hasActuator;
    }

    /**
     * Prefiltering method to skip unnecessary Entities such as buttons or cables
     * @return: All relevant entities for the calculation
     */
    private ArrayList<Entity> gatherRelevantEntities() {
        ArrayList<Entity> relevantEntities = new ArrayList<>();

        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(SimulationComponent.class) != null) {
                relevantEntities.add(e);
            }
        }

        return relevantEntities;
    }

    /**
     * get all entities assigned to a specified group
     * @param id: the group id to look up
     * @return an ArrayList with all Entities in that group
     */
    private ArrayList<Entity> getEntitiesByGroupdId(int id) {
        ArrayList<Entity> relevantEntities = gatherRelevantEntities();
        ArrayList<Entity> group = new ArrayList<>();

        for(Entity relevantEntity : relevantEntities) {
            if(relevantEntity.getComponent(SimulationComponent.class) != null
                    && relevantEntity.getComponent(SimulationComponent.class).getGroupId() == id) {
                group.add(relevantEntity);
            }
        }

        return group;
    }

    /**
     * checks if a group is connected correctly and is able to simulate
     * @param group: all entities to check for (a group)
     * @return false if not properly connected, return true if properly connected
     */
    private boolean checkGroupState(ArrayList<Entity> group) {


        return false;
    }

    private int[] getDistinctGroupIds() {
        ArrayList<Integer> distinctIds = new ArrayList<>();
        ArrayList<Entity> relevantEntities = gatherRelevantEntities();

        for(Entity relevantEntity : relevantEntities) {
            if(!distinctIds.contains(relevantEntity.getComponent(SimulationComponent.class).getGroupId())) {
                distinctIds.add(relevantEntity.getComponent(SimulationComponent.class).getGroupId());
            }
        }

        return distinctIds.stream().mapToInt(i -> i).toArray();
    }

    private void markov() {
        //TODO: Implement Markov Chain Detection
    }

    /**
     * Update the group id of each simulation entity based on cable connections
     */
    private void updateGroupIds() {
        ArrayList<Entity> sensors = getSensors();

        for(Entity sensor : sensors) {
            ArrayList<Entity> connectedTo = getInterconnection(sensor);
            connectedTo.add(sensor);
            ArrayList<Entity> cpus = findCPUs(connectedTo);

            if(cpus.isEmpty()) {
                continue;
            }

            for(Entity e : connectedTo) {
                if(e.getComponent(SimulationComponent.class) != null) {
                    if(!cpus.contains(e))
                        e.getComponent(SimulationComponent.class).setGroupId(cpus.get(0).getComponent(SimulationComponent.class).getGroupId());
                }
            }
        }

        // debugger
        /*System.out.println("-----groups---------");
        for(Entity e : gatherRelevantEntities()) {
            if(e.getComponent(SimulationComponent.class) != null) {
                System.out.println(e.getName() + "|||||" + e.getComponent(SimulationComponent.class).getGroupId());
            }
        }
        */

    }

    private ArrayList<Entity> findCPUs(ArrayList<Entity> group) {
        ArrayList<Entity> cpus = new ArrayList<>();

        for(Entity g : group) {
            if(g.getComponent(SimulationComponent.class) != null && g.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CPU) {
                cpus.add(g);
            }
        }
        return cpus;
    }

    private ArrayList<Entity> getSensors() {
        ArrayList<Entity> relevantEntities = new ArrayList<>();

        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(SimulationComponent.class) != null
                    && e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.SENSOR) {
                relevantEntities.add(e);
            }
        }

        return relevantEntities;
    }

    private ArrayList<Entity> getInterconnection(Entity e) {
        ArrayList<Entity> group = new ArrayList<>();

        if(e.getComponent(CablePortsComponent.class) != null) {
            ArrayList<CablePortEntity> ports = e.getComponent(CablePortsComponent.class).getCablePorts();

            for(CablePortEntity port : ports) {
                if(port.getConnectedEntity() == null) {
                    continue;
                }

                if(group.contains(port.getConnectedEntity())) {
                    continue;
                }

                if(port.getType() == CablePortType.IN) {
                    continue;
                }
                group.add(port.getConnectedEntity());

                group.addAll(getInterconnection(port.getConnectedEntity()));
            }
        }

        return group;
    }
}