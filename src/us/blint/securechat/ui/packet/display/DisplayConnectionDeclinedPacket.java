package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

public class DisplayConnectionDeclinedPacket extends Packet {
    private String connectionName;
    
    public DisplayConnectionDeclinedPacket(String connectionName) {
    	this.connectionName = connectionName;
    }
    
    public String getConnectionName() {
    	return connectionName;
    }
}
