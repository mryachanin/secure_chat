package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class RequestConnectionPacket extends Packet {
    private String ip, connectionName;
    private int port;
    public RequestConnectionPacket(String ip, int port, String connectionName) {
        super();
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
