package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a connection has been declined.
 */
public class DisplayConnectionDeclinedPacket extends Packet {
    private String ip;
    private int port;
    
    /**
     *  Initialize variables
     *  
     *  @param ip     IP of the connectionDeclined
     *  @param port   Port of the connection declined
     */
    public DisplayConnectionDeclinedPacket(String ip, int port) {
    	this.ip = ip;
    	this.port = port;
    }
    
    /**
     *  Returns the IP address of the connection declined
     * 
     *  @return ip
     */
    public String getIP() {
    	return ip;
    }
    
    /**
     *  Returns the port of the connection declined
     * 
     *  @return port
     */
    public int getPort() {
        return port;
    }
}
