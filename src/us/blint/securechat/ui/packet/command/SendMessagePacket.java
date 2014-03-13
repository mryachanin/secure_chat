package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class SendMessagePacket extends Packet {
    private String connectionName, message;
    
    public SendMessagePacket(String connectionName, String message) {
        this.connectionName = connectionName;
        this.message = message;
    }
    
    public String getConnectionName() {
        return connectionName;
    }
    
    public String getMessage() {
        return message;
    }
}
