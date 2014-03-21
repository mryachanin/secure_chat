package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a connection has been accepted by the remote client.
 */
public class DisplayConnectionAcceptedPacket extends Packet {
    private int connectionID;
    
    /**
     *  Initialize variables
     *  
     *  @param connectionID   ID of the connection accepted
     *  @param ip             IP of the connection accepted
     *  @param port           Port of the connection accepted
     */
    public DisplayConnectionAcceptedPacket(int connectionID) {
    	this.connectionID = connectionID;
    }
    
    /**
     *  Returns the ID of the connection that was accepted
     *  
     *  @return connectionID
     */
    public int getConnectionID() {
    	return connectionID;
    }
}
