package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

/**
 * The BuildComponent is used to store parameters regarding the inventory of the player.
 * A build component is attached to an entity that should be available for building at has
 * parameters such as the failure ratio and the current amount of available objects of the type.
 */
public class BuildComponent extends Component {
    private int amount;
    private float failureRatio;
    private SimulationType simulationType;
    private int portId;
    private int outOfControlSignalsAccepted;
    private int correctSignalsNeeded;
    private int tileId;
    private float failureDetectionRatio = 0.9f;

    public float getFailureDetectionRatio() {
        return failureDetectionRatio;
    }

    public void setFailureDetectionRatio(float failureDetectionRatio) {
        this.failureDetectionRatio = failureDetectionRatio;
    }

    public int getOutOfControlSignalsAccepted() {
        return outOfControlSignalsAccepted;
    }

    public int getCorrectSignalsNeeded() {
        return correctSignalsNeeded;
    }

    public void setOutOfControlSignalsAccepted(int outOfControlSignalsAccepted) {
        this.outOfControlSignalsAccepted = outOfControlSignalsAccepted;
    }

    public void setCorrectSignalsNeeded(int correctSignalsNeeded) {
        this.correctSignalsNeeded = correctSignalsNeeded;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }

    public int getPortId() {
        return portId;
    }

    public SimulationType getSimulationType() {
        return simulationType;
    }

    public void setSimulationType(SimulationType simulationType) {
        this.simulationType = simulationType;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void addToAmount() {
        amount++;
    }

    public void subtractFromAmount() {
        amount--;
    }

    public void setFailureRatio(float failureRatio) {
        this.failureRatio = failureRatio;
    }

    public float getFailureRatio() {
        return failureRatio;
    }

    public int getTileId() {
        return tileId;
    }

    public void setTileId(int tileId) {
        this.tileId = tileId;
    }
}
