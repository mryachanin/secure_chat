package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class AcceptConnectionPacket extends Packet {
    private String ip, connectionName;
    private int port;
    
    public AcceptConnectionPacket(String ip, int port, String connectionName) {
        this.ip = ip;
        this.port = port;
        this.connectionName = connectionName;
    }
    
    public String getip() {
        return ip;
    }
    
    public int getPort() {
        return port;
    }
    
    public String getConnectionName() {
        return connectionName;
    }
}
