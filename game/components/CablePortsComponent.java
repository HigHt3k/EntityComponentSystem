package game.components;

import com.ecs.component.Component;
import com.ecs.entity.Entity;

import java.awt.geom.Ellipse2D;
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
                cablePortsIn[i] = new CablePort(i, CablePortType.IN);
                cablePortsOut[i] = new CablePort(i, CablePortType.OUT);
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

    /**
     * generate the colliders based on the size and position of the graphics component
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void generatePortColliders(int x, int y, int width, int height) {
        for(CablePort port : cablePortsIn) {
            int id = port.getId();
            port.setCollider(new Ellipse2D.Double(x, y + height/5*(id+1) - height/10, width/5, height/5));
        }
        for(CablePort port : cablePortsOut) {
            int id = port.getId();
            port.setCollider(new Ellipse2D.Double(x + width - width/5, y + height/5*(id+1)  - height/10, width/5, height/5));
        }
    }
}
