package game.components;

import com.IdGenerator;
import com.ecs.component.Component;

public class SimulationComponent extends Component {
    private float failureRatio;
    private SimulationType simulationType;
    private int groupId;

    @Override
    public void update() {

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
        if(simulationType == SimulationType.CPU)
            groupId = IdGenerator.generateId();
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }
}
