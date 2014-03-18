package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that there is already a connection assigned the name the user
 *  tried to assign another connection.
 */
public class DisplayConnectionNameExistsPacket extends Packet {
    private String connectionName;
    
    /**
     *  Initialize variables
     *  
     *  @param connectionName   Connection name already used
     */
    public DisplayConnectionNameExistsPacket(String connectionName) {
        this.connectionName = connectionName;
    }
    
    /**
     *  Returns the connection name already used
     *  
     *  @return connecitonName
     */
    public String getConnectionName() {
        return connectionName;
    }
}
