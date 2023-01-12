package game.handler;

import com.Game;
import com.ecs.entity.Entity;
import com.ecs.system.SystemHandle;
import game.components.*;
import game.entities.CableCombinerEntity;

import java.util.ArrayList;

public class SimulationSystem extends SystemHandle {
    private ArrayList<Entity> tempGroup = new ArrayList<>();

    @Override
    public void handle() {
        updateGroupIds();
        int[] groupIds = getDistinctGroupIds();

        for(int i : groupIds) {
            ArrayList<Entity> group = getEntitiesByGroupdId(i);
            boolean valid = validateGroup(group);
            if(valid) {
                calculateGroupFailureRatio(group);
            }
        }
    }

    private void calculateGroupFailureRatio(ArrayList<Entity> group) {
        ArrayList<Entity> sensors = new ArrayList<>();
        ArrayList<Entity> actuators = new ArrayList<>();
        ArrayList<Entity> cpus = new ArrayList<>();

        // distinct components
        for(Entity e : group) {
            if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CPU) {
                cpus.add(e);
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.SENSOR) {
                sensors.add(e);
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.ACTUATOR) {
                actuators.add(e);
            }
        }

        // Single Component Failure
        float failureProbability = 0f;
        for(Entity e : group) {
            failureProbability += e.getComponent(SimulationComponent.class).getFailureRatio();
        }
        /*

        System.out.println(group);
        System.out.println("Failure Probability for Single Component Failure: " + failureProbability);
        System.out.println("... for a 2hrs flight: " + failureProbability * 120);

         */
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
                    && relevantEntity.getComponent(SimulationComponent.class).getGroupIds().contains(id)) {
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
            for(int id : relevantEntity.getComponent(SimulationComponent.class).getGroupIds()) {
                if(!distinctIds.contains(id)) {
                    distinctIds.add(id);
                }
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
        // TODO: Implement that cable port in 0 and out 0 only lead to group id update / connection. if 0, 1 and 0 is connected, only 0
        // should be used
        ArrayList<Entity> sensors = getSensors();

        for(Entity sensor : sensors) {
            tempGroup = new ArrayList<>();
            ArrayList<Entity> connectedTo = getInterconnection(sensor);
            connectedTo.add(sensor);

            if(sensors.isEmpty()) {
                continue;
            }

            for(Entity e : connectedTo) {
                if(e.getComponent(SimulationComponent.class) != null) {
                    if(!sensors.contains(e)) {
                        if(!e.getComponent(SimulationComponent.class).getGroupIds().contains(sensor.getComponent(SimulationComponent.class).getGroupIds().get(0)))
                            e.getComponent(SimulationComponent.class).addGroupId(sensor.getComponent(SimulationComponent.class).getGroupIds().get(0));
                    }
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
                if(group.contains(port.getConnectedEntity())) {
                    continue;
                }

                if(port.getType() == CablePortType.IN) {
                    continue;
                }

                if(port.getConnectedEntity() == null) {
                    continue;
                }

                if(e instanceof CableCombinerEntity) {
                    CablePortType portType = port.getType();
                    CablePortType otherSide;
                    if(port.getType() == CablePortType.IN) {
                        otherSide = CablePortType.OUT;
                    } else {
                        otherSide = CablePortType.IN;
                    }

                    // only add to group if port in - out is 0-0, 1-1, 2-2, 3-3
                    if(!tempGroup.contains(e.getComponent(CablePortsComponent.class)
                            .getCablePort(port.getPortId(), CablePortType.IN)
                            .getConnectedEntity())) {
                        continue;
                    }
                }

                group.add(port.getConnectedEntity());
                tempGroup.addAll(group);
                group.addAll(getInterconnection(port.getConnectedEntity()));
                tempGroup.addAll(group);
            }
        }

        return group;
    }
}
