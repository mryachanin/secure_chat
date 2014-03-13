package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class AcceptConnectionPacket extends Packet {
    private String connectionName;
    
    public AcceptConnectionPacket(String connectionName) {
        super();
        this.connectionName = connectionName;
    }
    
    public String getConnectionName() {
        return connectionName;
    }
}
