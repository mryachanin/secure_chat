package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user has declined a connection.
 *  Contains the IP and port of the connection declined.
 */
public class DeclineConnectionPacket extends Packet {
    private String ip;
    private int port;
    
    /**
     *  Initializes Variables
     *  
     *  @param ip     IP if the connection declined
     *  @param port   Port of the connection declined
     */
    public DeclineConnectionPacket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    /**
     *  Returns the IP address of the connection declined
     *  
     *  @return ip
     */
    public String getip() {
        return ip;
    }
    
    /**
     *  Returns the port of the connection declined
     *  @return port
     */
    public int getPort() {
        return port;
    }
}
