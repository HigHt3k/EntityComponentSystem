package game.components;

import com.Game;
import com.ecs.component.Component;
import com.ecs.component.GraphicsComponent;
import com.ecs.entity.Entity;
import game.entities.CablePort;
import game.entities.CablePortPosition;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class CablePortsComponent extends Component {
    private ArrayList<CablePort> cablePorts = new ArrayList<>();
    private int[] inIds;
    private int[] outIds;

    @Override
    public void update() {

    }

    public void updateImage() {
        if(this.getEntity().getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
            int baseId = inIds[0] + 500;

            CablePortPosition in = getCablePort(inIds[0], CablePortType.IN).getPosition();
            CablePortPosition out = getCablePort(outIds[0], CablePortType.OUT).getPosition();
            int imgToLoad = baseId*100 + CablePortPosition.valueOf(in) * 10 + CablePortPosition.valueOf(out);

            this.getEntity().getComponent(GraphicsComponent.class).setImage(Game.res().getTileSet().getTile(imgToLoad));
        }
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
