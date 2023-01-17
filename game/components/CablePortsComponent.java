package game.components;

import com.ecs.component.Component;
import com.ecs.entity.Entity;
import game.entities.CablePort;

import java.util.ArrayList;

public class CablePortsComponent extends Component {
    private ArrayList<CablePort> cablePorts = new ArrayList<>();
    private int[] inIds;
    private int[] outIds;

    @Override
    public void update() {

    }

    public void setIds(int[] inIds, int[] outIds) {
        this.inIds = inIds;
        this.outIds = outIds;
    }

    public int[] getInIds() {
        return inIds;
    }

    public int[] getOutIds() {
        return outIds;
    }

    public ArrayList<CablePort> getCablePorts() {
        return cablePorts;
    }

    public CablePort getCablePort(int id, CablePortType type) {
        for(CablePort port : cablePorts) {
            if(port.getType() == type) {
                if(port.getPortId() == id) {
                    return port;
                }
            }
        }
        return null;
    }

    public CablePort getNextFreePort(CablePortType type) {
        for(CablePort port : cablePorts) {
            if(port.getType() == type) {
                if(port.getConnectedEntity() == null) {
                    return port;
                }
            }
        }
        return null;
    }

    public void addCablePort(CablePort cablePortEntity) {
        this.cablePorts.add(cablePortEntity);
    }

    public int getPortIdOfConnectedEntity(Entity e) {
        for(CablePort port : cablePorts) {
            if(port.getConnectedEntity() == e) {
                return port.getPortId();
            }
        }
        return -1;
    }

    public CablePortType getPortTypeOfConnectedEntity(Entity e) {
        for(CablePort port : cablePorts) {
            if(port.getConnectedEntity() == e) {
                return port.getType();
            }
        }
        return null;
    }
}
