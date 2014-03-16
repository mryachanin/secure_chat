package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class AcceptConnectionPacket extends Packet {
    private int connectionNumber;
    
    public AcceptConnectionPacket(int connectionName) {
        super();
        this.connectionNumber = connectionName;
    }
    
    public int getConnectionNumber() {
        return connectionNumber;
    }
}
