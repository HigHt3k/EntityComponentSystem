package game.components;

import engine.IdGenerator;
import engine.ecs.component.Component;
import game.handler.simulation.SimulationState;
import game.handler.simulation.SimulationType;

import java.util.ArrayList;

public class SimulationComponent extends Component {
    private float failureRatio;
    private float failureRecognitionRatio = 0.9f;
    private SimulationState simulationState = SimulationState.INOPERATIVE;
    private SimulationType simulationType;
    private ArrayList<Integer> groupIds = new ArrayList<>();
    private int ownId = IdGenerator.generateId();
    private ArrayList<Integer> inputIds = new ArrayList<>();
    private ArrayList<SimulationState> inputStates = new ArrayList<>();
    private int tileId;


    // stuff for markov simulation chain
    private int correctSignalsNeeded = 1;
    private int outOfControlSignalsAccepted = 0;

    @Override
    public void update() {

    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }

    public int getTileId() {
        return tileId;
    }

    public void setOwnId(int id) {
        ownId = id;
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

    public void setSimulationType(SimulationType simulationType) {
        this.simulationType = simulationType;
        if(simulationType == SimulationType.SENSOR) {
            groupIds.add(ownId);
        }
    }


    public void addGroupId(int groupId) {
        groupIds.add(groupId);
    }

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
