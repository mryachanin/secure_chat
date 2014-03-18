package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a connection request has been received.
 */
public class DisplayConnectionRequestPacket extends Packet {
	private String ip;
	private int port;
	
	/**
	 *  Initialize variables
	 * 
	 *  @param ip     IP address of the client requesting the connection
	 *  @param port   Port of the client requesting the connection
	 */
	public DisplayConnectionRequestPacket(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	/**
	 *  Returns the IP address of the client requesting the connection
	 *  
	 *  @return ip
	 */
	public String getip() {
		return ip;
	}
	
	/**
	 *  Returns the port of the client requesting the connection
	 *  @return port
	 */
	public int getPort() {
	    return port;
	}
}
