package game.components;

import com.ecs.component.Component;
import com.ecs.entity.Entity;

import java.util.ArrayList;

public class CablePortsComponent extends Component {
    private ArrayList<CablePortEntity> cablePorts = new ArrayList<>();

    @Override
    public void update() {

    }

    public ArrayList<CablePortEntity> getCablePorts() {
        return cablePorts;
    }

    public CablePortEntity getCablePort(int id, CablePortType type) {
        for(CablePortEntity port : cablePorts) {
            if(port.getType() == type) {
                if(port.getPortId() == id) {
                    return port;
                }
            }
        }
        return null;
    }

    public CablePortEntity getNextFreePort(CablePortType type) {
        for(CablePortEntity port : cablePorts) {
            if(port.getType() == type) {
                if(port.getConnectedEntity() == null) {
                    return port;
                }
            }
        }
        return null;
    }

    public void addCablePort(CablePortEntity cablePortEntity) {
        this.cablePorts.add(cablePortEntity);
    }

    public int getPortIdOfConnectedEntity(Entity e) {
        for(CablePortEntity port : cablePorts) {
            if(port.getConnectedEntity() == e) {
                return port.getPortId();
            }
        }
        return -1;
    }
}
