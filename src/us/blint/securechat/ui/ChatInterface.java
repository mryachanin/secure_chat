package us.blint.securechat.ui;

import us.blint.securechat.ui.packet.Packet;

/**
 *  This defines how an interface for the chat program should function.
 */
public interface ChatInterface {
    
	/**
	 *  Read a packet of input from the user interface
	 *
	 *  @return Packet
	 */
	public Packet getInput();

	/**
	 *  Sends a packet to the user interface
	 *  
	 *  @param p   Packet to send to the user interface
	 */
	public void send(Packet p);
}