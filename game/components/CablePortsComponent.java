package game.components;

import engine.Game;
import engine.ecs.component.Component;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.ImageObject;
import engine.ecs.entity.Entity;
import game.entities.cable.CablePort;
import game.entities.cable.CablePortPosition;
import game.handler.simulation.SimulationType;

import java.util.ArrayList;

public class CablePortsComponent extends Component {
    private final ArrayList<CablePort> cablePorts = new ArrayList<>();
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

            ((ImageObject) this.getEntity().getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)).setImage(Game.res().getTileSet().getTile(imgToLoad));
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

    /**
     * get all entities connected to the input ports of this component, only non null
     * @return
     */
    public ArrayList<Entity> getAllConnectedEntities() {
        ArrayList<Entity> all = new ArrayList<>();

        for(CablePort c : cablePorts) {
            if(c.getType() == CablePortType.IN) {
                if(c.getConnectedEntity() != null) {
                    all.add(c.getConnectedEntity());
                }
            }
        }
        return all;
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
