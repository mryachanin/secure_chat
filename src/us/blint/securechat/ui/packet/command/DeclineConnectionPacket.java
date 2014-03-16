package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class DeclineConnectionPacket extends Packet {
    private int connectionNumber;
    
    public DeclineConnectionPacket(int connectionName) {
        super();
        this.connectionNumber = connectionName;
    }
    
    public int getConnectionNumber() {
        return connectionNumber;
    }
}
