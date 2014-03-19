package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a connection has been declined.
 */
public class DisplayConnectionDeclinedPacket extends Packet {
    private int connectionID;
    
    /**
     *  Initialize variables
     *  
     *  @param connectionID   ID of the connection declined

     */
    public DisplayConnectionDeclinedPacket(int connectionID) {
    	this.connectionID = connectionID;
    }
    
    /**
     *  Returns the ID of the connection declined
     * 
     *  @return connectionID
     */
    public int getConnectionID() {
    	return connectionID;
    }
}
