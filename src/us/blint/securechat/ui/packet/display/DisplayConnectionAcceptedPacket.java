package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a connection has been accepted by the remote client.
 */
public class DisplayConnectionAcceptedPacket extends Packet {
    private String ip;
    private int connectionID, port;
    
    /**
     *  Initialize variables
     *  
     *  @param connectionID   ID of the connection accepted
     *  @param ip             IP of the connection accepted
     *  @param port           Port of the connection accepted
     */
    public DisplayConnectionAcceptedPacket(int connectionID, String ip, int port) {
    	this.connectionID = connectionID;
    	this.ip = ip;
    	this.port = port;
    }
    
    /**
     *  Returns the ID of the connection that was accepted
     *  
     *  @return connectionID
     */
    public int getConnectionID() {
    	return connectionID;
    }
    
    /**
     *  Returns the IP address of the connection that was accepted
     *  
     *  @return ip
     */
    public String getIP() {
        return ip;
    }
    
    /**
     *  Returns the port of the connection that was accepted
     *  
     *  @return port
     */
    public int getPort() {
        return port;
    }
}
