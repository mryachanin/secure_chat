package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user wants to disconnect from a connection.
 */
public class DisconnectPacket extends Packet {
    private int connectionID;
    
    /**
     *  Initializes variables
     *  
     *  @param connectionID   ID of the connection to disconnect from
     */
    public DisconnectPacket(int connectionID) {
        this.connectionID = connectionID;
    }
    
    /**
     *  Returns the ID of the connection to disconnect from
     *  
     *  @return connectionID
     */
    public int getConnectionID() {
        return connectionID;
    }
}
