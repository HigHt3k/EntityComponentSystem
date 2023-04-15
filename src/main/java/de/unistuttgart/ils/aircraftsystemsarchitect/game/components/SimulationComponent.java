package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationState;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

import java.util.ArrayList;

/**
 * SimulationComponent are used by the {@link de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.SimulationSystem} to
 * correctly simulate the configuration of the current system.
 * It is used as a parameter storage for different variables such as
 * the failure ratio, failure detection ratio, current state and input channel states.
 */
public class SimulationComponent extends Component {
    private float failureRatio;
    private float failureRecognitionRatio = 0.9f;
    private SimulationState simulationState = SimulationState.INOPERATIVE;
    private SimulationType simulationType;
    private final ArrayList<Integer> groupIds = new ArrayList<>();
    private final int ownId = IdGenerator.generateId();
    private final ArrayList<Integer> inputIds = new ArrayList<>();
    private final ArrayList<SimulationState> inputStates = new ArrayList<>();
    private int tileId;

    // Markov simulation chain variables
    private int correctSignalsNeeded = 1;
    private int outOfControlSignalsAccepted = 0;
    private final int totalSignalsNeeded = 1;

    /**
     * Sets the tile ID of the entity this component is attached to.
     *
     * @param tileId The ID of the tile the entity is located on.
     */
    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    /**
     * Gets the tile ID of the entity this component is attached to.
     *
     * @return The ID of the tile the entity is located on.
     */
    public int getTileId() {
        return tileId;
    }

    /**
     * Gets the list of input states for this component.
     *
     * @return An {@link ArrayList} of {@link SimulationState} objects.
     */
    public ArrayList<SimulationState> getInputStates() {
        return inputStates;
    }

    /**
     * Adds an input state to the list of input states for this component.
     *
     * @param state The {@link SimulationState} to add to the list.
     */
    public void addInputState(SimulationState state) {
        this.inputStates.add(state);
    }

    /**
     * Resets the list of input states for this component.
     */
    public void resetInputStates() {
        inputStates.clear();
    }

    /**
     * Gets the list of input IDs for this component.
     *
     * @return An {@link ArrayList} of {@link Integer} objects.
     */
    public ArrayList<Integer> getInputIds() {
        return inputIds;
    }

    /**
     * Adds an input ID to the list of input IDs for this component.
     *
     * @param id The ID of the input to add to the list.
     */
    public void addInputId(int id) {
        inputIds.add(id);
    }

    /**
     * Resets the list of input IDs for this component.
     */
    public void resetInputIds() {
        inputIds.clear();
    }

    /**
     * Sets the number of correct signals needed for a Markov simulation.
     *
     * @param correctSignalsNeeded The number of correct signals needed.
     */
    public void setCorrectSignalsNeeded(int correctSignalsNeeded) {
        this.correctSignalsNeeded = correctSignalsNeeded;
    }

    /**
     * Sets the number of out-of-control signals that are accepted by this simulation component.
     * @param outOfControlSignalsAccepted the number of out-of-control signals to accept
     */
    public void setOutOfControlSignalsAccepted(int outOfControlSignalsAccepted) {
        this.outOfControlSignalsAccepted = outOfControlSignalsAccepted;
    }

    /**
     * Gets the number of correct signals needed for this simulation component.
     * @return the number of correct signals needed
     */
    public int getCorrectSignalsNeeded() {
        return correctSignalsNeeded;
    }

    /**
     * Gets the number of out-of-control signals accepted by this simulation component.
     * @return the number of out-of-control signals accepted
     */
    public int getOutOfControlSignalsAccepted() {
        return outOfControlSignalsAccepted;
    }

    /**
     * Sets the simulation state for this simulation component.
     * @param simulationState the simulation state to set
     */
    public void setSimulationState(SimulationState simulationState) {
        this.simulationState = simulationState;
    }

    /**
     * Gets the current simulation state for this simulation component.
     * @return the current simulation state
     */
    public SimulationState getSimulationState() {
        return simulationState;
    }

    /**
     * Sets the failure recognition ratio for this simulation component.
     * @param failureRecognitionRatio the failure recognition ratio to set
     */
    public void setFailureRecognitionRatio(float failureRecognitionRatio) {
        this.failureRecognitionRatio = failureRecognitionRatio;
    }

    /**
     * Gets the ID of this simulation component.
     * @return the ID of this simulation component
     */
    public int getOwnId() {
        return ownId;
    }

    /**
     * Gets the failure recognition ratio for this simulation component.
     * @return the failure recognition ratio
     */
    public float getFailureRecognitionRatio() {
        return failureRecognitionRatio;
    }

    /**
     * Sets the failure ratio for this simulation component.
     * @param failureRatio the failure ratio to set
     */
    public void setFailureRatio(float failureRatio) {
        this.failureRatio = failureRatio;
    }

    /**
     * Gets the failure ratio for this simulation component.
     * @return the failure ratio
     */
    public float getFailureRatio() {
        return failureRatio;
    }

    /**
     * Gets the simulation type for this simulation component.
     * @return the simulation type
     */
    public SimulationType getSimulationType() {
        return simulationType;
    }

    /**
     * Sets the simulation type for this simulation component.
     * In case of Sensor: add the ID of this component to the group IDs list to create a new group.
     * @param simulationType the simulation type to set for this component
     */
    public void setSimulationType(SimulationType simulationType) {
        this.simulationType = simulationType;
        if(simulationType == SimulationType.SENSOR) {
            groupIds.add(ownId);
        }
    }

    /**
     * Adds a group ID to the list of group IDs for this simulation component.
     * @param groupId the group ID to add
     */
    public void addGroupId(int groupId) {
        groupIds.add(groupId);
    }

    /**
     * Resets the currently stored group IDs to an empty list.
     * In case of Sensor type: group ID is set to the ID of this component
     */
    public void resetGroupIds() {
        groupIds.clear();
        if(this.getSimulationType() == SimulationType.SENSOR) {
            groupIds.add(ownId);
        }
    }

    /**
     * Gets the list of group IDs for this simulation component.
     * @return the list of group IDs
     */
    public ArrayList<Integer> getGroupIds() {
        return groupIds;
    }
}
