package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user wants to accept a connection.
 *
 */
public class AcceptConnectionPacket extends Packet {
    private int connectionID;
    
    /**
     *  Initializes variables
     * 
     *  @param connectionID   ID of the connection to accept
     */
    public AcceptConnectionPacket(int connectionID) {
        this.connectionID = connectionID;
    }
    
    /**
     *  Returns the ID of the connection to accept
     *  
     *  @return connectionID
     */
    public int getConnectionID() {
        return connectionID;
    }
}
