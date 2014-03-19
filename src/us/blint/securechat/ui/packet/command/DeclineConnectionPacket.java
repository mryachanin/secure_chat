package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user has declined a connection.
 */
public class DeclineConnectionPacket extends Packet {
    private int connectionID;
    
    /**
     *  Initializes Variables
     *  
     *  @param connectionID   ID of the connection declined
     */
    public DeclineConnectionPacket(int connectionID) {
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
