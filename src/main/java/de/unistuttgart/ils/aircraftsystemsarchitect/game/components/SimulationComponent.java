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


    // stuff for markov simulation chain
    private int correctSignalsNeeded = 1;
    private int outOfControlSignalsAccepted = 0;
    private final int totalSignalsNeeded = 1;

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public int getTileId() {
        return tileId;
    }

    public ArrayList<SimulationState> getInputStates() {
        return inputStates;
    }

    public void addInputState(SimulationState state) {
        this.inputStates.add(state);
    }

    public void resetInputStates() {
        inputStates.clear();
    }

    public ArrayList<Integer> getInputIds() {
        return inputIds;
    }

    public void addInputId(int id) {
        inputIds.add(id);
    }

    public void resetInputIds() {
        inputIds.clear();
    }

    public void setCorrectSignalsNeeded(int correctSignalsNeeded) {
        this.correctSignalsNeeded = correctSignalsNeeded;
    }

    public void setOutOfControlSignalsAccepted(int outOfControlSignalsAccepted) {
        this.outOfControlSignalsAccepted = outOfControlSignalsAccepted;
    }

    public int getCorrectSignalsNeeded() {
        return correctSignalsNeeded;
    }

    public int getOutOfControlSignalsAccepted() {
        return outOfControlSignalsAccepted;
    }

    public void setSimulationState(SimulationState simulationState) {
        this.simulationState = simulationState;
    }

    public SimulationState getSimulationState() {
        return simulationState;
    }

    public void setFailureRecognitionRatio(float failureRecognitionRatio) {
        this.failureRecognitionRatio = failureRecognitionRatio;
    }

    public int getOwnId() {
        return ownId;
    }

    public float getFailureRecognitionRatio() {
        return failureRecognitionRatio;
    }

    public void setFailureRatio(float failureRatio) {
        this.failureRatio = failureRatio;
    }

    public float getFailureRatio() {
        return failureRatio;
    }

    public SimulationType getSimulationType() {
        return simulationType;
    }

    /**
     * Set the simulation type.
     * In case of Sensor: add the id of this component to the group ids list to create a new group.
     * @param simulationType: the simulation type to set for this component
     */
    public void setSimulationType(SimulationType simulationType) {
        this.simulationType = simulationType;
        if(simulationType == SimulationType.SENSOR) {
            groupIds.add(ownId);
        }
    }


    public void addGroupId(int groupId) {
        groupIds.add(groupId);
    }

    /**
     * Resets the currently stored group ids to an empty list.
     * In case of Sensor type: group id is set to the id of this component
     */
    public void resetGroupIds() {
        groupIds.clear();
        if(this.getSimulationType() == SimulationType.SENSOR) {
            groupIds.add(ownId);
        }
    }

    public ArrayList<Integer> getGroupIds() {
        return groupIds;
    }
}
