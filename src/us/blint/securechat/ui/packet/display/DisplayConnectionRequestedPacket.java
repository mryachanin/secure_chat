package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that a connection request has been successfully sent to another client.
 *
 */
public class DisplayConnectionRequestedPacket extends Packet {
	int connectionID, port;
	String ip;
	
    /**
     *  Initialize variables
     *  
     *  @param connectionID   ID of the connection requested by the user
     *  @param ip             IP of the connection requested by the user
     *  @param port           Port of the connection requested by the user
     */
	public DisplayConnectionRequestedPacket(int connectionID, String ip, int port) {
		this.connectionID = connectionID;
		this.ip = ip;
		this.port = port;
	}
	
    /**
     *  Returns the ID of the connection that was requested by the user
     *  
     *  @return connectionID
     */
	public int getConnectionID() {
		return connectionID;
	}
	
    /**
     *  Returns the IP address of the connection that was requested by the user
     *  
     *  @return ip
     */
    public String getIP() {
        return ip;
    }
    
    /**
     *  Returns the port of the connection that was requested by the user
     *  
     *  @return port
     */
    public int getPort() {
        return port;
    }
}
