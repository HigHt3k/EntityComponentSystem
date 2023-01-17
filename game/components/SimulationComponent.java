package game.components;

import com.IdGenerator;
import com.ecs.component.Component;

import java.util.ArrayList;

public class SimulationComponent extends Component {
    private float failureRatio;
    private float failureRecognitionRatio = 0.9f;
    private SimulationState simulationState = SimulationState.CORRECT;
    private SimulationType simulationType;
    private ArrayList<Integer> groupIds = new ArrayList<>();

    @Override
    public void update() {

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
        if(simulationType == SimulationType.SENSOR)
            groupIds.add(IdGenerator.generateId());
    }

    public void addGroupId(int groupId) {
        groupIds.add(groupId);
    }

    public void resetGroupIds() {
        groupIds.clear();
    }

    public ArrayList<Integer> getGroupIds() {
        return groupIds;
    }
}
