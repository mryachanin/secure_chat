package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a connection request has been received.
 */
public class DisplayConnectionRequestPacket extends Packet {
	private String ip;
	private int connectionID, port;
	
	/**
	 *  Initialize variables
	 * 
	 *  @param connectionID   ID of the connection
	 *  @param ip             IP address of the client requesting the connection
	 *  @param port           Port of the client requesting the connection
	 */
	public DisplayConnectionRequestPacket(int connectionID, String ip, int port) {
		this.connectionID = connectionID;
	    this.ip = ip;
		this.port = port;
	}
	
	/**
	 *  Returns the ID of the connection
	 *  
	 *  @return connectionID
	 */
	public int getConnectionID() {
	    return connectionID;
	}
	
	/**
	 *  Returns the IP address of the client requesting the connection
	 *  
	 *  @return ip
	 */
	public String getIP() {
		return ip;
	}
	
	/**
	 *  Returns the port of the client requesting the connection
	 *  
	 *  @return port
	 */
	public int getPort() {
	    return port;
	}
}
