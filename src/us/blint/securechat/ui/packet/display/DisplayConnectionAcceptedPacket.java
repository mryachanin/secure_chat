package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a connection has been accepted.
 */
public class DisplayConnectionAcceptedPacket extends Packet {
    private String connectionName;
    
    /**
     *  Initialize variables
     *  
     *  @param connectionName   Name of the connection accepted
     */
    public DisplayConnectionAcceptedPacket(String connectionName) {
    	this.connectionName = connectionName;
    }
    
    /**
     *  Returns the connection name of the connection accepted
     *  
     *  @return connectionName
     */
    public String getConnectionName() {
    	return connectionName;
    }
}
