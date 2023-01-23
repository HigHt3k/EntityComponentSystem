package game.handler.simulation.markov;

import game.handler.simulation.SimulationState;
import game.handler.simulation.SimulationType;

public class MarkovStateObject {
    private SimulationType type;
    private SimulationState state;
    private double failureProbability;
    private double failureRecognitionProbability;

    public MarkovStateObject(SimulationType type, SimulationState state, double failureProbability, double failureRecognitionProbability) {
        this.type = type;
        this.state = state;
        this.failureProbability = failureProbability;
        this.failureRecognitionProbability = failureRecognitionProbability;
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
}
