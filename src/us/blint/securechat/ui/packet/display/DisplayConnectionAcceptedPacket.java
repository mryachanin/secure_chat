package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

public class DisplayConnectionAcceptedPacket extends Packet {
    private String connectionName;
    
    public DisplayConnectionAcceptedPacket(String connectionName) {
    	this.connectionName = connectionName;
    }
    
    public String getConnectionName() {
    	return connectionName;
    }
}
