package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

public class DeclineConnectionPacket extends Packet {
    private String ip;
    private int port;
    
    public DeclineConnectionPacket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public String getip() {
        return ip;
    }
    
    public int getPort() {
        return port;
    }
}
