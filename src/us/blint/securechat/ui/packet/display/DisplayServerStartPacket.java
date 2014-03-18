package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that the server has been started.
 */
public class DisplayServerStartPacket extends Packet {
	int port;
	
	/**
	 *  Initialize variables
	 * 
	 *  @param port   Port the server was started on
	 */
	public DisplayServerStartPacket(int port) {
		this.port = port;
	}
	
	/**
	 *  Returns the port the server was started on
	 *  
	 *  @return port
	 */
	public int getPort() {
		return port;
	}
}
