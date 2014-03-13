package us.blint.securechat.ui.packet.display;

import java.util.ArrayList;

import us.blint.securechat.connection.ConnectionRequest;
import us.blint.securechat.ui.packet.Packet;

public class DisplayPendingConnectionsPacket extends Packet {
    private ArrayList<ConnectionRequest> pendingConnections;
    
    public DisplayPendingConnectionsPacket(ArrayList<ConnectionRequest> pendingConnections) {
        super();
        this.pendingConnections = pendingConnections;
    }
    
    public ArrayList<ConnectionRequest> getPendingConnections() {
        return pendingConnections;
    }
}
