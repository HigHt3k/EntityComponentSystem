package game.handler.simulation.markov;

import engine.ecs.entity.Entity;
import game.handler.simulation.SimulationState;
import game.handler.simulation.SimulationType;

import java.util.ArrayList;

public class MarkovStateObject {
    private Entity entity;
    private SimulationType type;
    private SimulationState state;
    private double failureProbability;
    private double failureRecognitionProbability;
    private ArrayList<SimulationState> inputStates;
    private ArrayList<Integer> inputObjects;

    public MarkovStateObject(Entity entity, SimulationType type, SimulationState state, double failureProbability, double failureRecognitionProbability, ArrayList<SimulationState> inputStates) {
        this.entity = entity;
        this.type = type;
        this.state = state;
        this.failureProbability = failureProbability;
        this.failureRecognitionProbability = failureRecognitionProbability;
        this.inputStates = new ArrayList<>(inputStates);
        this.inputObjects = new ArrayList<>();
    }

    public void setInputStates(ArrayList<SimulationState> inputStates) {
        this.inputStates = inputStates;
    }

    public ArrayList<Integer> getInputObjects() {
        return inputObjects;
    }

    public void setInputObjects(ArrayList<Integer> inputObjects) {
        this.inputObjects = inputObjects;
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

    public void setState(SimulationState state) {
        this.state = state;
    }

    public SimulationState getState() {
        return state;
    }

    public SimulationType getType() {
        return type;
    }

    public double getFailureProbability() {
        return failureProbability;
    }

    public double getFailureRecognitionProbability() {
        return failureRecognitionProbability;
    }

    public Entity getEntity() {
        return entity;
    }
}
