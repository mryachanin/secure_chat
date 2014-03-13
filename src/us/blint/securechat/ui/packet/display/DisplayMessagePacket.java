package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

public class DisplayMessagePacket extends Packet {
    private String message, connectionName;
    
    public DisplayMessagePacket(String message, String connectionName) {
        this.message = message;
        this.connectionName = connectionName;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getConnectionName() {
    	return connectionName;
    }
}
