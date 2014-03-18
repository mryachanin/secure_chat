package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user wants to disconnect from a connection.
 */
public class DisconnectPacket extends Packet {
    private String connectionName;
    
    /**
     *  Initializes variables
     *  
     *  @param connectionName   Name of the connection to disconnect from
     */
    public DisconnectPacket(String connectionName) {
        this.connectionName = connectionName;
    }
    
    /**
     *  Returns the connection name to disconnect from
     *  
     *  @return connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }
}
