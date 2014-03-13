package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class DeclineConnectionPacket extends Packet {
    private String connectionName;
    
    public DeclineConnectionPacket(String connectionName) {
        super();
        this.connectionName = connectionName;
    }
    
    public String getConnectionName() {
        return connectionName;
    }
}
