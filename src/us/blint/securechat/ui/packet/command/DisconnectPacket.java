package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class DisconnectPacket extends Packet {
    private String connectionName;
    
    public DisconnectPacket(String connectionName) {
        super();
        this.connectionName = connectionName;
    }
    
    public String getConnectionName() {
        return connectionName;
    }
}
