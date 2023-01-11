package game.components;

import com.ecs.component.Component;
import com.ecs.entity.Entity;

import java.util.ArrayList;

public class CablePortsComponent extends Component {
    private int cablePortAmount;
    private CablePort[] cablePortsIn;
    private CablePort[] cablePortsOut;

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
            cablePortsIn = new CablePort[cablePortAmount];
            cablePortsOut = new CablePort[cablePortAmount];
            for(int i = 0; i < cablePortAmount; i++) {
                cablePortsIn[i] = new CablePort();
                cablePortsOut[i] = new CablePort();
            }
        }
    }

    public CablePort[] getCablePortsIn() {
        return cablePortsIn;
    }

    public CablePort[] getCablePortsOut() {
        return cablePortsOut;
    }

    public CablePort getCablePortIn(int id) {
        return cablePortsIn[id];
    }
    public CablePort getCablePortOut(int id) {
        return cablePortsOut[id];
    }

    public ArrayList<CablePort> getAvailablePortsIn() {
        ArrayList<CablePort> availPorts = new ArrayList<>();
        for(int i = 0; i < cablePortAmount; i++) {
            if(cablePortsIn[i].getConnectedEntity() == null) {
                availPorts.add(cablePortsIn[i]);
            }
        }
        return availPorts;
    }

    public ArrayList<CablePort> getAvailablePortsOut() {
        ArrayList<CablePort> availPorts = new ArrayList<>();
        for(int i = 0; i < cablePortAmount; i++) {
            if(cablePortsOut[i].getConnectedEntity() == null) {
                availPorts.add(cablePortsOut[i]);
            }
        }
        return availPorts;
    }

    public int getPortIdIn(Entity e) {
        for(int i = 0; i < cablePortAmount; i++) {
            if(cablePortsIn[i].getConnectedEntity() == e) {
                return i;
            }
        }
        return -1;
    }

    public int getPortIdOut(Entity e) {
        for(int i = 0; i < cablePortAmount; i++) {
            if(cablePortsOut[i].getConnectedEntity() == e) {
                return i;
            }
        }
        return -1;
    }
}
