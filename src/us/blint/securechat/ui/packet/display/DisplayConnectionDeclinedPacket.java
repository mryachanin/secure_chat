package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

public class DisplayConnectionDeclinedPacket extends Packet {
    private String ip;
    private int port;
    
    public DisplayConnectionDeclinedPacket(String ip, int port) {
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
