package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

/**
 * The BuildComponent is used to store parameters regarding the inventory of the player.
 * A build component is attached to an entity that should be available for building at has
 * parameters such as the failure ratio and the current amount of available objects of the type.
 */
public class BuildComponent extends Component {

    /**
     * The amount of available objects of this type
     */
    private int amount;

    /**
     * The failure ratio of the build component
     */
    private float failureRatio;

    /**
     * The simulation type of the build component
     */
    private SimulationType simulationType;

    /**
     * The port ID of the build component
     */
    private int portId;

    /**
     * The number of out of control signals that are accepted by the build component
     */
    private int outOfControlSignalsAccepted;

    /**
     * The number of correct signals that are needed by the build component
     */
    private int correctSignalsNeeded;

    /**
     * The ID of the tile that this component belongs to
     */
    private int tileId;

    /**
     * The failure detection ratio of the build component
     */
    private float failureDetectionRatio = 0.9f;

    /**
     * Returns the failure detection ratio of the build component
     * @return the failure detection ratio
     */
    public float getFailureDetectionRatio() {
        return failureDetectionRatio;
    }

    /**
     * Sets the failure detection ratio of the build component
     * @param failureDetectionRatio the new failure detection ratio
     */
    public void setFailureDetectionRatio(float failureDetectionRatio) {
        this.failureDetectionRatio = failureDetectionRatio;
    }

    /**
     * Returns the number of out of control signals that are accepted by the build component
     * @return the number of out of control signals
     */
    public int getOutOfControlSignalsAccepted() {
        return outOfControlSignalsAccepted;
    }

    /**
     * Returns the number of correct signals that are needed by the build component
     * @return the number of correct signals needed
     */
    public int getCorrectSignalsNeeded() {
        return correctSignalsNeeded;
    }

    /**
     * Sets the number of out of control signals that are accepted by the build component
     * @param outOfControlSignalsAccepted the new number of out of control signals
     */
    public void setOutOfControlSignalsAccepted(int outOfControlSignalsAccepted) {
        this.outOfControlSignalsAccepted = outOfControlSignalsAccepted;
    }

    /**
     * Sets the number of correct signals that are needed by the build component
     * @param correctSignalsNeeded the new number of correct signals needed
     */
    public void setCorrectSignalsNeeded(int correctSignalsNeeded) {
        this.correctSignalsNeeded = correctSignalsNeeded;
    }

    /**
     * Sets the port ID of the build component
     * @param portId the new port ID
     */
    public void setPortId(int portId) {
        this.portId = portId;
    }

    /**
     * Returns the port ID of the build component
     * @return the port ID
     */
    public int getPortId() {
        return portId;
    }

    /**
     * Returns the simulation type of the build component
     * @return the simulation type
     */
    public SimulationType getSimulationType() {
        return simulationType;
    }

    /**
     * Set the simulation type of the BuildComponent
     * @param simulationType the simulation type to be set
     */
    public void setSimulationType(SimulationType simulationType) {
        this.simulationType = simulationType;
    }

    /**
     * Set the amount of build components available
     * @param amount the amount to be set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Get the current amount of build components available
     * @return the current amount of build components
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Increase the amount of build components available by 1
     */
    public void addToAmount() {
        amount++;
    }

    /**
     * Decrease the amount of build components available by 1
     */
    public void subtractFromAmount() {
        amount--;
    }

    /**
     * Set the failure ratio of the BuildComponent
     * @param failureRatio the failure ratio to be set
     */
    public void setFailureRatio(float failureRatio) {
        this.failureRatio = failureRatio;
    }

    /**
     * Get the failure ratio of the BuildComponent
     * @return the failure ratio
     */
    public float getFailureRatio() {
        return failureRatio;
    }

    /**
     * Get the tile ID associated with the BuildComponent
     * @return the tile ID
     */
    public int getTileId() {
        return tileId;
    }

    /**
     * Set the tile ID of the BuildComponent
     * @param tileId the tile ID to be set
     */
    public void setTileId(int tileId) {
        this.tileId = tileId;
    }
}
