package us.blint.securechat.ui.packet.display;

import java.util.ArrayList;

import us.blint.securechat.connection.Connection;
import us.blint.securechat.ui.packet.Packet;

public class DisplayAcceptedConnectionsPacket extends Packet {
    private ArrayList<Connection> acceptedConnections;
    
    public DisplayAcceptedConnectionsPacket(ArrayList<Connection> acceptedConnections) {
        super();
        this.acceptedConnections = acceptedConnections;
    }
    
    public ArrayList<Connection> getAcceptedConnections() {
        return acceptedConnections;
    }
}
