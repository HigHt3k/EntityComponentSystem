package game.components;

import com.ecs.component.Component;
import com.ecs.entity.Entity;

import java.util.ArrayList;

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
        if(cablePortAmount != 0) {
            cablePorts = new CablePort[cablePortAmount];
            for(int i = 0; i < cablePortAmount; i++) {
                cablePorts[i] = new CablePort();
            }
        }
    }

    public CablePort[] getCablePorts() {
        return cablePorts;
    }

    public CablePort getCablePort(int id) {
        return cablePorts[id];
    }

    public ArrayList<CablePort> getAvailablePorts() {
        ArrayList<CablePort> availPorts = new ArrayList<CablePort>();
        for(int i = 0; i < cablePortAmount; i++) {
            if(cablePorts[i].getConnectedEntity() == null) {
                availPorts.add(cablePorts[i]);
            }
        }
        return availPorts;
    }

    public int getPortId(Entity e) {
        for(int i = 0; i < cablePortAmount; i++) {
            if(cablePorts[i].getConnectedEntity() == e) {
                return i;
            }
        }
        return -1;
    }
}
