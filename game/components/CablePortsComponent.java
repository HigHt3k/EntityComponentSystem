package game.components;

import com.ecs.component.Component;

import java.util.Calendar;

public class CablePortsComponent extends Component {
    private int cablePortAmount;
    private CablePort[] cablePorts;

    @Override
    public void update() {

    }

    public void setCablePortAmount(int cablePortAmount) {
        this.cablePortAmount = cablePortAmount;
    }

    public int getCablePortAmount() {
        return cablePortAmount;
    }

    public void generateCablePorts() {
        if(cablePortAmount != 0)
            cablePorts = new CablePort[cablePortAmount];
    }

    public CablePort getCablePort(int id) {
        return cablePorts[id];
    }

}
